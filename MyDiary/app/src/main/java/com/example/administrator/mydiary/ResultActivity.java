package com.example.administrator.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    MyDBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;

    TextView tv_title, tv_date, tv_text;
    ImageView im;

    long db_id = -1;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_text = (TextView) findViewById(R.id.tv_text);
        im = (ImageView)findViewById(R.id.imageView);
        mHelper = new MyDBHelper(this);
        db = mHelper.getWritableDatabase();

        db_id = intent.getLongExtra("db_id", -1);
        cursor = db.rawQuery(" select * from TBL_Diary where _id=? ;", new String[]{String.valueOf(db_id)});
        cursor.moveToNext();
        String getTitle = cursor.getString(1);
        String getImg = cursor.getString(2);
        String getText = cursor.getString(3);
        String getDate = cursor.getString(4);
        Bitmap bitmap = BitmapFactory.decodeFile(getImg);
        im.setImageBitmap(bitmap);
        tv_title.setText(getTitle);
        tv_date.setText(getDate);
        tv_text.setText(getText);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mod) {
            setResult(RESULT_FIRST_USER);
            finish();
            return true;
        }
        if (id == R.id.del) {
            String query = "DELETE FROM TBL_Diary WHERE _id = " + db_id;
            db.execSQL(query);
            Intent data = new Intent();
            setResult(RESULT_OK, data);

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
