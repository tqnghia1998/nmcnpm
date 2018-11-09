package cnpm31.nhom10.studylife.DbModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// Các thông tin của một Full Subject (bao gồm cả môn học và các lịch học)
/// </summary>
public class CreateSubjectCredentialsDataModel {

    public SubjectDataModel Subject;
    public List<ScheduleDataModel> Schedule;

    public CreateSubjectCredentialsDataModel() {
        Subject = new SubjectDataModel();
        Schedule = new ArrayList<>();
    }

    // Phương thức chuyển dữ liệu json thành danh sách Full Subject
    public static ArrayList<CreateSubjectCredentialsDataModel> fromJson(JSONArray arrayJson) {

        // Tạo danh sách các Full Subject
        ArrayList<CreateSubjectCredentialsDataModel> listFullSubject = new ArrayList<>();
        int len = arrayJson.length();

        // Lấy từng object trong json array (mảng các json object)
        for (int i = 0; i < len; ++ i) {
            // Chuyển về kiểu Full Subject tương ứng
            CreateSubjectCredentialsDataModel fullSubject = fromJson(arrayJson, i);

            // Nếu thành công thì thêm vào danh sách
            if (fullSubject != null) {
                listFullSubject.add(fullSubject);
            }
        }
        return listFullSubject;
    }

    // Phương thức chuyển dữ liệu json thành một Full Subject
    private static CreateSubjectCredentialsDataModel fromJson(JSONArray arrayJson, int i) {

        // Lấy đối tượng thứ i trong json array
        JSONObject obj;
        try {
            obj = arrayJson.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Tạo kết quả trả về
        CreateSubjectCredentialsDataModel fullSubject = new CreateSubjectCredentialsDataModel();

        // Ánh xạ các thông tin môn học
        try {
            JSONObject subject = obj.getJSONObject("subject");
            fullSubject.Subject.Id = subject.getString("id");
            fullSubject.Subject.Major = subject.getString("major");
            fullSubject.Subject.Subject = subject.getString("subject");
            fullSubject.Subject.Credit = Integer.parseInt(subject.getString("credit"));
            fullSubject.Subject.Teacher = subject.getString("teacher");
            fullSubject.Subject.Term = subject.getString("term");
            fullSubject.Subject.Course = subject.getString("course");
            fullSubject.Subject.TimeStart = subject.getString("timeStart");
            fullSubject.Subject.TimeFinish = subject.getString("timeFinish");
            fullSubject.Subject.Status = Byte.parseByte(subject.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        // Ánh xạ các lịch học
        try {
            // Lấy json array của các lịch học
            JSONArray listSchedule = obj.getJSONArray("schedule");

            // Vì lịch học là một mảng nên phải duyệt
            for (int j = 0; j < listSchedule.length(); j++) {
                // Lấy lịch học thứ i
                JSONObject schedule = listSchedule.getJSONObject(j);

                // Tạo một đối tượng tương ứng
                ScheduleDataModel model = new ScheduleDataModel();
                model.Id = schedule.getString("id");
                model.DayInTheWeek = schedule.getString("dayInTheWeek");
                model.Period = schedule.getString("period");
                model.Room = schedule.getString("room");
                model.TimeStart = schedule.getString("timeStart");
                model.TimeFinish = schedule.getString("timeFinish");
                fullSubject.Schedule.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fullSubject;
    }
}
