package com.example.handin_week10_photoapp.storage;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.handin_week10_photoapp.MainActivity;
import com.example.handin_week10_photoapp.controller.ImageController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class FirebaseManager {

    private MainActivity mainActivity;
    private ImageController imageController;

    public FirebaseManager(ImageController imageController, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.imageController = imageController;
    }

    //method to create file extension
    private String getExtension(Uri uri) {
        ContentResolver cr = mainActivity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    // handle upload files
    public void uploadImage() {
        if (imageController.filePath != null) {
            StorageReference ref = mainActivity.storageReference.child(System.currentTimeMillis() + "" + getExtension(imageController.filePath));
            ref.putFile(imageController.filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(mainActivity, "Image uploaded!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }else{
            Toast.makeText(mainActivity, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


}
