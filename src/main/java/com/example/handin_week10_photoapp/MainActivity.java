package com.example.handin_week10_photoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.handin_week10_photoapp.controller.ImageController;
import com.example.handin_week10_photoapp.storage.FirebaseManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button select_btn;
    private Button upload_btn;
    public ImageView imageView;
    ImageController imageController;
    FirebaseManager firebaseManager;

    public StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageReference = FirebaseStorage.getInstance().getReference("images");
        imageController = new ImageController(this);
        firebaseManager = new FirebaseManager(imageController, this);

        select_btn = findViewById(R.id.select_btn);
        upload_btn = findViewById(R.id.upload_btn);
        imageView = findViewById(R.id.imageView);

        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageController.selectImage();
            }
        });
        upload_btn.setOnClickListener(this);
    }

    public void showImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:  // take photo
                    imageController.getImageFromCamera(resultCode, data);
                    break;
                case 1: // choose from gallery
                    imageController.getImageFromGallery(resultCode, data);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == select_btn) {
            showImage();
        } else if (v == upload_btn) {
            firebaseManager.uploadImage();
        }
    }
}
