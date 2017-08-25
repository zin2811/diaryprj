package com.example.administrator.mydiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-07-31.
 */

public class MyCursorAdapter extends CursorAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    Handler mHandler = new Handler();
    final static String querySelectAll = String.format("SELECT * FROM %s", "TBL_Diary");
    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        ImageView imv = (ImageView) view.findViewById(R.id.iv_image);

        String name = cursor.getString(cursor.getColumnIndex("title"));
        String imvloc = cursor.getString(cursor.getColumnIndex("imgloc"));
        String date = cursor.getString(cursor.getColumnIndex("writedate"));
        Bitmap bitmap = BitmapFactory.decodeFile(imvloc);
        Log.d("스트링 확인", name + ", " + date);
        imv.setImageBitmap(bitmap);
        tvName.setText(name);
        tvDate.setText(date);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_list, parent, false);
        return v;
    }
    protected void onContentChanged(final SQLiteDatabase db) {
        super.onContentChanged();
        new Thread() {
            public void run() {
                final Cursor cursor = db.rawQuery(querySelectAll, null);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        changeCursor(cursor);
                    }
                });
            }
        }.start();
    }
}