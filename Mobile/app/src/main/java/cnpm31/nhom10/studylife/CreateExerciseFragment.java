package cnpm31.nhom10.studylife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cnpm31.nhom10.studylife.DTOModel.SubjectDTO;
import cnpm31.nhom10.studylife.DbModel.ExerciseDataModel;

import static cnpm31.nhom10.studylife.ExerciseFragment.refreshExercise;
import static cnpm31.nhom10.studylife.MainActivity.urlExercise;
import static cnpm31.nhom10.studylife.MainActivity.urlMajor;

public class CreateExerciseFragment extends android.app.Fragment {

    ImageButton btnBackFromCreateExercise;
    TextView txtExerciseTitle;
    EditText editExerciseName;
    EditText chosenDate;
    EditText chosenHour;
    Button btnChooseDate;
    Button btnChooseHour;
    EditText editExerciseContent;
    Button btnCreateExercise;
    SeekBar exerciseCreateSeekBar;
    TextView txtExerciseCreateDetailProgress;

    Spinner exerciseSpinnerCourse;
    ArrayAdapter adapterExerciseSpinner;

    boolean isSameDayDeadline = false;

    boolean isEdit = false;
    String idExercise = null;

    public CreateExerciseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_exercise_create, null);

        // Tạo danh sách các môn học đã đăng ký về
        List<String> listSubjectName = new ArrayList<>();
        listSubjectName.add("Không");
        Thread thread = new Thread(() -> {
            JSONArray arrayJson = JsonHandler.getJsonFromUrl(urlMajor
                    + "/%25"           // Tất cả các khoa
                    + "/%25/%25"       // Học kỳ và năm học
                    + "/1612422"       // MSSV
                    + "/0");           // Trạng thái Active (đang học)
            List<SubjectDTO> listDTOSubject = SubjectDTO.fromJson(arrayJson);
            for (int i = 0; i < listDTOSubject.size(); i++) {
                if (listDTOSubject.get(i).isRegisterd) {
                    listSubjectName.add(listDTOSubject.get(i).Subject);
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Đưa tên các môn học vào spinner chọn môn
        exerciseSpinnerCourse = view.findViewById(R.id.exerciseSpinnerCourse);
        adapterExerciseSpinner = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_spinner_item, listSubjectName);
        adapterExerciseSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        exerciseSpinnerCourse.setAdapter(adapterExerciseSpinner);

        // Ánh xạ control
        txtExerciseTitle = view.findViewById(R.id.txtExerciseTitle);
        editExerciseName = view.findViewById(R.id.editExerciseName);
        chosenDate = view.findViewById(R.id.chosenDate);
        chosenHour = view.findViewById(R.id.chosenHour);
        editExerciseContent = view.findViewById(R.id.editExerciseContent);
        btnCreateExercise = view.findViewById(R.id.btnCreateExercise);

        // Sự kiện quay về
        btnBackFromCreateExercise = view.findViewById(R.id.btnBackFromCreateExercise);
        btnBackFromCreateExercise.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().remove(CreateExerciseFragment.this).commit();
            refreshExercise();
        });

        // Sự kiện chọn ngày
        btnChooseDate = view.findViewById(R.id.btnChooseDate);
        btnChooseDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity()
                    , (_view, _year, _month, _day) -> {
                boolean isValid = true;
                if (_year < year) {
                    isValid = false;
                }
                else if (_year == year) {
                    if (_month < month) {
                        isValid = false;
                    }
                    else if (_month == month) {
                        if (_day < day) {
                            isValid = false;
                        }
                        else if (_day == day) {
                            isSameDayDeadline = true;
                        }
                    }
                }
                if (isValid) {
                    chosenDate.setText(++_month + "/" + _day + "/" + _year);
                    btnChooseHour.setEnabled(true);
                }
                else {
                    final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                    b.setTitle("Thông báo");
                    b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                    b.setMessage("Hạn chót phải là ngày trong tương lai").show();
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        // Sự kiện chọn giờ
        btnChooseHour = view.findViewById(R.id.btnChooseHour);
        btnChooseHour.setOnClickListener(v -> {

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    (view1, hourOfDay, minute) -> {
                boolean isValid = true;

                // Kiểm tra phải deadline là ngày hiện tại không
                if (isSameDayDeadline) {
                    if (hourOfDay < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                        isValid = false;
                    }
                    else if (hourOfDay == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                        if (minute <= Calendar.getInstance().get(Calendar.MINUTE)) {
                            isValid = false;
                        }
                    }
                }
                if (isValid) {
                    chosenHour.setText(hourOfDay + ":" + minute);
                }
                else {
                    final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                    b.setTitle("Thông báo");
                    b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                    b.setMessage("Hạn chót đã qua").show();
                }
            }, 0, 0, true);

            timePickerDialog.show();

        });

        // Sự kiện chọn tiến độ
        txtExerciseCreateDetailProgress = view.findViewById(R.id.txtExerciseCreateDetailProgress);
        exerciseCreateSeekBar = view.findViewById(R.id.exerciseCreateSeekBar);
        exerciseCreateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtExerciseCreateDetailProgress.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Sự kiện tạo bài tập
        btnCreateExercise.setOnClickListener(v -> {

            // Kiểm tra dữ liệu người dùng nhập
            if (editExerciseName.length() == 0) {
                final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                b.setTitle("Thông báo");
                b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                b.setMessage("Tên môn học không thể trống").show();
                editExerciseName.requestFocus();
                return;
            }
            if (chosenDate.length() == 0 || chosenHour.length() == 0) {
                final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                b.setTitle("Thông báo");
                b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                b.setMessage("Hạn chót bài tập không thể trống").show();
                return;
            }

            // Tạo hộp thoại xác nhận
            final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
            b.setTitle("Xác nhận");
            b.setMessage((isEdit ? "Sửa" : "Tạo") + " bài tập này?");

            // Nếu cancel
            b.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

            // Nếu Ok
            b.setPositiveButton("Ok", (dialog, id) -> {

                // Trường hợp sửa bài tập
                if (isEdit) {
                    JsonHandler.updateExercise(urlExercise + "/put", new ExerciseDataModel(
                            "1612422",
                            idExercise,
                            editExerciseName.getText().toString(),
                            exerciseSpinnerCourse.getSelectedItem().toString(),
                            chosenHour.getText().toString() + " " + chosenDate.getText().toString(),
                            String.valueOf(exerciseCreateSeekBar.getProgress()),
                            editExerciseContent.getText().toString()
                    ), getActivity());
                }
                // Trường hợp tạo bài tập
                else {
                    JsonHandler.postExercise(urlExercise, new ExerciseDataModel(
                            "1612422",
                            "auto",
                            editExerciseName.getText().toString(),
                            exerciseSpinnerCourse.getSelectedItem().toString(),
                            chosenHour.getText().toString() + " " + chosenDate.getText().toString(),
                            String.valueOf(exerciseCreateSeekBar.getProgress()),
                            editExerciseContent.getText().toString()
                    ), getActivity());
                }
            }).show();
        });

        // Kiểm tra có thông tin truyền vào không, nếu có thì đây đang là sửa bài tập
        if (getArguments() != null) {

            // Lấy các thông tin
            idExercise = getArguments().getString("id");
            String deadline = getArguments().getString("deadline");
            String progress = getArguments().getString("progress");
            String subject = getArguments().getString("subject");

            // Hiển thị lên giao diện
            editExerciseName.setText(getArguments().getString("name"));
            editExerciseContent.setText(getArguments().getString("content"));
            chosenHour.setText(deadline.substring(0, deadline.indexOf(' ')));
            chosenDate.setText(deadline.substring(deadline.indexOf(' ')));
            exerciseCreateSeekBar.setProgress(Integer.parseInt(progress));
            txtExerciseCreateDetailProgress.setText(progress + "%");
            for (int i = 0; i < exerciseSpinnerCourse.getCount(); i++) {
                if (exerciseSpinnerCourse.getItemAtPosition(i).toString().equals(subject)) {
                    exerciseSpinnerCourse.setSelection(i);
                    break;
                }
            }

            isEdit = true;
            txtExerciseTitle.setText("SỬA BÀI TẬP");
            btnCreateExercise.setText("Sửa");
        }

        // Xử lý ẩn keyboard
        view.findViewById(R.id.layoutExerciseFragment).setOnClickListener(v -> {
            editExerciseContent.clearFocus();
        });
        editExerciseContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideSoftKeyboard(getActivity(), view);
            }
        });
        editExerciseName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideSoftKeyboard(getActivity(), view);
            }
        });
        return view;
    }
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}