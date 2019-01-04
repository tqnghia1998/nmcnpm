package cnpm31.nhom10.studylife;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cnpm31.nhom10.studylife.DbModel.ExerciseDataModel;

import static cnpm31.nhom10.studylife.MainActivity.mssv;
import static cnpm31.nhom10.studylife.MainActivity.urlExercise;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentListener} interface
 * to handle interaction events.
 */
public class ExerciseFragment extends android.app.Fragment {

    public static ExerciseCustomRecyclerViewAdapter adapterExerciseRecycler;
    public static RecyclerView exerciseRecyclerView;

    // Các danh sách Parent và Child cho RecyclerView
    public static List<ExerciseTitle> listExerciseTitle = new ArrayList<>();
    static List<ExerciseDataModel> exerciseDataModels = new ArrayList<>();

    // Spinner sắp xếp bài tập
    static Spinner spinnerExerciseFilter;
    ArrayAdapter<String> adapterExerciseSpinnerFilter;

    // Spinner lọc theo môn
    static Spinner spinnerExerciseSubjectFilter;
    static ArrayAdapter<String> adapterSpinnerExerciseSubjectFilter;
    static List<String> listSubjectName = new ArrayList<>();

    // Các control khác
    ImageButton btnBackFromExercise;
    Button btnAddExercise;

    // Các biến cho việc tìm kiếm
    static TextView emptyExercise;
    static Button btnExerciseSearch;
    static EditText editExerciseSearch;
    static boolean isExerciseSearch = false;

