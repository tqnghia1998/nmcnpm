package cnpm31.nhom10.studylife;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import cnpm31.nhom10.studylife.DTOModel.SubjectRegisteredDTO;

import static cnpm31.nhom10.studylife.MainActivity.urlRegistered;

public class ViewScheduleFragment extends android.app.Fragment {

    //Danh sách các môn học đã đăng kí với thông tin chi tiết
    List<SubjectRegisteredDTO> listSubReg;

    public ViewScheduleFragment() {}

    //TextView
    private TextView textViewDate; //Hiển thị ngày/tháng/năm hiện tại
    private TextView textViewMon;
    private TextView textViewTue;
    private TextView textViewWed;
    private TextView textViewThu;
    private TextView textViewFri;
    private TextView textViewSat;
    private TextView textViewSun;

    //Image Button
    private ImageButton btnPrevWeek;
    private ImageButton  btnNextWeek;

    //Views
    private RelativeLayout relativeLayoutMonDay;
    private RelativeLayout relativeLayoutTueDay;
    private RelativeLayout relativeLayoutWedDay;
    private RelativeLayout relativeLayoutThuDay;
    private RelativeLayout relativeLayoutFriDay;
    private RelativeLayout relativeLayoutSatDay;
    private RelativeLayout relativeLayoutSunday;

    //Mảng lưu các ngày trong tuần
    public String[] weekDays;
    //Ngày bắt đầu tuần
    public String firstDayOfWeek;
    //Ngày kết thúc tuần
    public String lastDayOfWeek;

    //Tuần hiện tại
    public int weekDaysCount = 0;
    //Ngày hiện tại
    public int day = 0;

    //Thông tin cần thiết môn học để hiển thị
    public ArrayList<SubjectData> weekDatas;

    //Button
    Button btnSunday;
    Button btnMonday;
    Button btnTuesday;
    Button btnWedday;
    Button btnThuday;
    Button btnFriday;
    Button btnSatday;

