package com.example.taskmanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import model.Task;
import model.TaskList;

public class TaskListAddapter extends ArrayAdapter<TaskList> {

    private int resourceId;
    private static final String TAG = "TaskListAddapter";

    public TaskListAddapter(Context context, int textViewResourceId, List<TaskList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LitePal.getDatabase();

        TaskList taskList = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        TextView listName = view.findViewById(R.id.list_name);
        TextView complition = view.findViewById(R.id.complition);
        listName.setText(taskList.getListName());

        final List<Task> tasks = LitePal.findAll(model.Task.class);
        List<Task> useTasks = new ArrayList<Task>();
        for(int i=0; i < tasks.size(); i++){
            if(tasks.get(i).getListName().equals(taskList.getListName())){
                useTasks.add(tasks.get(i));
                //Log.d(TAG, "getView: "+i);
            }
        }

        taskList.setComplition(useTasks);

        //Log.d(TAG, "getViewUse: "+ useTasks.size());
        //Log.d(TAG, "getViewComplition: " + taskList.getComplition());
        double Complition = taskList.getComplition() * 100;
        DecimalFormat df=new DecimalFormat("0.0");
        complition.setText(df.format(Complition) + "%");
        return view;
    }


}
