package cnpm31.nhom10.studylife.DbModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExerciseDataModel {

    public String Mssv;
    public String Id;
    public String Name;
    public String Subject;
    public String Deadline;
    public String Content;
    public String Progress;

    public ExerciseDataModel() {}
    public ExerciseDataModel(String mssv, String id, String name, String subject, String deadline, String progress, String content) {
        Mssv = mssv;
        Id = id;
        Name = name;
        Subject = subject;
        Deadline = deadline;
        Progress = progress;
        Content = content;
    }

    // Phương thức chuyển dữ liệu json thành danh sách ExerciseDataModel
    public static ArrayList<ExerciseDataModel> fromJson(JSONArray arrayJson) {

        // Tạo danh sách các Full Subject
        ArrayList<ExerciseDataModel> listExercise = new ArrayList<>();
        int len = arrayJson.length();

        // Lấy từng object trong json array (mảng các json object)
        for (int i = 0; i < len; ++ i) {
            // Chuyển về kiểu Full Subject tương ứng
            ExerciseDataModel exercise = fromJson(arrayJson, i);

            // Nếu thành công thì thêm vào danh sách
            if (exercise != null) {
                listExercise.add(exercise);
            }
        }
        return listExercise;
    }

    // Phương thức chuyển dữ liệu json thành một ExerciseDataModel
    private static ExerciseDataModel fromJson(JSONArray arrayJson, int i) {

        // Lấy đối tượng thứ i trong json array
        JSONObject obj;
        try {
            obj = arrayJson.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Tạo kết quả trả về
        ExerciseDataModel exercise = new ExerciseDataModel();

        // Ánh xạ các thông tin môn học
        try {
            exercise.Mssv = obj.getString("mssv");
            exercise.Id = obj.getString("id");
            exercise.Name = obj.getString("name");
            exercise.Subject = obj.getString("subject");
            exercise.Deadline = obj.getString("deadline");
            exercise.Content = obj.getString("content");
            exercise.Progress = obj.getString("progress");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return exercise;
    }

}
