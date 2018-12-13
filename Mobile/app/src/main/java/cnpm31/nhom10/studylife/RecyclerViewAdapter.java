package cnpm31.nhom10.studylife;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cnpm31.nhom10.studylife.DbModel.CreateSubjectCredentialsDataModel;
import cnpm31.nhom10.studylife.DbModel.RegisteredDataModel;

import static cnpm31.nhom10.studylife.MainActivity.urlMajor;
import static cnpm31.nhom10.studylife.RegisterFragment.adapterRecycler;
import static cnpm31.nhom10.studylife.RegisterFragment.credit;
import static cnpm31.nhom10.studylife.RegisterFragment.updateSumCredit;

// Đối tượng lưu giữ tên môn học dùng cho CustomRecyclerViewAdapter
class SubjectDetail implements Parcelable {

    int parentIndex;
    CreateSubjectCredentialsDataModel detail;

    public SubjectDetail(int _parentIndex, CreateSubjectCredentialsDataModel _detail){
        parentIndex = _parentIndex;
        detail = _detail;
    }

    protected SubjectDetail(Parcel in) {
        detail.Subject.Subject = in.readString();
    }

    //region </--Các phương thức mặc định khi implements Parcelable-->
    public static final Creator<SubjectDetail> CREATOR = new Creator<SubjectDetail>() {
        @Override
        public SubjectDetail createFromParcel(Parcel in) {
            return new SubjectDetail(in);
        }

        @Override
        public SubjectDetail[] newArray(int size) {
            return new SubjectDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(detail.Subject.Subject);
    }
    //endregion
}

// Đối tượng lưu giữ thông tin môn học dùng cho CustomRecyclerViewAdapter
class SubjectTitle extends ExpandableGroup<SubjectDetail> {

    public boolean isRegistered;
    public String iD;
    public String maJor;

    public SubjectTitle(String title, List<SubjectDetail> items, boolean _isRegistered, String _iD) {
        super(title, items);
        isRegistered = _isRegistered;
        iD = _iD;
        maJor = items.get(0).detail.Subject.Major;
    }
}

// ViewHoder của tiêu đề môn học
class SubjectTitleViewHolder extends GroupViewHolder {

    // View của tiêu đề một môn học
    public TextView txtTitle;
    public CheckBox checkBox;

    public SubjectTitleViewHolder(View view) {
        super(view);
        txtTitle = view.findViewById(R.id.txtTitle);
        checkBox = view.findViewById(R.id.checkBox);
    }
}

// ViewHolder của thông tin môn học
class SubjectDetailViewHolder extends ChildViewHolder {

    // View của thông tin một môn học
    public TextView txtDetail;
    public Button btnRegister;

    public SubjectDetailViewHolder(final View itemView) {
        super(itemView);
        txtDetail = itemView.findViewById(R.id.txtDetail);
        btnRegister = itemView.findViewById(R.id.btnRegister);
    }
}

// CustomAdapter cho RecyclerViewAdapter
class CustomRecyclerViewAdapter extends ExpandableRecyclerViewAdapter<SubjectTitleViewHolder,SubjectDetailViewHolder> {

    // Danh sách checkbox của các môn
    List<CheckBox> listCheckBox = new ArrayList<>();

