package cnpm31.nhom10.studylife;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import cnpm31.nhom10.studylife.DTOModel.SubjectRegisteredDTO;

public class ViewScheduleDetailsFragment extends android.app.Fragment {

    //Thông tin chi tiết một môn học
    SubjectRegisteredDTO subjectDetails;
    //Button back
    ImageButton btnBackFromViewDetails;

    private TextView textViewSubject;
    private TextView textViewId;
    private TextView textViewMajor;
    private TextView textViewTeacher;
    private TextView textViewDayStart;
    private TextView textViewDayFinish;
    private TextView textViewCredit;
    private TextView textViewRoom;
    private TextView textViewTimeStart;
    private TextView textViewTimeFinish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_viewschedule_details, null);

        // Sự kiện thoát khỏi fragment view detail
        btnBackFromViewDetails = view.findViewById(R.id.btnBackFromSubjectDetail);
        btnBackFromViewDetails.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().remove(ViewScheduleDetailsFragment.this).commit();
            getFragmentManager().popBackStack();
        });

        //Ánh xạ các text view
        textViewSubject = (TextView)view.findViewById(R.id.textSubject);
        textViewId = (TextView)view.findViewById(R.id.textID);
        textViewMajor = (TextView)view.findViewById(R.id.textMajor);
        textViewTeacher = (TextView)view.findViewById(R.id.textTeacher);
        textViewDayStart = (TextView)view.findViewById(R.id.textDayStart);
        textViewDayFinish = (TextView)view.findViewById(R.id.textDayFinish);
        textViewCredit = (TextView)view.findViewById(R.id.textCredit);
        textViewRoom = (TextView)view.findViewById(R.id.textRoom);
        textViewTimeStart = (TextView)view.findViewById(R.id.textTimeStart);
        textViewTimeFinish = (TextView)view.findViewById(R.id.textTimeFinish);

        //Lấy đối tượng subjectDetails truyền từ fragment viewschedule
        Bundle bundle = getArguments();
        subjectDetails = (SubjectRegisteredDTO)bundle.getSerializable("subjectDetails");

        //Hiển thị thông tin chi tiết môn học lên các text view
        textViewSubject.setText(subjectDetails.Subject);
        textViewId.setText("   " + subjectDetails.Id);
        textViewMajor.setText("   " +subjectDetails.Major);
        textViewTeacher.setText("   " +subjectDetails.Teacher);
        textViewDayStart.setText("   " +subjectDetails.StartOfCourse);
        textViewDayFinish.setText("   " +subjectDetails.FinishOfCourse);
        textViewCredit.setText("   " +String.valueOf(subjectDetails.Credit));
        textViewRoom.setText("   " +subjectDetails.Room);
        textViewTimeStart.setText("   " +subjectDetails.TimeStart);
        textViewTimeFinish.setText("   " +subjectDetails.TimeFinish);

        return view;
    }
}
