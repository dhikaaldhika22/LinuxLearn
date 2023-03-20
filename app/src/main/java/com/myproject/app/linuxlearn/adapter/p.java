package com.myproject.app.linuxlearn.adapter;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class p {
    private StorageTask uploadTask;
    private StorageReference storageReferencePic;

    private void main() {
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = (Uri) task.getResult();
                    downloadUri.toString();
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", downloadUri);
                }
            }
        });
    }
}
