package com.example.administrator.mydiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ModActivity extends AppCompatActivity {
    EditText etTitle, etText;
    ImageView imv ;
    String title = "";
    String text = "";
    String query;
    MyDBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    long db_id;
    String imgloc;
    Uri photoURI, albumURI;

    boolean isAlbum = false;
    private File file = null;
    private final int REQUEST_IMAGE_CAPTURE = 1234;
    private final int REQUEST_IMAGE_ALBUM = 4321;
    private final int REQUEST_IMAGE_CROP = 3142;
    public void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(photoURI, "image/*");
        cropIntent.putExtra("scale", true);
        if (isAlbum == false) {
            cropIntent.putExtra("output", photoURI);
        } else if (isAlbum == true) {
            cropIntent.putExtra("output", albumURI);
        }
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            if (file != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                imgloc = file.getAbsolutePath();
                imv.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getApplicationContext(), "File is null", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_IMAGE_ALBUM && resultCode == RESULT_OK) {
            isAlbum = true;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            File albumFile = null;
            try {
                albumFile = createImageFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (albumFile != null) {
                albumURI = Uri.fromFile(albumFile);
            }
            photoURI = data.getData();
            cropImage();
        } else if (requestCode == REQUEST_IMAGE_CROP && resultCode == RESULT_OK) {
            galleryAddPic();
            Bitmap bitmap = BitmapFactory.decodeFile(imgloc);
            imv.setImageBitmap(bitmap);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imgloc);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        long now = System.currentTimeMillis();
        String imageFileName = "/photo" + String.valueOf(now) + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + imageFileName);
        imgloc = storageDir.getAbsolutePath();
        return storageDir;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        etText = (EditText) findViewById(R.id.et_text);
        etTitle = (EditText) findViewById(R.id.et_title);

        Intent intent = getIntent();
        db_id = intent.getLongExtra("db_id", -1);

        mHelper = new MyDBHelper(this);
        db = mHelper.getWritableDatabase();
        cursor = db.rawQuery(" select * from TBL_Diary where _id=? ;", new String[]{String.valueOf(db_id)});
        cursor.moveToNext();
        String getTitle = cursor.getString(1);
        String getImg = cursor.getString(2);
        String getText = cursor.getString(3);
        Bitmap bitmap = BitmapFactory.decodeFile(getImg);
        long now = System.currentTimeMillis();
        imgloc = getImg;
        file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/photo" + String.valueOf(now) + ".jpg");
        imv = (ImageView)findViewById(R.id.imv);
        imv.setImageBitmap(bitmap);
        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                };


                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_IMAGE_ALBUM);
                    }
                };


                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };


                new AlertDialog.Builder(ModActivity.this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();

            }
        });
        etTitle.setText(getTitle);
        etText.setText(getText);

        Button fab = (Button) findViewById(R.id.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = mHelper.getWritableDatabase();
                title = etTitle.getText().toString();
                text = etText.getText().toString();
                title = title.replaceAll("'","''");
                text = text.replaceAll("'","''");
                query = "UPDATE TBL_Diary SET title = '"+title+"',imgloc = '"+imgloc+"',texts = '"+text+"' WHERE _id = " + db_id;
                db.execSQL(query);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
