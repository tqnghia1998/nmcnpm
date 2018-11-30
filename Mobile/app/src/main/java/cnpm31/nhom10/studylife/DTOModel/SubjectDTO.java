package cnpm31.nhom10.studylife.DTOModel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cnpm31.nhom10.studylife.DbModel.CreateSubjectCredentialsDataModel;

public class SubjectDTO  {

    // Id môn học
    public String Id;

    // Tên môn học
    public String Subject;

    // Tình trạng đăng ký
    public boolean isRegisterd;

    public static ArrayList<SubjectDTO> fromJson(JSONArray arrayJson) {

        // Tạo danh sách các DTO
        ArrayList<SubjectDTO> listDTOSubject = new ArrayList<>();
        int len = arrayJson.length();

        // Lấy từng object trong json array (mảng các json object)
        for (int i = 0; i < len; ++i) {

            // Chuyển về kiểu DTO tương ứng
            SubjectDTO dtoSubject = new SubjectDTO();
            try {
                JSONObject model = arrayJson.getJSONObject(i);
                dtoSubject.Id = model.getString("id");
                dtoSubject.Subject = model.getString("subject");
                dtoSubject.isRegisterd = model.getBoolean("isRegistered");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            // Nếu thành công thì thêm vào danh sách
            listDTOSubject.add(dtoSubject);
        }
        return listDTOSubject;
    }
}
