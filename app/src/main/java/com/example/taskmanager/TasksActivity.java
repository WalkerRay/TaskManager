package com.example.taskmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Task;
import model.TaskList;

public class TasksActivity extends AppCompatActivity {

    private Button addTask;
    private EditText taskName;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private String ListName;
    private static final String TAG = "TasksActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        LitePal.getDatabase();

        addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new AddTask());
        Intent intent = getIntent();
        ListName = intent.getStringExtra("listname");

        final List<Task> tasks = LitePal.findAll(model.Task.class);
        final List<Task> useTasks = new ArrayList<Task>();

//        LitePal.deleteAll(model.Task.class);

        for(int i=0; i < tasks.size(); i++){
            if(tasks.get(i).getListName().equals(ListName)){
                useTasks.add(tasks.get(i));
            }
        }
        if(useTasks.size() != 0){

//            Log.d(TAG, "onCreate: "+ useTasks.size());
//            if(useTasks.get(1).getListName() == null){
//                Log.d(TAG, "onCreate: "+"nullListname");
//            }else{
//                Log.d(TAG, "onCreate: "+ useTasks.get(1).getListName());
//            }
//
//            if(useTasks.get(1).getTaskName() == null){
//                Log.d(TAG, "onCreate: "+"nullTaskname");
//            }else{
//                Log.d(TAG, "onCreate: "+ useTasks.get(1).getTaskName());
//            }
//
//
//            if(useTasks.get(1).getOvertime() == null){
//                Log.d(TAG, "onCreate: "+"nullOvertime");
//            }else{
//                Log.d(TAG, "onCreateOver: "+ useTasks.get(1).getOvertime().get(Calendar.YEAR));
//            }
//
//            if(useTasks.get(1).getStartTime() == null){
//                Log.d(TAG, "onCreate: "+"nullStarttime");
//            }else{
//                Log.d(TAG, "onCreateStart: "+ useTasks.get(1).getStartTime().get(Calendar.YEAR));
//            }


            final TaskAddapter adapter = new TaskAddapter(TasksActivity.this, R.layout.task_item, useTasks);
            ListView listView = (ListView)findViewById(R.id.tasks);
            listView.setAdapter(adapter);



//            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                    final Task task = useTasks.get(position);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(TasksActivity.this);
//                    builder.setTitle(task.getTaskName());
//                    builder.setMessage("起始时间："+ task.getStartTime());
//                    builder.setMessage("结束时间："+ task.getOvertime());
//                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            LitePal.deleteAll(model.Task.class, "taskName = ?", task.getTaskName());
//                        }
//                    }).show();
//                    return false;
//                }
//            });
        }

    }

    class AddTask implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TasksActivity.this);

            // Get the layout inflater

            LayoutInflater layoutInflater = getLayoutInflater();
            final View layout = layoutInflater.inflate(R.layout.task_dialog, null);
            datePicker = layout.findViewById(R.id.datepicker);
            timePicker = layout.findViewById(R.id.timepicker);
            boolean hourView = true;
            timePicker.setIs24HourView(hourView);


            builder.setView(layout).setTitle("添加任务")
                    // Add action buttons
                    .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            taskName = layout.findViewById(R.id.edit_taskname);
                            datePicker = layout.findViewById(R.id.datepicker);
                            timePicker = layout.findViewById(R.id.timepicker);

                            if(taskName.getText().toString().isEmpty()){
                                Toast.makeText(TasksActivity.this, "请输入任务名称", Toast.LENGTH_SHORT).show();

                            }else if(EqualTaskName(taskName.getText().toString())){
                                Toast.makeText(TasksActivity.this, "该任务已存在", Toast.LENGTH_SHORT).show();


                            }else {

                                Task task = new Task();
                                task.setTaskName(taskName.getText().toString());
                                task.setListName(ListName);
                                Calendar calendar = Calendar.getInstance();

                                int year = datePicker.getYear();
                                int month = datePicker.getMonth();
                                int dayOfMonth = datePicker.getDayOfMonth();
                                int hourOfDay = timePicker.getHour();
                                int minute = timePicker.getMinute();
                                Date date = new Date(year, month, dayOfMonth, hourOfDay, minute);

                                calendar.setTime(date);
                                task.setOvertime(calendar);
                                task.save();

                                Intent intent = new Intent(TasksActivity.this, TasksActivity.class);
                                intent.putExtra("listname", ListName);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) { }
                    });
            builder.show();
        }
    }

    private boolean EqualTaskName(String taskName){
        List<Task> tasks = LitePal.findAll(model.Task.class);
        for(int i = 0; i < tasks.size(); i++){
            if(taskName.equals(tasks.get(i).getTaskName())){
                return true;
            }
        }
        return false;
    }
}
