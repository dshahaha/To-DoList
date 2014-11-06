package com.example.dhruv.to_dolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class main extends Activity {

    ArrayList<String> tasks = new ArrayList<String>();
    ListView listView;
    ToDoAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ToDoAdapter(this, tasks);
        listView.setAdapter(adapter);
        findViewById(R.id.button).setOnClickListener(new OnPressListener());
        listView.setOnItemLongClickListener(new OnItemClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class ToDoAdapter extends ArrayAdapter<String> {

        public ToDoAdapter(Context context, List<String> objects) {

            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.todoitem,null);
            }
            ((TextView) convertView.findViewById(R.id.listItemText)).setText(getItem(position));
            return convertView;
        }
    }

    public class OnPressListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            new ToDoDialog(main.this);
        }
    }

    public class OnItemClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l){
            AlertDialog.Builder adb=new AlertDialog.Builder(main.this);
            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete this task?");
            adb.setNegativeButton("No", null);
            adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    tasks.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_SHORT).show();
                }
            });
            adb.show();
            return true;
        }
        public void onNothingSelected(AdapterView<?> view){}
    }


    public class ToDoDialog extends AlertDialog.Builder {
        public ToDoDialog(Context context) {
            super(context);
            final EditText toDoEditText = new EditText(main.this);
            AlertDialog.Builder alert = new AlertDialog.Builder(main.this);
            alert.setTitle("New Task");
            toDoEditText.setHint("\nEnter Task");
            toDoEditText.setMinimumHeight(200);
            alert.setView(toDoEditText);

            alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(toDoEditText.getText().toString().length() != 0)
                        tasks.add(toDoEditText.getText().toString());
                    adapter.notifyDataSetChanged();
                }
            });
            alert.show();
        }
    }

}
