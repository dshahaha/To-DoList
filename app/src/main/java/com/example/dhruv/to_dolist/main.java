package com.example.dhruv.to_dolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ToDoAdapter(this, tasks);
        listView.setAdapter(adapter);
        findViewById(R.id.button).setOnClickListener(new OnPressListener());
        listView.setOnItemLongClickListener(new OnItemClickListener());
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        for(int i = 0; i<preferences.getAll().size();i++){
            if(preferences.getString(i+"",null) != null)
                tasks.add(preferences.getString(i+"",null));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.clear();
        for(int i = 0;i<tasks.size();i++)
            editor.putString("" + i, tasks.get(i));
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.clear();
        for(int i = 0;i<tasks.size();i++)
            editor.putString("" + i, tasks.get(i));
        editor.apply();
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
        return (id == R.id.action_settings||super.onOptionsItemSelected(item));
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
            AlertDialog.Builder deleteBox =new AlertDialog.Builder(main.this);
            deleteBox.setTitle(tasks.get(position));
            deleteBox.setMessage("Delete this task?");
            deleteBox.setNegativeButton("No", null);
            deleteBox.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    tasks.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            });
            deleteBox.show();
            return true;
        }
    }


    public class ToDoDialog extends AlertDialog.Builder {
        public ToDoDialog(Context context) {
            super(context);
            final EditText toDoEditText = new EditText(main.this);
            AlertDialog.Builder newTaskBox = new AlertDialog.Builder(main.this);
            newTaskBox.setTitle("New Task");
            toDoEditText.setHint("\nEnter Task");
            toDoEditText.setMinimumHeight(200);
            newTaskBox.setView(toDoEditText);

            newTaskBox.setPositiveButton("Create", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (toDoEditText.getText().toString().length() != 0)
                        tasks.add(toDoEditText.getText().toString());
                    adapter.notifyDataSetChanged();
                }
            });
            newTaskBox.show();
        }
    }

}
