package com.example.taskmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.DataSupportException;

import java.util.List;

import model.Task;
import model.TaskList;

public class MainActivity extends AppCompatActivity {

    private Button addlist;
    private EditText listname;
    private int finished = 0;
    private int unfinished = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.getDatabase();

        addlist = findViewById(R.id.addTaskList);
        addlist.setOnClickListener(new AddList());

        final List<TaskList> taskLists = LitePal.findAll(model.TaskList.class);
        if(taskLists.size() != 0){
//            String[] expRe = new String[taskLists.size()];
//            for(int i = 0; i < taskLists.size(); i ++){
//                expRe[i] = taskLists.get(i).getListName();
//            }

            TaskListAddapter adapter = new TaskListAddapter(MainActivity.this, R.layout.tasklist_item, taskLists);
            ListView listView = (ListView)findViewById(R.id.taskLists);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(MainActivity.this, TasksActivity.class);
                    intent.putExtra("listname", taskLists.get(position).getListName());
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    class AddList implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            // Get the layout inflater

            LayoutInflater layoutInflater = getLayoutInflater();
            final View layout = layoutInflater.inflate(R.layout.tasklist_dialog,
                    null);

            builder.setView(layout).setTitle("创建清单")
                    // Add action buttons
                .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listname = layout.findViewById(R.id.edit_listname);
                        if(listname.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this, "请输入清单名称", Toast.LENGTH_SHORT).show();

                        }else if(EqualListName(listname.getText().toString())){
                            Toast.makeText(MainActivity.this, "该清单已存在", Toast.LENGTH_SHORT).show();

                        } else {
                            TaskList newlist = new TaskList();
                            newlist.setListName(listname.getText().toString());
                            newlist.save();

                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
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

    private boolean EqualListName(String listName){
        List<TaskList> tasklists = LitePal.findAll(model.TaskList.class);
        for(int i = 0; i < tasklists.size(); i++){
            if(listName.equals(tasklists.get(i).getListName())){
                return true;
            }
        }
        return false;
    }
}
