package cnpm31.nhom10.studylife.DTOModel;

import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleDTO implements Parcelable {




    //region </--Các phương thức mặc định khi implements Parcelable-->
    protected ScheduleDTO(Parcel in) {
    }

    public static final Creator<ScheduleDTO> CREATOR = new Creator<ScheduleDTO>() {
        @Override
        public ScheduleDTO createFromParcel(Parcel in) {
            return new ScheduleDTO(in);
        }

        @Override
        public ScheduleDTO[] newArray(int size) {
            return new ScheduleDTO[size];
        }
    };
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) { }
    //endregion
}
