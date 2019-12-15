package model;

import android.util.Log;

import org.litepal.crud.LitePalSupport;

import java.util.Calendar;
import java.util.Date;

public class Task extends LitePalSupport {
    private String taskName;
    private String listName;
    private boolean finish = false;
    private String startTime;
    private String overtime;
    private static final String TAG = "Task";

//    public void a(){
//        System.out.println("Current time is" +startTime.getTime());
//        Date date = new Date(2017, 6, 20, 17, 20);
//        startTime.setTime(date);
//        System.out.println("After setting Time:  " + startTime.get(Calendar.MINUTE) );
//
//
//    }
//
//    public static void main(String[] args){
//        Task a = new Task();
//        a.a();
//    }
    public Task(){
        Calendar calendar = Calendar.getInstance();
        this.setStartTime(calendar);
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public boolean isFinished() {
        return finish;
    }

    public void setStatus(boolean status){
        finish = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH)+1;

        Log.d(TAG, "setStartTime: "+calendar.get(Calendar.YEAR)+"/"+month+"/"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));

        this.startTime = calendar.get(Calendar.YEAR)+"/"+month+"/"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(Calendar calendar) {
        Log.d(TAG, "setOvertime: "+calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR) - 1900;
        this.overtime = year+"/"+month+"/"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
    }
}
