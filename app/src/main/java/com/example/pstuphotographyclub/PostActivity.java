package com.example.pstuphotographyclub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton selectImage;
    private EditText title;
    private EditText caption;
    private Button submit;
    private Uri imageUri = null;
    private StorageReference storage;
    private ProgressDialog progress;
    private DatabaseReference database;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Blog");

        selectImage = (ImageButton)findViewById(R.id.imageselect);
        title = (EditText)findViewById(R.id.title);
        caption = (EditText)findViewById(R.id.caption);
        submit = (Button)findViewById(R.id.submit);
        progress = new ProgressDialog(this);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

    }

    private  void startPosting() {

        progress.setMessage("Posting......");
        progress.show();

        final String mtitle = title.getText().toString().trim();
        final String mCaption = caption.getText().toString().trim();

        if(!TextUtils.isEmpty(mtitle) && !TextUtils.isEmpty(mCaption) && imageUri != null) {

            StorageReference filepath = storage.child("Blog_Images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = database.push();

                    newPost.child("title").setValue(mtitle);
                    newPost.child("caption").setValue(mCaption);
                    newPost.child("image").setValue(downloadUri.toString());

                    progress.dismiss();

                    startActivity(new Intent(PostActivity.this, MainActivity.class));

                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            selectImage.setImageURI(imageUri);
        }
    }
}
