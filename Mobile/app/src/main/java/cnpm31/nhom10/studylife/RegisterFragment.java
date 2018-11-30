package cnpm31.nhom10.studylife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cnpm31.nhom10.studylife.DTOModel.SubjectDTO;
import cnpm31.nhom10.studylife.DbModel.CreateSubjectCredentialsDataModel;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.media.CamcorderProfile.get;
import static android.support.v4.content.ContextCompat.getSystemService;
import static android.support.v4.content.ContextCompat.getSystemServiceName;
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
    public static CustomRecyclerViewAdapter adapterRecycler;

    // Danh sách các đối tượng dùng cho ExpandableRecyclerView
    public static List<SubjectTitle> listSubjectTitle = new ArrayList<>();

    // Các control
    ImageView imgRefresh;
    ImageButton btnBackFromRegister;
    static Spinner spinner;
    static ArrayAdapter<String> adapterSpinner;
    static Spinner filterSpinner;
    static RecyclerView recyclerView;
    public static TextView credit;
    static EditText editSearch;
    Button btnSearch;
    static TextView emptySubject;

    // Biến cờ xác định có vừa mới vào fragment
    int flag = 0;
    public static boolean isSearching = false;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_register, null);

        // Sự kiện thoát khỏi fragment
        btnBackFromRegister = view.findViewById(R.id.btnBackFromRegister);
        btnBackFromRegister.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().remove(RegisterFragment.this).commit();
        });

        // Lấy danh sách khoa truyền từ Activity vào
        listMajor.add("Select an option");
        List<String> listMajorFromServer = null;
        if (getArguments() != null) {
            listMajorFromServer = getArguments().getStringArrayList("listMajor");
        }
        // Nếu danh sách trống (có thể truyền lỗi)
        if (listMajorFromServer == null || listMajorFromServer.size() == 0) {

            // Kết nối lại để lấy danh sách
            Thread threadGetMajor = new Thread(() -> {
                JSONArray arrayJson = JsonHandler.getJsonFromUrl(urlMajor);
                for (int i=0; i<arrayJson.length(); i++){
                    try {
                        listMajor.add(arrayJson.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadGetMajor.start();
            try {
                threadGetMajor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (listMajorFromServer != null) {
            listMajor.addAll(listMajorFromServer);
        }

        // Thiết lập dữ liệu cho recycler view
        emptySubject = view.findViewById(R.id.emptySubject);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Xử lý sự kiện chọn khoa
        spinner = view.findViewById(R.id.spinnerCourse);
        adapterSpinner = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_spinner_item, listMajor);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapterSpinner);
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
                if (!isSearching) refresh();
                else isSearching = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý sự kiện lọc môn
        filterSpinner = view.findViewById(R.id.spinnerFilter);
        final ArrayAdapter<String> adapterFilterSpinner = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_spinner_item
                , new String[] {"Tất cả các môn", "Môn đã đăng ký", "Môn chưa đăng ký"});
        adapterFilterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        filterSpinner.setAdapter(adapterFilterSpinner);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flag < 2) return;
                if (!isSearching) refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Cập nhật số tín chỉ
        credit = view.findViewById(R.id.numCredit);
        updateSumCredit();

        // Sự kiện tìm kiếm
        editSearch = view.findViewById(R.id.editSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            isSearching = true;
            filterSpinner.setSelection(0);
            refresh();
        });

        // Sự kiện refresh
        imgRefresh = view.findViewById(R.id.refresh);
        imgRefresh.setOnClickListener(v -> {
            isSearching = false;
            refresh();
        });

        return view;
    }

    // region Tiểu trình lấy thông tin các môn học
    public static void updateSumCredit(){
        AtomicInteger sumCredit = new AtomicInteger();
        Thread t = new Thread(() -> {
            JSONArray arrayJson = JsonHandler.getJsonFromUrl(urlMajor
                    + "/credit"
                    + "/1612422");
            try {
                sumCredit.set(arrayJson.getJSONObject(0).getInt("credit"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        credit.setText("Số tín chỉ: " + sumCredit + "/22");
    }
    public static void refresh(){
        Thread t = new Thread(() -> {

            // Lấy tên khoa và encode thành UTF-8
            String majorName = URLEncoder
                    .encode(spinner.getSelectedItem().toString())
                    .replace("+", "%20");

            // Lấy tài nguyên từ url tương ứng
            JSONArray arrayJson = JsonHandler.getJsonFromUrl(urlMajor
                    + "/" + (isSearching ? "%25" : majorName)   // Tên khoa (nếu đang tìm kiếm thì chọn hết khoa)
                    + "/%25/%25"                                // Học kỳ và năm học
                    + "/1612422"                                // MSSV
                    + "/0");                                    // Trạng thái NonActive

            // Chuyển thành danh sách các DTOJsubject
            listSubjectTitle.clear();
            List<SubjectDTO> listDTOSubject = SubjectDTO.fromJson(arrayJson);

            // Thêm vào danh sách đối tượng SubjectTitle (dùng cho ExpandableRecyclerView)
            if (listDTOSubject != null) {
                for (int i = 0, j = 0; i < listDTOSubject.size(); i++) {

                    // Nếu đang tìm kiếm
                    if (isSearching) {
                        if (editSearch.getText().toString().equals("")) break;
                        if (listDTOSubject.get(i).Subject.toLowerCase()
                                .contains(editSearch.getText().toString().toLowerCase())
                                || listDTOSubject.get(i).Id.equals(editSearch.getText().toString())) {
                        }
                        else continue;
                    }
                    else {
                        // Lọc môn đã đăng ký/chưa đăng ký
                        if (filterSpinner.getSelectedItemPosition() == 1) { // Nếu chỉ hiển thị môn đã đăng ký
                            if (!listDTOSubject.get(i).isRegisterd) continue;
                        } else if (filterSpinner.getSelectedItemPosition() == 2) { // Nếu chỉ hiển thị môn chưa đăng ký
                            if (listDTOSubject.get(i).isRegisterd) continue;
                        }
                    }

                    // Lấy thông tin môn học từ server
                    JSONArray _arrayJson = JsonHandler.getJsonFromUrl(urlMajor
                            + "/" + listDTOSubject.get(i).Id);
                    List<CreateSubjectCredentialsDataModel> models
                            = CreateSubjectCredentialsDataModel.fromJson(_arrayJson);
                    List<SubjectDetail> subjectDetails = new ArrayList<>();
                    subjectDetails.add(new SubjectDetail(j++, models.size() > 0 ? models.get(0) : null));

                    // Thêm vào Recyler view
                    listSubjectTitle.add(new SubjectTitle(listDTOSubject.get(i).Subject
                            , subjectDetails,
                            listDTOSubject.get(i).isRegisterd,
                            listDTOSubject.get(i).Id));
                    if (isSearching) break;
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        emptySubject.setVisibility(listSubjectTitle.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        // Nếu vừa tìm kiếm xong
        if (isSearching){
            editSearch.setFocusableInTouchMode(false);
            editSearch.setFocusable(false);
            editSearch.setFocusableInTouchMode(true);
            editSearch.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) editSearch.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
            if (listSubjectTitle.size() > 0) {
                for (int i = 0; i < spinner.getCount(); i++) {
                    if (spinner.getItemAtPosition(i).toString().equals(listSubjectTitle.get(0).maJor)){
                        if (i == spinner.getSelectedItemPosition()) isSearching = false;
                        else spinner.setSelection(i);
                        break;
                    }
                }
            }
        }

        // Tạo adapter và đưa vào RecyclerViewAdapter
        adapterRecycler = new CustomRecyclerViewAdapter(listSubjectTitle);
        recyclerView.setAdapter(adapterRecycler);
    }
    // endregion

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