package com.chetan.wt;

public class TutorNotification {
    String course_name;
    String student_name;
    String date;

    public TutorNotification()
    {

    }

    public TutorNotification(String course_name, String student_name, String date) {
        this.course_name = course_name;
        this.student_name = student_name;
        this.date = date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
