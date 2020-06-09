package com.example.handin_week10_photoapp.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import com.example.handin_week10_photoapp.MainActivity;
import java.io.IOException;
import static android.app.Activity.RESULT_OK;

public class ImageController {

    private MainActivity mainActivity;
    public Uri filePath;

    public ImageController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                checkImageOptions(dialog, item, options);
            }
        });
        builder.show();
    }

    public void checkImageOptions(DialogInterface dialog, int item, CharSequence[] options) {
        if (options[item].equals("Take Photo")) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mainActivity.startActivityForResult(takePicture, 0);
        } else if (options[item].equals("Choose from Gallery")) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mainActivity.startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
        } else if (options[item].equals("Cancel")) {
            dialog.dismiss();
        }
    }

    public void getImageFromGallery(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), filePath);
                mainActivity.imageView.setImageBitmap(bitmap);
            } catch (IOException e) {

            }
        }
    }

    public void getImageFromCamera(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mainActivity.imageView.setImageBitmap(bitmap);
            String path = MediaStore.Images.Media.insertImage(mainActivity.getContentResolver(), bitmap, "title", null);
            if (path != null) {
                filePath = Uri.parse(path);
            }
        }
    }
}
