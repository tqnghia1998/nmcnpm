package cnpm31.nhom10.studylife.DTOModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SubjectRegisteredDTO implements Serializable{

    // Id môn học
    public String Id;

    // Tên môn học
    public String Subject;

    // Ngày trong tuần
    public String DayInTheWeek;

    // Lớp học
    public String Room;

    // Tiết
    public String Period;

    // Thời gian bắt đầu
    public String TimeStart;

    // Thời gian kết thúc
    public String TimeFinish ;

    // Tên khoa
    public String Major ;

    // Số tín chỉ
    public int Credit;

    // Giảng viên phụ trách
    public String Teacher;

    // Term of this subject
    public String Term ;

    // Course of this subject
    public String Course;

    //Thời gian bắt đầu học phần
    public String StartOfCourse;

    // Thời gian kết thúc học phần
    public String FinishOfCourse;

    public static ArrayList<SubjectRegisteredDTO> fromJson(JSONArray arrayJson){

        //Tạo danh sách các môn học đã đăng kí
        ArrayList<SubjectRegisteredDTO> listSubReg = new ArrayList<>();
        int len = arrayJson.length();

        //Lấy từng object trong json array (mảng các json object)
        for(int i = 0; i <len; i++)
        {
            //Chuyển về kiểu DTO tương ứng
            SubjectRegisteredDTO dtoSubReg = new SubjectRegisteredDTO();

            try {
                JSONObject model = arrayJson.getJSONObject(i);
                dtoSubReg.Id = model.getString("id");
                dtoSubReg.Subject = model.getString("subject");
                dtoSubReg.DayInTheWeek = model.getString("dayInTheWeek");
                dtoSubReg.Room = model.getString("room");
                dtoSubReg.Period = model.getString("period");
                dtoSubReg.TimeStart = model.getString("timeStart");
                dtoSubReg.TimeFinish = model.getString("timeFinish");
                dtoSubReg.Major = model.getString("major");
                dtoSubReg.Credit = Integer.parseInt(model.getString("credit"));
                dtoSubReg.Teacher = model.getString("teacher");
                dtoSubReg.Term = model.getString("term");
                dtoSubReg.Course = model.getString("course");
                dtoSubReg.StartOfCourse = model.getString("startOfCourse");
                dtoSubReg.FinishOfCourse = model.getString("finishOfCourse");
            } catch (JSONException e)
            {
                e.printStackTrace();
                return null;
            }

            //Nếu thành công thì thêm vào danh sách
            if (dtoSubReg !=null)
            {
                listSubReg.add(dtoSubReg);
            }
        }

        return listSubReg;

    }

}