    public ExerciseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_exercise, null);
        emptyExercise = view.findViewById(R.id.emptyExercise);

        // Khởi tạo spinner lọc theo môn
        listSubjectName.add("Tất cả");
        spinnerExerciseSubjectFilter = view.findViewById(R.id.spinnerExerciseSubjectFilter);
        adapterSpinnerExerciseSubjectFilter = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_spinner_item, listSubjectName);
        adapterSpinnerExerciseSubjectFilter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerExerciseSubjectFilter.setAdapter(adapterSpinnerExerciseSubjectFilter);
        spinnerExerciseSubjectFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listSubjectName.size() > 1) refreshExercise();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Khởi tạo spinner sắp xếp bài tập
        spinnerExerciseFilter = view.findViewById(R.id.spinnerExerciseFilter);
        adapterExerciseSpinnerFilter = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_spinner_item, new String[] {"Theo hạn chót", "Theo tiến độ"});
        adapterExerciseSpinnerFilter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerExerciseFilter.setAdapter(adapterExerciseSpinnerFilter);
        spinnerExerciseFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshExercise();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Khởi tạo và hiển thị các bài tập
        exerciseRecyclerView = view.findViewById(R.id.exerciseRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        exerciseRecyclerView.setLayoutManager(layoutManager);
        refreshExercise();

        // Sự kiện thêm bài tập mới
        btnAddExercise = view.findViewById(R.id.btnAddExercise);
        btnAddExercise.setOnClickListener(v -> {
            CreateExerciseFragment createExerciseFragment = new CreateExerciseFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.createExerciseFrame, createExerciseFragment);
            fragmentTransaction.commit();
        });

        // Sự kiện thoát
        btnBackFromExercise = view.findViewById(R.id.btnBackFromExercise);
        btnBackFromExercise.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().remove(ExerciseFragment.this).commit();
        });


        // Sự kiện tìm kiếm
        editExerciseSearch = view.findViewById(R.id.editExerciseSearch);
        btnExerciseSearch = view.findViewById(R.id.btnExerciseSearch);
        btnExerciseSearch.setOnClickListener(v -> {
            isExerciseSearch = true;
            spinnerExerciseSubjectFilter.setSelection(0);
            refreshExercise();
        });
        return view;
    }

    // region Tiểu trình lấy thông tin các bài tập
    public static void refreshExercise() {

        // Xóa dữ liệu
        listExerciseTitle.clear();

        // Kết nối lại để lấy danh sách
        Thread threadGetExercise = new Thread(() -> {
            JSONArray jsonArray = JsonHandler.getJsonFromUrl(urlExercise + "/" + mssv);
            exerciseDataModels = ExerciseDataModel.fromJson(jsonArray);
        });
        threadGetExercise.start();
        try {
            threadGetExercise.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Thêm các môn vào bộ lọc môn
        listSubjectName.clear();
        listSubjectName.add("Tất cả");
        for (int i = 0; i < exerciseDataModels.size(); i++) {
            if (!listSubjectName.contains(exerciseDataModels.get(i).Subject)
                    && !exerciseDataModels.get(i).Subject.equals("Không")) {
                listSubjectName.add(exerciseDataModels.get(i).Subject);
            }
        }
        adapterSpinnerExerciseSubjectFilter.notifyDataSetChanged();

        // Sắp xếp theo deadline/tiến độ
        for (int i = 0; i < exerciseDataModels.size() - 1; i++) {
            for (int j = i + 1; j < exerciseDataModels.size(); j++) {
                // Sắp xếp theo deadline
                if (spinnerExerciseFilter.getSelectedItemPosition() == 0) {
                    int deadlineI = Integer.parseInt(GetCountDown(exerciseDataModels.get(i).Deadline
                            , true));
                    int deadlineJ = Integer.parseInt(GetCountDown(exerciseDataModels.get(j).Deadline
                            , true));
                    if (deadlineI > deadlineJ) {
                        ExerciseDataModel temp = exerciseDataModels.get(i);
                        exerciseDataModels.set(i, exerciseDataModels.get(j));
                        exerciseDataModels.set(j, temp);
                    }
                }
                // Sắp xếp theo tiến độ
                else {
                    int progressI = Integer.parseInt(exerciseDataModels.get(i).Progress);
                    int progressJ = Integer.parseInt(exerciseDataModels.get(j).Progress);
                    if (progressI < progressJ) {
                        ExerciseDataModel temp = exerciseDataModels.get(i);
                        exerciseDataModels.set(i, exerciseDataModels.get(j));
                        exerciseDataModels.set(j, temp);
                    }
                }
            }
        }
        // Đưa danh sách bài tập vào Recycler View
        for (int i = 0, j = 0; i < exerciseDataModels.size(); i++) {

            // Nếu đang tìm kiếm
            if (isExerciseSearch) {
                if (editExerciseSearch.getText().toString().equals("")) break;
                if (exerciseDataModels.get(i).Name.toLowerCase()
                        .contains(editExerciseSearch.getText().toString().toLowerCase())
                        || exerciseDataModels.get(i).Id.equals(editExerciseSearch.getText().toString())) {
                }
                else continue;
            }

            // Lọc môn
            if (!exerciseDataModels.get(i).Subject
                    .equals(spinnerExerciseSubjectFilter.getSelectedItem().toString())
                    && spinnerExerciseSubjectFilter.getSelectedItemPosition() != 0) continue;

            // Tạo ExerciseDetail tương ứng của từng ExerciseTitle
            List<ExerciseDetail> exerciseDetails = new ArrayList<>();
            exerciseDetails.add(new ExerciseDetail(j++, exerciseDataModels.get(i)));

            listExerciseTitle.add(new ExerciseTitle(exerciseDataModels.get(i).Name
                    , exerciseDetails, mssv
                    , exerciseDataModels.get(i).Progress
                    , GetCountDown(exerciseDataModels.get(i).Deadline, false)));
        }
        emptyExercise.setVisibility(listExerciseTitle.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        // Nếu vừa tìm kiếm xong
        if (isExerciseSearch){
            editExerciseSearch.setFocusableInTouchMode(false);
            editExerciseSearch.setFocusable(false);
            editExerciseSearch.setFocusableInTouchMode(true);
            editExerciseSearch.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) editExerciseSearch.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editExerciseSearch.getWindowToken(), 0);
            isExerciseSearch = false;
        }

        // Đưa vào Recycler View
        adapterExerciseRecycler = new ExerciseCustomRecyclerViewAdapter(listExerciseTitle);
        exerciseRecyclerView.setAdapter(adapterExerciseRecycler);
    }
    // endregion

    public static String GetCountDown(String deadline, boolean onlySeccond)
    {
        // Chuyển đổi Deadline thành countdown
        SimpleDateFormat format = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        Date now = new Date();
        System.out.println(format.format(now));

        Date deadday = new Date();
        try {
            deadday = format.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long duration  = deadday.getTime() - now.getTime();
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        if (onlySeccond) {
            return String.valueOf(diffInSeconds);
        }
        if (duration < 0) {
            return "Đã quá hạn";
        }
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

        if (diffInDays == 0) {
            if (diffInHours == 0) {
                if (diffInMinutes == 0) {
                    return diffInSeconds + " giây";
                }
                else {
                    return diffInMinutes + " phút";
                }
            }
            else {
                return diffInHours + " giờ" + (diffInMinutes % 60 > 0 ? " " + diffInMinutes % 60 + " phút" : "");
            }
        }
        else {
            return diffInDays + " ngày" + (diffInHours % 24 > 0 ? " " + diffInHours % 24 + " giờ" : "");
        }
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