package com.example.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Task;
import model.TaskList;
import com.example.taskmanager.TasksActivity;

public class TaskAddapter extends ArrayAdapter<Task> {
    private int resourceId;
    private static final String TAG = "TaskAddapter";

    public TaskAddapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Task task = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        TextView taskName = view.findViewById(R.id.task_name);
        final CheckBox finished = view.findViewById(R.id.finished);
        TextView overTime = view.findViewById(R.id.overTime);
        taskName.setText(task.getTaskName());
        finished.setChecked(task.isFinished());
        overTime.setText("截止日期："+task.getOvertime());
        finished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton ButtonView, boolean isChecked) {
                //Log.d(TAG, "onCheckedChanged: "+isChecked);
                task.setStatus(isChecked);
                //Log.d(TAG, "onCheckedChangedTask: "+task.isFinished());
                task.save();
            }
        });

        taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(task.getTaskName());
                builder.setMessage("起始时间："+ task.getStartTime());
                builder.setMessage("\n结束时间："+ task.getOvertime());
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LitePal.deleteAll(model.Task.class, "taskName = ?", task.getTaskName());
                        Intent intent = new Intent(getContext(), TasksActivity.class);
                        intent.putExtra("listname", task.getListName());
                        getContext().startActivity(intent);
                        ((Activity)getContext()).finish();
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        //Log.d(TAG, "getViewdeadline: " + deadline(task));
        if(deadline(task) == 1){
            taskName.setTextColor(0xFFFFFF00);
            Log.d(TAG, "getView: deadline");
        }else if(deadline(task) == 2){
            taskName.setTextColor(0xFFFF0000);
        }
        return view;
    }

    private int deadline(Task task){
        String overTime = task.getOvertime();
        String[] overDate = overTime.split(" ");
        String[] a = overDate[0].split("/");
        String[] b = overDate[1].split(":");
        int year = Integer.valueOf(a[0]);
        int month = Integer.valueOf(a[1]);
        int dayOfMonth = Integer.valueOf(a[2]);
        int hour = Integer.valueOf(b[0]);
        int minute = Integer.valueOf(b[1]);
//        Log.d(TAG, "deadlineYear: "+task.getTaskName()+" "+year);
//        Log.d(TAG, "deadlineMonth: "+task.getTaskName()+" "+month);
//        Log.d(TAG, "deadlineDay: "+task.getTaskName()+" "+dayOfMonth);

        Calendar over = Calendar.getInstance();
        over.set(year, month-1, dayOfMonth);
        Calendar rightNow = Calendar.getInstance();
//        Log.d(TAG, "deadlineRightNow: "+task.getTaskName()+" "+rightNow.get(Calendar.YEAR)+" "+(rightNow.get(Calendar.MONTH)+1)+" "+rightNow.get(Calendar.DAY_OF_MONTH));
//        Log.d(TAG, "deadlineOver: "+task.getTaskName()+" "+(over.get(Calendar.YEAR)-1900)+" "+over.get(Calendar.MONTH)+" "+over.get(Calendar.DAY_OF_MONTH));

        long time1 = rightNow.getTimeInMillis();
        long time2 = over.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        Log.d(TAG, "deadlineDays: "+between_days);
        if((between_days <= 2 && between_days>0) || (between_days==0 && hour > rightNow.get(Calendar.HOUR_OF_DAY)) ||
                (between_days==0 && hour == rightNow.get(Calendar.HOUR_OF_DAY) && minute > rightNow.get(Calendar.MINUTE))){

            return 1;
        }else if(between_days > 2){
            return 0;
        }else{
            return 2;
        }
    }
}
