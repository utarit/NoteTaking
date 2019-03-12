package com.example.notetakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
        startActivity(intent);

        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase database =  openOrCreateDatabase("NotesDatabase", MODE_PRIVATE, null);
        //database.execSQL("create table Notes (note varchar)");
        ListView listView = findViewById(R.id.listView);

        ArrayList<String> notes = new ArrayList<String>();

        Cursor c = database.rawQuery("select * from notes ", null);
        int index = c.getColumnIndex("note");


        if( c != null && c.moveToFirst() ){
            do{
                notes.add(c.getString(index));
            } while(c.moveToNext());
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

    }
}
