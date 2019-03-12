package com.example.notetakingapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteTaking extends AppCompatActivity {
    Button button;
    EditText noteText;
    SQLiteDatabase database;

    public void buttonClick(View view){
        String text = noteText.getText().toString();
        ContentValues cont = new ContentValues();
        cont.put("note", text);

        database.insert("Notes",null, cont);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taking);

        database =  openOrCreateDatabase("NotesDatabase", MODE_PRIVATE, null);

         noteText = findViewById(R.id.editText);
         button = findViewById(R.id.button);


    }
}
