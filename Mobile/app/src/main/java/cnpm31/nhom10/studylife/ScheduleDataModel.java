package cnpm31.nhom10.studylife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScheduleDataModel {
    String Id;
    String DayInTheWeek;
    String Room;
    String Period;
    String TimeStart;
    String TimeFinish;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDayInTheWeek() {
        return DayInTheWeek;
    }

    public void setDayInTheWeek(String dayInTheWeek) {
        DayInTheWeek = dayInTheWeek;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getTimeFinish() {
        return TimeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        TimeFinish = timeFinish;
    }

    public ScheduleDataModel(String id, String dayInTheWeek, String room, String period, String timeStart, String timeFinish) {
        Id = id;
        DayInTheWeek = dayInTheWeek;
        Room = room;
        Period = period;
        TimeStart = timeStart;
        TimeFinish = timeFinish;
    }
}