    public  ArrayList<Button> listButton = new ArrayList<Button>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_viewschedule, null);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                // Lấy thông tin chi tiết các môn học đã đăng kí
                JSONArray arrayJson = JsonHandler.getJsonFromUrl(urlRegistered
                        + "/1612422");         // MSSV

                // Chuyển thành danh sách các DTOSubjectRegistered
                listSubReg = SubjectRegisteredDTO.fromJson(arrayJson);

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Ánh xạ các text view
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewSun = (TextView) view.findViewById(R.id.textViewSun);
        textViewMon = (TextView) view.findViewById(R.id.textViewMon);
        textViewTue = (TextView) view.findViewById(R.id.textViewTue);
        textViewWed = (TextView) view.findViewById(R.id.textViewWed);
        textViewThu = (TextView) view.findViewById(R.id.textViewThu);
        textViewFri = (TextView) view.findViewById(R.id.textViewFri);
        textViewSat = (TextView) view.findViewById(R.id.textViewSat);

        //Ánh xạ các relativeLayout
        relativeLayoutMonDay = (RelativeLayout) view.findViewById(R.id.relativeLayoutMonDay);
        relativeLayoutTueDay = (RelativeLayout) view.findViewById(R.id.relativeLayoutTueDay);
        relativeLayoutWedDay = (RelativeLayout) view.findViewById(R.id.relativeLayoutWedDay);
        relativeLayoutThuDay = (RelativeLayout) view.findViewById(R.id.relativeLayoutThuDay);
        relativeLayoutFriDay = (RelativeLayout) view.findViewById(R.id.relativeLayoutFriDay);
        relativeLayoutSatDay = (RelativeLayout) view.findViewById(R.id.relativeLayoutSatDay);
        relativeLayoutSunday = (RelativeLayout) view.findViewById(R.id.relativeLayoutSunday);

        //Lấy chính xác các ngày trong tuần lưu thành mảng các chuỗi
        weekDays = getDayOfWeek();

        //Lấy ngày hiện tại để Set background đỏ
        Calendar now = Calendar.getInstance(Locale.GERMANY);
        day = now.get(Calendar.DAY_OF_WEEK);

        //Hiển thị ngày và môn học
        loadDayAndSubject(weekDays);

        //Sụ kiện Previou Week
        btnPrevWeek = view.findViewById(R.id.btnPrevWeek);
        btnPrevWeek.setOnClickListener(v -> {

            for (int j = 0; j < listButton.size(); j++) {
                //Lấy id của button
                int buttonID = listButton.get(j).getId();
                String day = listSubReg.get(buttonID).DayInTheWeek;

                switch (day)
                {
                    case "Monday":
                        relativeLayoutMonDay.removeView(listButton.get(j));
                        break;
                    case "Tuesday":
                        relativeLayoutTueDay.removeView(listButton.get(j));
                        break;
                    case "Wednesday":
                        relativeLayoutWedDay.removeView(listButton.get(j));
                        break;
                    case "Thursday":
                        relativeLayoutThuDay.removeView(listButton.get(j));
                        break;
                    case "Friday":
                        relativeLayoutFriDay.removeView(listButton.get(j));
                        break;
                    case "Saturday":
                        relativeLayoutSatDay.removeView(listButton.get(j));
                        break;
                    case "Sunday":
                        relativeLayoutSunday.removeView(listButton.get(j));
                        break;

                    default:
                        break;

                }
            }

            //Lấy các ngày tuần trước đó
            weekDays = getWeekDayPrev();
            //Hiển thị ngày và môn học trong tuần đó
            loadDayAndSubject(weekDays);
        });

        //Sụ kiện Next Week
        btnNextWeek = view.findViewById(R.id.btnNextWeek);
        btnNextWeek.setOnClickListener(v -> {

            for (int j = 0; j < listButton.size(); j++) {
                //Lấy id của button
                int buttonID = listButton.get(j).getId();
                String day = listSubReg.get(buttonID).DayInTheWeek;

                switch (day)
                {
                    case "Monday":
                        relativeLayoutMonDay.removeView(listButton.get(j));
                        break;
                    case "Tuesday":
                        relativeLayoutTueDay.removeView(listButton.get(j));
                        break;
                    case "Wednesday":
                        relativeLayoutWedDay.removeView(listButton.get(j));
                        break;
                    case "Thursday":
                        relativeLayoutThuDay.removeView(listButton.get(j));
                        break;
                    case "Friday":
                        relativeLayoutFriDay.removeView(listButton.get(j));
                        break;
                    case "Saturday":
                        relativeLayoutSatDay.removeView(listButton.get(j));
                        break;
                    case "Sunday":
                        relativeLayoutSunday.removeView(listButton.get(j));
                        break;

                        default:
                            break;

                }
            }

            //Lấy các ngày tuần sau đó
            weekDays = getWeekDayNext();
            //Hiển thị ngày và môn học trong tuần đó
            loadDayAndSubject(weekDays);
        });

        return view;
    }

    public void loadDayAndSubject(String[] weekDays){

        //Lấy ra ngày đầu tuần
        firstDayOfWeek = convertWeekDays(weekDays[0]);
        //Lấy ngày kết thúc tuần
        lastDayOfWeek = convertWeekDays(weekDays[6]);
        //Hiển thị ngày bắt đầu và kết thúc tuần
        textViewDate.setText(firstDayOfWeek + " - " + lastDayOfWeek + " " + convertWeekDaysMonth(weekDays[6]));

        //Hiển thị thứ và ngày tương ứng lên textview
        textViewMon.setText(convertWeekDays(weekDays[0]) + "\nMon");
        textViewTue.setText(convertWeekDays(weekDays[1]) + "\nTue");
        textViewWed.setText(convertWeekDays(weekDays[2]) + "\nWeb");
        textViewThu.setText(convertWeekDays(weekDays[3]) + "\nThu");
        textViewFri.setText(convertWeekDays(weekDays[4]) + "\nFri");
        textViewSat.setText(convertWeekDays(weekDays[5]) + "\nSat");
        textViewSun.setText(convertWeekDays(weekDays[6]) + "\nSun");

        //Set background đỏ cho ngày hiện tại
        if (weekDaysCount == 0) {
            switch (day) {
                case 1:
                    textViewSun.setBackgroundColor(Color.parseColor("red"));
                    break;
                case 2:
                    textViewMon.setBackgroundColor(Color.parseColor("red"));
                    break;
                case 3:
                    textViewTue.setBackgroundColor(Color.parseColor("red"));
                    break;
                case 4:
                    textViewWed.setBackgroundColor(Color.parseColor("red"));
                    break;
                case 5:
                    textViewThu.setBackgroundColor(Color.parseColor("red"));
                    break;
                case 6:
                    textViewFri.setBackgroundColor(Color.parseColor("red"));
                    break;
                case 7:
                    textViewSat.setBackgroundColor(Color.parseColor("red"));
                    break;
            }
        }
        //Set lại background ngày hiện tại khi lần đầu nhấn next hoặc prev
        else if (weekDaysCount == -1 || weekDaysCount == 1){
            switch (day) {
                case 1:
                    textViewSun.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    break;
                case 2:
                    textViewMon.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    break;
                case 3:
                    textViewTue.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    break;
                case 4:
                    textViewWed.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    break;
                case 5:
                    textViewThu.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    break;
                case 6:
                    textViewFri.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    break;
                case 7:
                    textViewSat.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    break;
            }
        }

        //Hiển thị các môn học tương ứng thời gian
        try{

            new loadSubjectToView().execute("");

        } catch (Exception ex){

            Log.getStackTraceString(ex);
        }
    }

    //Hàm lấy ngày từ chuỗi yyyy/MM/dd
    public static String convertWeekDays(String date){

        String day = null;
        try {

            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date dateParse = yyyyMMdd.parse(date);

            SimpleDateFormat dd = new SimpleDateFormat("dd");
            day = dd.format(dateParse);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return day;
    }

    //Hàm lấy tháng năm từ chuỗi yyyy/MM/dd
    public static String convertWeekDaysMonth(String date) {

        String monthyear = null;
        try
        {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
            Date dateParse = yyyyMMdd.parse(date);

            SimpleDateFormat MMyyyy = new SimpleDateFormat("MMM yyyy");
            monthyear = MMyyyy.format(dateParse);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return monthyear;

    }

    //Hàm tạo một mảng các ngày trong tuần theo định dạng yyyy/MM/dd
    public String[] getDayOfWeek() {

        //Lấy thông tin ngày hiện tại
        Calendar now = GregorianCalendar.getInstance(Locale.GERMANY);
        now.setFirstDayOfWeek(Calendar.MONDAY);

        //Tạo định dạng thời gian muốn lưu
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        //Mảng lưu các ngày trong tuần
        String[] days = new String[7];

        //Tính khoảng cách ngày hiện tại so với ngày đầu tuần
        int gaps = -now.get(Calendar.DAY_OF_WEEK) + 2 ;

        //Nếu là chủ nhật (DAY_OF_WEEK = 1) gán lại gaps
        if(gaps == 1)
        {
            gaps = -6;
        }

        //Đặt lại thời gian là ngày đầu tuần
        now.add(Calendar.DAY_OF_MONTH, gaps );

        //Thêm lần lượt ngày bắt đầu đến ngày kết thúc tuần vào mảng chuỗi
        for (int i = 0; i < 7; i++) {
            days[i] = date.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH , 1);
        }

        return days;
    }

    //Lấy các ngày của tuần trước tuần hiện tại
    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayPrev() {

        weekDaysCount--;
        Calendar now = Calendar.getInstance();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];

        int gaps = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2;

        //Nếu là chủ nhật (DAY_OF_WEEK = 1) gán lại gaps
        if(gaps == 1)
        {
            gaps = -6;
        }

        //Giảm ngày hiện tại đi 1 tuần
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        //Giảm về ngày đầu tuần của tuần đó
        now.add(Calendar.DAY_OF_MONTH, gaps);

        for (int i = 0; i < 7; i++) {
            days[i] = formatDate.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    //Lấy các ngày của tuần kế tiếp tuần hiện tại
    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayNext() {

        weekDaysCount++;
        Calendar now = Calendar.getInstance();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];

        int gaps = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2;

        //Nếu là chủ nhật (DAY_OF_WEEK = 1) gán lại gaps
        if(gaps == 1)
        {
            gaps = -6;
        }

        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, gaps);

        for (int i = 0; i < 7; i++) {
            days[i] = formatDate.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    //Tạo một button với các tham số truyền vào
    public Button getButtonToLayout(int higth, int marginTop,
                                    String buttonText, int buttonID, String buttonColor) {

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, higth);
        Button button = new Button(getActivity());
        button.setLayoutParams(params);
        button.setBackgroundColor(Color.parseColor(buttonColor));
        button.setText(buttonText);
        button.setOnClickListener(buttonOnClick);
        button.setTextSize(10);
        button.setId(buttonID);
        params.setMargins(0, marginTop, 0, 0);

        return button;
    }

    //Sự kiện click vào một môn học
    public View.OnClickListener buttonOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            //Lấy id của button đã ckick
            Button b = (Button) v;
            int buttonId = b.getId();

            //Tạo mới một đối tượng full subject
            SubjectRegisteredDTO subjectReg = new SubjectRegisteredDTO();

            //Tìm môn học trong listSubject trùng id với button đã click
            for (int j = 0; j < weekDatas.size(); j++) {

                //Lấy id của subject
                int subjectID = weekDatas.get(j).subjectID;

                if (buttonId == subjectID ){
                    //Coppy subject với id subject tương ứng
                    subjectReg = listSubReg.get(subjectID);

                    //Gửi đối tượng qua fragment ViewScheduleDetailsFragment
                    ViewScheduleDetailsFragment viewScheduleDetailsFragment = new ViewScheduleDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("subjectDetails", subjectReg);
                    viewScheduleDetailsFragment.setArguments(bundle);

                    //Hiển thị Fragment ViewScheduleDetailsFragment
                    FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.registerFrame,viewScheduleDetailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    break;
                }
            }
        }
    };

    //Tạo một subjectData với các tham số có sẵn
    public static SubjectData setDataSubject(String day, int subjectId,
                                             String subjectTitle, int topMargin, int buttonHight) {
        SubjectData subjectData = new SubjectData();

        subjectData.day = day;
        subjectData.subjectID = subjectId;
        subjectData.subjectTitle = subjectTitle;
        subjectData.topMargin = topMargin;
        subjectData.buttonHight = buttonHight;

        return subjectData;
    }

    //Tính khoảng cách của button so với top dựa vào thời gian bắt đầu
    public int getTopMargin(String startTime) {

        int topMargin = 0;

        try {
            String[]hourMinutes = startTime.split(":");
            int hours = Integer.parseInt(hourMinutes[0]);
            int minutes = Integer.parseInt(hourMinutes[1]);

            topMargin = hours * 40  + minutes * 40 / 60;
        } catch (Exception e) {
            Log.getStackTraceString(e);
        }

        return topMargin*2;
    }

    //Lấy chiều cao button dựa vào thời gian bắt đầu và kết thúc
    public int getHightOfButton(String startTime, String endTime) {

        int hight = 0;

        try {
            SimpleDateFormat hours = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
            Date start = hours.parse(startTime);
            Date end = hours.parse(endTime);

            long timeDifference = end.getTime() - start.getTime();

            hight = (int) (timeDifference/60000*2/3); //1 phút = 2/3 dp

        } catch (Exception e) {
            Log.getStackTraceString(e);
        }

        return hight*2;
    }

    //Hiển thị môn học lên view
    public class loadSubjectToView extends AsyncTask<String, Void, String>  {

        @Override
        protected String doInBackground(String... params) {
            try {
                weekDatas = new ArrayList<SubjectData>();
                int topMargin;
                int buttonHight;

                //Ngày đầu tuần và cuối tuần
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date fisrtWeek = yyyyMMdd.parse(weekDays[0]);
                Date lastWeek = yyyyMMdd.parse(weekDays[6]);

                for (int i = 0; i < listSubReg.size(); i++) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                    Date startDay = dateFormat.parse(listSubReg.get(i).StartOfCourse);
                    Date finishDay = dateFormat.parse(listSubReg.get(i).FinishOfCourse);

                    int first_start = fisrtWeek.compareTo(startDay);
                    int last_start = lastWeek.compareTo(startDay);
                    int first_finish = fisrtWeek.compareTo(finishDay);
                    int last_finish = lastWeek.compareTo(finishDay);

                    if ((first_start <= 0 && last_start >= 0 ) || (first_finish <= 0 && last_finish >=0)
                            || (first_start >= 0 && last_finish <= 0)) {

                        topMargin = getTopMargin(listSubReg.get(i).TimeStart);
                        buttonHight = getHightOfButton(listSubReg.get(i).TimeStart, listSubReg.get(i).TimeFinish);

                        //Thêm các thông tin cần thiết một môn học từ list subject registered lấy về
                        weekDatas.add(setDataSubject(listSubReg.get(i).DayInTheWeek, i,
                                listSubReg.get(i).Subject + "\n\n" + listSubReg.get(i).Room, topMargin, buttonHight));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String str) {

            try {

                SubjectData subjectToDay;
                int length = weekDatas.size();

                if (length != 0) {

                    //Hiển thị từng môn học lên relativeLayout
                    for (int k = 0; k < length; k++) {

                        subjectToDay = weekDatas.get(k);

                        String day = subjectToDay.day;
                        switch (day) {

                            case "Sunday":

                                btnSunday = getButtonToLayout(
                                        subjectToDay.buttonHight,
                                        subjectToDay.topMargin,
                                        subjectToDay.subjectTitle,
                                        subjectToDay.subjectID,"#CCCCCC");
                                relativeLayoutSunday.addView(btnSunday);
                                listButton.add(btnSunday);
                                break;

                            case "Monday":

                                btnMonday = getButtonToLayout(
                                        subjectToDay.buttonHight,
                                        subjectToDay.topMargin,
                                        subjectToDay.subjectTitle,
                                        subjectToDay.subjectID,"#999966");
                                relativeLayoutMonDay.addView(btnMonday);
                                listButton.add(btnMonday);
                                break;

                            case "Tuesday":

                                btnTuesday = getButtonToLayout(
                                        subjectToDay.buttonHight,
                                        subjectToDay.topMargin,
                                        subjectToDay.subjectTitle,
                                        subjectToDay.subjectID,"#FFFF66");
                                relativeLayoutTueDay.addView(btnTuesday);
                                listButton.add(btnTuesday);
                                break;

                            case "Wednesday":
                                btnWedday = getButtonToLayout(
                                        subjectToDay.buttonHight,
                                        subjectToDay.topMargin,
                                        subjectToDay.subjectTitle,
                                        subjectToDay.subjectID,"#66CC66");
                                relativeLayoutWedDay.addView(btnWedday);
                                listButton.add(btnWedday);
                                break;

                            case "Thursday":
                                btnThuday = getButtonToLayout(
                                        subjectToDay.buttonHight,
                                        subjectToDay.topMargin,
                                        subjectToDay.subjectTitle,
                                        subjectToDay.subjectID,"#FF9966");
                                relativeLayoutThuDay.addView(btnThuday);
                                listButton.add(btnThuday);
                                break;

                            case "Friday":
                                btnFriday = getButtonToLayout(
                                        subjectToDay.buttonHight,
                                        subjectToDay.topMargin,
                                        subjectToDay.subjectTitle,
                                        subjectToDay.subjectID,"#CC66CC");
                                relativeLayoutFriDay.addView(btnFriday);
                                listButton.add(btnFriday);
                                break;

                            case "Saturday":
                                btnSatday = getButtonToLayout(
                                        subjectToDay.buttonHight,
                                        subjectToDay.topMargin,
                                        subjectToDay.subjectTitle,
                                        subjectToDay.subjectID,"#3366FF");
                                relativeLayoutSatDay.addView(btnSatday);
                                listButton.add(btnSatday);
                                break;

                            default:
                                break;
                        }

                    }

                }

            } catch (Exception e) {
                Log.getStackTraceString(e);
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            try {
                dialog = ProgressDialog.show(getActivity(), null, null,true, false);
                dialog.setContentView(R.layout.progress_layout);
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
    }
}


//Thông tin cần thiết của một môn học để xử lý hiển thị lên view
class SubjectData {
    public String day;
    public int subjectID;
    public String subjectTitle;
    public int topMargin;
    public int buttonHight;
}