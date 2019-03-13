package com.example.notetakingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    ArrayList<String> notes = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        Intent intent = new Intent(this, NoteTaking.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SECOND_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String returnString = data.getStringExtra(Intent.EXTRA_TEXT);
                notes.add(returnString);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SQLiteDatabase database =  openOrCreateDatabase("NotesDatabase", MODE_PRIVATE, null);
        //database.execSQL("create table Notes (note varchar)");
        ListView listView = findViewById(R.id.listView);


        Cursor c = database.rawQuery("select * from notes ", null);
        int index = c.getColumnIndex("note");


        if( c != null && c.moveToFirst() ){
            do{
                notes.add(c.getString(index));
            } while(c.moveToNext());
        }


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                final String note = textView.getText().toString();
                final int pos = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                database.execSQL("delete from notes where note = ?", new String[]{note});
                                notes.remove(pos);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });

    }
}