    public CustomRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public SubjectTitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_parent, parent, false);
        return new SubjectTitleViewHolder(view);
    }

    @Override
    public SubjectDetailViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_child, parent, false);
        return new SubjectDetailViewHolder(view);
    }

    @Override public void onGroupExpanded(int positionStart, int itemCount) {
        if (itemCount > 0) {
            int groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos;
            notifyItemRangeInserted(positionStart, itemCount);
            for (ExpandableGroup grp : adapterRecycler.getGroups()) {
                if (grp != adapterRecycler.getGroups().get(groupIndex)) {
                    if (this.isGroupExpanded(grp)) {
                        this.toggleGroup(grp);
                    }
                }
            }
        }
    }

    @Override
    public void onBindChildViewHolder(final SubjectDetailViewHolder holder, final int flatPosition
            , final ExpandableGroup group, int childIndex) {

        // Hiển thị thông tin chi tiết
        SubjectDetail details = (SubjectDetail)group.getItems().get(childIndex);
        StringBuilder onScreen = new StringBuilder();
        onScreen.append("- Mã môn học\t: ").append(details.detail.Subject.Id)
                .append("\n\n- Giảng viên\t\t: ").append(details.detail.Subject.Teacher)
                .append("\n\n- Số tín chỉ\t\t\t: ").append(details.detail.Subject.Credit)
                .append("\n\n- Học kỳ\t\t\t\t: ").append(details.detail.Subject.Term)
                .append("/").append(details.detail.Subject.Course)
                .append("\n\n- Thời gian\t\t\t: ").append(details.detail.Subject.TimeStart)
                .append(" đến ").append(details.detail.Subject.TimeFinish)
                .append("\n\n+ ");

        for (int i = 0; i < details.detail.Schedule.size(); i++) {
            String dayVietnamese = "Thứ hai";
            switch (details.detail.Schedule.get(i).DayInTheWeek) {
                case "Tuesday": dayVietnamese = "Thứ ba"; break;
                case "Wednesday": dayVietnamese = "Thứ tư"; break;
                case "Thursday": dayVietnamese = "Thứ năm"; break;
                case "Friday": dayVietnamese = "Thứ sáu"; break;
                case "Saturday": dayVietnamese = "Thứ bảy"; break;
                case "Sunday": dayVietnamese = "Chủ nhật"; break;
            }
            onScreen.append(dayVietnamese)
                    .append(", phòng ")
                    .append(details.detail.Schedule.get(i).Room)
                    .append(", tiết ")
                    .append(details.detail.Schedule.get(i).Period)
                    .append(" (").append(details.detail.Schedule.get(i).TimeStart)
                    .append(" - ").append(details.detail.Schedule.get(i).TimeFinish).append(")");
            if (i < details.detail.Schedule.size() - 1) {
                onScreen.append("\n\n+ ");
            }
        }


        holder.txtDetail.setText(onScreen.toString());

        // Cập nhật text của button
        final CheckBox parentCheckBox = listCheckBox.get(details.parentIndex);
        holder.btnRegister.setText(parentCheckBox.isChecked() ? "Hủy đăng ký" : "Đăng ký");

        // Xử lý sự kiện ấn button Đăng ký
        holder.btnRegister.setOnClickListener(v -> {

            // Tạo hộp thoại xác nhận đăng ký
            final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
            b.setTitle("Xác nhận");
            b.setMessage((parentCheckBox.isChecked() ? "Hủy đ" : "Đ") + "ăng ký môn học này?");

            // Nếu cancel
            b.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

            // Nếu OK - đồng ý đăng ký
            b.setPositiveButton("Ok", (dialog, id) -> {

                List <RegisteredDataModel> registeredDataModelList = new ArrayList<>();
                int numDayInTheWeek = ((SubjectTitle)group).getItems().get(0).detail.Schedule.size();
                for (int i = 0; i < numDayInTheWeek; i++) {
                    RegisteredDataModel registeredData = new RegisteredDataModel();
                    registeredData.Mssv = "1612422";
                    registeredData.Id = ((SubjectTitle)group).iD;
                    registeredData.Subject = group.getTitle();
                    registeredData.DayInTheWeek = ((SubjectTitle)group).getItems().get(0).detail.Schedule.get(i).DayInTheWeek;
                    registeredDataModelList.add(registeredData);
                }

                // Nếu checkbox chưa check, tức muốn đăng ký
                if (!parentCheckBox.isChecked()) {
                    JsonHandler.postRegistered("http://10.0.2.2:51197/api/registered",
                            registeredDataModelList,
                            holder.txtDetail.getContext(),
                            parentCheckBox,
                            holder.btnRegister);
                }
                else {
                    JsonHandler.deleteRegistered("http://10.0.2.2:51197/api/registered",
                            registeredDataModelList.get(0),
                            holder.txtDetail.getContext(),
                            parentCheckBox,
                            holder.btnRegister);
                }
            });
            b.show();
        });
    }

    @Override
    public void onBindGroupViewHolder(SubjectTitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.txtTitle.setText(group.getTitle());
        holder.checkBox.setChecked(((SubjectTitle)group).isRegistered);
        listCheckBox.add(holder.checkBox);
    }
}
