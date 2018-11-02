package cnpm31.nhom10.studylife;

public class SubjectDataModel {
    String Id;
    String Major;
    String Subject;
    int Credit;
    String Teacher;
    String TimeStart;
    String TimeFinish;
    byte Status;

    public SubjectDataModel() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public int getCredit() {
        return Credit;
    }

    public void setCredit(int credit) {
        Credit = credit;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
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

    public byte getStatus() {
        return Status;
    }

    public void setStatus(byte status) {
        Status = status;
    }

    public SubjectDataModel(String id, String major, String subject, int credit, String teacher, String timeStart, String timeFinish, byte status) {
        Id = id;
        Major = major;
        Subject = subject;
        Credit = credit;
        Teacher = teacher;
        TimeStart = timeStart;
        TimeFinish = timeFinish;
        Status = status;
    }
}
