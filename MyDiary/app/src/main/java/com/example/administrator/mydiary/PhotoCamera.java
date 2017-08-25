package com.example.administrator.mydiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class PhotoCamera{
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUri;
    private int id_view;
    private String absoultePath;
    Context mContext;
    public void doTakePhotoAction(Context context,String runAct) // 카메라 촬영 후 이미지 가져오기
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mContext = context;

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        if (runAct.equals("write")){
            ((WriteActivity)mContext).startActivityForResult(intent, PICK_FROM_ALBUM);
        }else if(runAct.equals("mod")){
            ((ModActivity)mContext).startActivityForResult(intent, PICK_FROM_ALBUM);
        }

    }


    public void doTakeAlbumAction(Context context,String runAct) // 앨범에서 이미지 가져오기
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        mContext = context;

        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        if (runAct.equals("write")){
            ((WriteActivity)mContext).startActivityForResult(intent, PICK_FROM_ALBUM);
        }else if(runAct.equals("mod")){
            ((ModActivity)mContext).startActivityForResult(intent, PICK_FROM_ALBUM);
        }
    }
    protected void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyPhoto";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



