package cnpm31.nhom10.studylife;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cnpm31.nhom10.studylife.DbModel.CreateSubjectCredentialsDataModel;
import cnpm31.nhom10.studylife.DbModel.ExerciseDataModel;
import cnpm31.nhom10.studylife.DbModel.RegisteredDataModel;

import static cnpm31.nhom10.studylife.ExerciseFragment.adapterExerciseRecycler;
import static cnpm31.nhom10.studylife.MainActivity.urlExercise;
import static cnpm31.nhom10.studylife.RegisterFragment.adapterRecycler;

// Đối tượng lưu giữ tên môn học dùng cho CustomRecyclerViewAdapter
class ExerciseDetail implements Parcelable {

    int parentIndex;
    ExerciseDataModel detail;

    public ExerciseDetail(int _parentIndex, ExerciseDataModel _detail){
        parentIndex = _parentIndex;
        detail = _detail;
    }

    protected ExerciseDetail(Parcel in) {
        detail.Id = in.readString();
    }

    //region </--Các phương thức mặc định khi implements Parcelable-->
    public static final Creator<ExerciseDetail> CREATOR = new Creator<ExerciseDetail>() {
        @Override
        public ExerciseDetail createFromParcel(Parcel in) {
            return new ExerciseDetail(in);
        }

        @Override
        public ExerciseDetail[] newArray(int size) {
            return new ExerciseDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(detail.Id);
    }
    //endregion
}

// Đối tượng lưu giữ thông tin môn học dùng cho CustomRecyclerViewAdapter
class ExerciseTitle extends ExpandableGroup<ExerciseDetail> {

    public String iD;
    public String Progress;
    public String Deadline;

    public ExerciseTitle(String title, List<ExerciseDetail> items, String _iD, String _Progress, String _Deadline) {
        super(title, items);
        iD = _iD;
        Progress = _Progress;
        Deadline = _Deadline;
    }
}

// ViewHoder của tiêu đề bài tập
class ExerciseTitleViewHolder extends GroupViewHolder {

    // View của tiêu đề một môn học
    public TextView txtExerciseTitle;
    public TextView txtExerciseProgress;
    public TextView txtExerciseDeadline;

    public ExerciseTitleViewHolder(View view) {
        super(view);
        txtExerciseTitle = view.findViewById(R.id.txtExerciseTitle);
        txtExerciseProgress = view.findViewById(R.id.txtExerciseProgress);
        txtExerciseDeadline = view.findViewById(R.id.txtExerciseDeadline);
    }
}

// ViewHolder của thông tin bài tập
class ExerciseDetailViewHolder extends ChildViewHolder {

    // View của thông tin một môn học
    public TextView txtExerciseDetail;
    public TextView txtExerciseDetailProgress;
    public SeekBar seekBar;
    public Button btnExerciseEdit;
    public Button btnExerciseRemove;

    public ExerciseDetailViewHolder(final View itemView) {
        super(itemView);
        txtExerciseDetail = itemView.findViewById(R.id.txtExerciseDetail);
        txtExerciseDetailProgress = itemView.findViewById(R.id.txtExerciseDetailProgress);
        seekBar = itemView.findViewById(R.id.seekBar);
        btnExerciseEdit = itemView.findViewById(R.id.btnExerciseEdit);
        btnExerciseRemove = itemView.findViewById(R.id.btnExerciseRemove);
    }
}

// CustomAdapter cho RecyclerViewAdapter
class ExerciseCustomRecyclerViewAdapter extends ExpandableRecyclerViewAdapter<ExerciseTitleViewHolder,ExerciseDetailViewHolder> {

    // Biến cờ hiệu
    boolean flag;

    // Danh sách TextView tiến độ của các bài tập
    List<TextView> listProgress = new ArrayList<>();

    // Danh sách TextView deadline của các bài tập
    List<TextView> listDeadline = new ArrayList<>();

