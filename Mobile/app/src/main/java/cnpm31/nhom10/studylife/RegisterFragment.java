package cnpm31.nhom10.studylife;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cnpm31.nhom10.studylife.DbModel.CreateSubjectCredentialsDataModel;
import cnpm31.nhom10.studylife.DbModel.SubjectDataModel;

import static cnpm31.nhom10.studylife.MainActivity.urlMajor;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends android.app.Fragment {

    // Tạo danh sách các Full Subject (bao gồm thông tin môn học và các lịch học tương ứng)
    ArrayList<CreateSubjectCredentialsDataModel> listFullSubject;

    // Tạo danh sách các tên khoa để hiển thị Spinner
    List<String> listMajor = new ArrayList<>();

    // Danh sách các đối tượng dùng cho ExpandableRecyclerView
    List<SubjectTitle> listSubjectTitle = new ArrayList<>();

    // Tạo danh sách các Full Subject của từng khoa
    List<CreateSubjectCredentialsDataModel> listSubject = new ArrayList<>();

    // Các control
    Spinner spinner;
    RecyclerView recyclerView;

    // Biến cờ xác định có vừa mới vào fragment hay không
    int flag = 0;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_register, null);


        // Lấy danh sách khoa truyền từ Activity vào
        listMajor.add("Select an option");
        listMajor.addAll(getArguments().getStringArrayList("listMajor"));

        // Đưa dữ liệu vào spinner
        spinner = view.findViewById(R.id.spinnerCourse);
        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_spinner_item, listMajor);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapterSpinner);

        // Thiết lập dữ liệu cho recycler view
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Xử lý sự kiện chọn khoa
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {

                // flag = 0 tức vừa với vào fragment
                if (flag == 0) {
                    flag++;
                    return;
                }
                // flag = 1 tức người dùng lựa chọn spinner lần đầu tiên
                if (flag == 1) {
                    // Xóa lựa chọn "Select an option"
                    listMajor.remove(0);
                    // Tăng flag
                    flag++;
                    // Vì kích thước listMajor giảm nên vị trí các item thay đổi
                    // Do đó, lần chọn hiện tại vô dụng, phải chọn tại vị trí position - 1
                    spinner.setSelection(position - 1);
                    return;
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Lấy tên khoa và encode thành UTF-8
                        String majorName = URLEncoder
                                .encode(parent.getItemAtPosition(position).toString())
                                .replace("+", "%20");

                        // Lấy tài nguyên từ url tương ứng
                        JSONArray arrayJson = JsonHandler.getJsonFromUrl(urlMajor + "/" + majorName + "/%25/%25");

                        // Chuyển thành danh sách các Full Subject
                        listSubject = CreateSubjectCredentialsDataModel.fromJson(arrayJson);
                        listSubjectTitle.clear();

                        // Chuyển các Full Subject sang các đối tượng SubjectTitle và SubjectDetail
                        for(int i = 0; i < listSubject.size(); i++)
                        {
                            CreateSubjectCredentialsDataModel fullSubject = listSubject.get(i);
                            String lichHoc = new String();
                            List<String> numLichHoc = new ArrayList<>();
                            for (int j = 0; j < fullSubject.Schedule.size(); j++)
                            {
                                lichHoc += "\n\n  + " + fullSubject.Schedule.get(j).DayInTheWeek
                                        + ", phòng " + fullSubject.Schedule.get(j).Room
                                        + ", tiết " + fullSubject.Schedule.get(j).Period
                                        + " (" + fullSubject.Schedule.get(j).TimeStart + "-"
                                        + fullSubject.Schedule.get(j).TimeFinish + ")";
                                numLichHoc.add(fullSubject.Schedule.get(j).DayInTheWeek);
                            }
                            // 1 SubjectTitle tương ứng với 1 list SubjectDetail (quy định của adapter)
                            List<SubjectDetail> details = new ArrayList<>();
                            details.add(new SubjectDetail(
                                    "- Giảng viên: " + fullSubject.Subject.Teacher + "\n\n"
                                    + "- Số tín chỉ: " + fullSubject.Subject.Credit + "\n\n"
                                    + "- Học kỳ: " + fullSubject.Subject.Term + "/" + fullSubject.Subject.Course + "\n\n"
                                    + "- Thời gian: " + fullSubject.Subject.TimeStart + "-" + fullSubject.Subject.TimeFinish + "\n\n"
                                    + "- Lịch học: "  + lichHoc, i, numLichHoc));

                            // Thêm vào danh sách đối tượng SubjectTitle (dùng cho ExpandableRecyclerView)
                            listSubjectTitle.add(new SubjectTitle(listSubject.get(i).Subject.Subject
                                    , details, false, fullSubject.Subject.Id));
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Tạo adapter và đưa vào RecyclerViewAdapter
                CustomRecyclerViewAdapter adapterRecycler = new CustomRecyclerViewAdapter(listSubjectTitle);
                recyclerView.setAdapter(adapterRecycler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRegisterFragmentListener {
    }
}