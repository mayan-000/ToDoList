package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> Items = new ArrayList<>();
    Button addItem;
    EditText writeItem;
    ListView itemList;
    ArrayAdapter<String> adapter;
    SharedPreferences sharedPreferences;

    private final AdapterView.OnItemClickListener ItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Delete")
                    .setMessage("Do You Want To Delete Item")
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Items.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }).show().create();
        }
    };

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String item = writeItem.getText().toString();

            if(item.length()!=0){
                Items.add(item);
                writeItem.setText("");
                adapter.notifyDataSetChanged();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        setContentView(R.layout.activity_main);

        addItem = findViewById(R.id.addItem);
        writeItem = findViewById(R.id.writeItem);
        itemList = findViewById(R.id.itemList);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, Items);
        itemList.setAdapter(adapter);

        addItem.setOnClickListener(listener);

        itemList.setOnItemClickListener(ItemClick);
    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }

    @Override
    protected void onResume() {
        retrieveData();
        super.onResume();
    }

    private void saveData(){
        sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ListSize", Items.size());

        for (int i=0; i<Items.size(); i++){
            editor.putString("Item "+i, Items.get(i));
        }

        editor.commit();
    }

    private void retrieveData(){
        sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);

        int listSize = sharedPreferences.getInt("ListSize",0);

        for (int i=0; i<listSize; i++){
            String s = sharedPreferences.getString("Item "+i,"");
            Items.add(s);
            adapter.notifyDataSetChanged();
        }
    }
}