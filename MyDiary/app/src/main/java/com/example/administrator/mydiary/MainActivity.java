package com.example.administrator.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyDBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    MyCursorAdapter myAdapter;
    ListView list;
    long current_id = -1;
    int LOGIN_SUCCESS = -1;
    String USER_ID = "";
    final static int ACT_EDIT = 0;
    final static int LOGIN_SIGN = 496431;
    final static int AFTER_SIGN = 846244;
    final static int LOGIN_CHECK = 416843;
    final static String querySelectAll = String.format("SELECT * FROM %s", "TBL_Diary");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACT_EDIT:
                if (resultCode == RESULT_OK) {
                    myAdapter.onContentChanged(db);
                    list.setAdapter(myAdapter);
                }
                if (resultCode == RESULT_FIRST_USER){
                    Intent intentMod = new Intent(getApplicationContext(), ModActivity.class);
                    intentMod.putExtra("db_id", current_id);
                    startActivityForResult(intentMod, ACT_EDIT);
                }
                if (resultCode == LOGIN_SIGN){
                    Intent intentSign= new Intent(getApplicationContext(), SignActivity.class);
                    intentSign.putExtra("db_id", current_id);
                    startActivityForResult(intentSign,ACT_EDIT);
                }
                if(resultCode == LOGIN_CHECK){
                    USER_ID = data.getStringExtra("id");
                    LOGIN_SUCCESS = 1;
                }
                if(resultCode == AFTER_SIGN){
                    Intent intentLogin = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivityForResult(intentLogin,ACT_EDIT);
                }



                break;
                
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        list = (ListView) findViewById(R.id.list_diary);
        mHelper = new MyDBHelper(this);
        db = mHelper.getWritableDatabase();
        cursor = db.rawQuery(querySelectAll, null);
        myAdapter = new MyCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        list.setAdapter(myAdapter);

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivityForResult(intent, ACT_EDIT);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_id = id;
                Intent intent2 = new Intent(getApplicationContext(), ResultActivity.class);
                intent2.putExtra("db_id", id);
                startActivityForResult(intent2, ACT_EDIT);

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                String query = "DELETE FROM TBL_Diary WHERE _id = " + id;
                db.execSQL(query);
                myAdapter.onContentChanged(db);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.db_save) {
            if (LOGIN_SUCCESS != 1){
                Intent intentLogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivityForResult(intentLogin,ACT_EDIT);
            }else if(LOGIN_SUCCESS == 1){
                Toast.makeText(getApplicationContext(),USER_ID,Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        if (id == R.id.db_load) {
            if (LOGIN_SUCCESS == -1){
                Intent intentLogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivityForResult(intentLogin,ACT_EDIT);
            }else if(LOGIN_SUCCESS == 1){
                Toast.makeText(getApplicationContext(),USER_ID,Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
