package model;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskList extends LitePalSupport {
    private String listName;
    private double complition = 0;//任务列表中的任务完成率
    private Calendar startTime = Calendar.getInstance();//清单创建时间
    private List<Task> tasks = new ArrayList<Task>();
    private static final String TAG = "TaskList";

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public double getComplition() {
        return complition;
    }

    public void setComplition(List<Task> useTasks) {

        double finished = 0;
        double unfinished = 0;
        //Log.d(TAG, "setComplitionUse: "+useTasks.size());
        if(useTasks.size() != 0){
            for(int i = 0; i < useTasks.size(); i++){
                if(useTasks.get(i).isFinished()){
                    finished += 1;
                }else{
                    unfinished += 1;
                }
                //Log.d(TAG, "setComplitionFin: "+ finished);
            }
            this.complition = finished/(double)useTasks.size();

        }

    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Date date) {
        this.startTime.setTime(date);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


}