    public ExerciseCustomRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ExerciseTitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_parent_exercise, parent, false);
        return new ExerciseTitleViewHolder(view);
    }

    @Override
    public ExerciseDetailViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_child_exercise, parent, false);
        return new ExerciseDetailViewHolder(view);
    }

    @Override public void onGroupExpanded(int positionStart, int itemCount) {
        if (itemCount > 0) {
            int groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos;
            notifyItemRangeInserted(positionStart, itemCount);
            for (ExpandableGroup grp : adapterExerciseRecycler.getGroups()) {
                if (grp != adapterExerciseRecycler.getGroups().get(groupIndex)) {
                    if (this.isGroupExpanded(grp)) {
                        this.toggleGroup(grp);
                    }
                }
            }
        }
    }

    @Override
    public void onBindChildViewHolder(final ExerciseDetailViewHolder holder, final int flatPosition
            , final ExpandableGroup group, int childIndex) {

        // Lấy đối tượng ExerciseDetail để có thông tin chi tiết
        ExerciseDetail details = (ExerciseDetail)group.getItems().get(childIndex);

        // Set thông tin bài tập
        StringBuilder onScreen = new StringBuilder();
        onScreen.append("- Tên bài tập\t: ").append(details.detail.Name);
        if (!details.detail.Subject.equals("Không")) {
            onScreen.append("\n\n- Thuộc môn\t: ").append(details.detail.Subject);
        }
        onScreen.append("\n\n- Hạn chót\t\t: ").append(details.detail.Deadline)
                .append("\n\n- Nội dung\t\t: ").append(details.detail.Content);
        holder.txtExerciseDetail.setText(onScreen.toString());

        // Thiết lập SeekBar, lấy TextView hiển thị Progress bên ngoài (title) của bài tập
        final TextView parentTextView = listProgress.get(details.parentIndex);

        // Gán vào SeekBar bên trong
        String progress = parentTextView.getText().toString();
        progress = progress.substring(0, progress.length() - 1);
        holder.seekBar.setProgress(Integer.parseInt(progress));
        holder.txtExerciseDetailProgress.setText(parentTextView.getText());

        // Xử lý sự kiện cập nhật tiến độ
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Chỉ thay đổi khi nhận thấy bắt đầu điều chỉnh SeekBar
                if (!flag) return;

                // Cập nhật chỉ số nằm bên phải SeekBar
                holder.txtExerciseDetailProgress.setText(String.valueOf(progress) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                flag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                flag = false;

                // Sau khi điều chỉnh xong, cập nhật tiến dộ lên server
                details.detail.Progress = String.valueOf(seekBar.getProgress());
                JsonHandler.updateExercise(urlExercise + "/put", new ExerciseDataModel(
                        "1612422",
                        details.detail.Id,
                        details.detail.Name,
                        details.detail.Subject,
                        details.detail.Deadline,
                        details.detail.Progress,
                        details.detail.Content
                ), holder.seekBar.getContext());

                // Cập nhật lên TextView bên ngoài (nếu thành công)
                listProgress.get(details.parentIndex).setText(String.valueOf(seekBar.getProgress()) + "%");
            }
        });

        // Xử lý sự kiện sửa bài tập
        holder.btnExerciseEdit.setOnClickListener(v -> {

            // Hiển thị Fragment sửa bài tập
            CreateExerciseFragment editExerciseFragment = new CreateExerciseFragment();

            // Tạo bundle chứa thông tin bài tập hiện tại
            Bundle bundle = new Bundle();
            bundle.putString("id", details.detail.Id);
            bundle.putString("name", details.detail.Name);
            bundle.putString("subject", details.detail.Subject);
            bundle.putString("deadline", details.detail.Deadline);
            bundle.putString("progress", details.detail.Progress);
            bundle.putString("content", details.detail.Content);
            editExerciseFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = ((Activity)holder.btnExerciseEdit
                    .getContext()).getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.createExerciseFrame, editExerciseFragment);
            fragmentTransaction.commit();
        });

        // Xử lý sự kiện xóa bài tập
        holder.btnExerciseRemove.setOnClickListener(v -> {

            // Tạo hộp thoại xác nhận
            final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
            b.setTitle("Xác nhận");
            b.setMessage("Xóa bài tập này?");

            // Nếu cancel
            b.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

            // Nếu OK - đồng ý xóa
            b.setPositiveButton("Ok", (dialog, id) -> {
                JsonHandler.deleteExercise(urlExercise, details.detail.Id, holder.btnExerciseRemove.getContext());
            }).show();
        });
    }

    @Override
    public void onBindGroupViewHolder(ExerciseTitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.txtExerciseTitle.setText(group.getTitle());
        holder.txtExerciseProgress.setText(((ExerciseTitle)group).Progress + "%");
        holder.txtExerciseDeadline.setText(((ExerciseTitle)group).Deadline);
        listProgress.add(holder.txtExerciseProgress);
        listDeadline.add(holder.txtExerciseDeadline);
    }
}
