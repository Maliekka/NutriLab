package com.example.nutrilab;

public class Shift {
    public Shift(){

    }

    public Shift(String D, String Ts, String Te){
        this.Day = D;
        this.TimeStart = Ts;
        this.TimeEnd = Te;
    }

    private String Day;
    private String TimeStart;
    private String TimeEnd;

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getTimeEnd() {
        return TimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        TimeEnd = timeEnd;
    }

}
