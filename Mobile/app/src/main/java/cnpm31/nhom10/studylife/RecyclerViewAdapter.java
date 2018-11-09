package cnpm31.nhom10.studylife;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

import cnpm31.nhom10.studylife.DbModel.RegisteredDataModel;

// Đối tượng lưu giữ tên môn học dùng cho CustomRecyclerViewAdapter
class SubjectDetail implements Parcelable {

    public List<String> dayInTheWeek;
    public int parentIndex;
    public String detail;

    public SubjectDetail(String _detail, int _parentIndex, List<String> _dayInTheWeek){
        detail = _detail;
        parentIndex = _parentIndex;
        dayInTheWeek = _dayInTheWeek;
    }

    protected SubjectDetail(Parcel in) {
        detail = in.readString();
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
        dest.writeString(detail);
    }
    //endregion
}

// Đối tượng lưu giữ thông tin môn học dùng cho CustomRecyclerViewAdapter
class SubjectTitle extends ExpandableGroup<SubjectDetail> {

    public boolean isRegistered;
    public String iD;

    public SubjectTitle(String title, List<SubjectDetail> items, boolean _isRegistered, String _iD) {
        super(title, items);
        isRegistered = _isRegistered;
        iD = _iD;
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

    @Override
    public void onBindChildViewHolder(final SubjectDetailViewHolder holder, final int flatPosition
            , final ExpandableGroup group, int childIndex) {

        // Hiển thị thông tin chi tiết
        SubjectDetail details = (SubjectDetail)group.getItems().get(childIndex);
        holder.txtDetail.setText(details.detail);

        // Cập nhật text của button
        final CheckBox parentCheckBox = listCheckBox.get(details.parentIndex);
        holder.btnRegister.setText(parentCheckBox.isChecked() ? "Hủy đăng ký" : "Đăng ký");

        final String[] x = {new String()};

        // Xử lý sự kiện ấn button Đăng ký
        holder.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // Tạo hộp thoại xác nhận đăng ký
                final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                b.setTitle("Xác nhận");
                b.setMessage("Đăng ký môn học này?");

                // Nếu cancel
                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                // Nếu OK - đồng ý đăng ký
                b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final RegisteredDataModel registeredData = new RegisteredDataModel();

                        // Lấy các thông tin cần thiết
                        registeredData.Mssv = "1612422";
                        registeredData.Id = ((SubjectTitle)group).iD;
                        registeredData.Subject = ((SubjectTitle)group).getTitle();

                        // Vì có nhiều lịch học
                        int numDayInTheWeek = ((SubjectTitle)group).getItems().get(0).dayInTheWeek.size();
                        for (int i = 0; i < numDayInTheWeek; i++) {

                            // Mỗi lịch học POST một lần
                            registeredData.DayInTheWeek = ((SubjectTitle)group).getItems().get(0).dayInTheWeek.get(i);

                            String result = JsonHandler.postRegistered("http://10.0.2.2:51197/api/registered"
                                    , registeredData, holder.txtDetail.getContext());

                            // Toast.makeText(holder.txtDetail.getContext(), result, Toast.LENGTH_LONG).show();
                        }

                        // Cập nhật text của button và check box
                        parentCheckBox.setChecked(!parentCheckBox.isChecked());
                        holder.btnRegister.setText(parentCheckBox.isChecked() ? "Hủy đăng ký" : "Đăng ký");
                    }
                });
                b.show();
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(SubjectTitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.txtTitle.setText(group.getTitle());
        holder.checkBox.setChecked(((SubjectTitle)group).isRegistered);
        listCheckBox.add(holder.checkBox);
    }
}
