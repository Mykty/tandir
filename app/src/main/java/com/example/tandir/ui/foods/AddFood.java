package com.example.tandir.ui.foods;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tandir.R;
import com.example.tandir.module.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class AddFood extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    CardView cardView;
    StorageReference storageReference;
    ImageView bookImage;
    File file;
    Uri fileUri;
    Button submit;
    TextView changeIt;
    private static final int PERMISSION_REQUEST_CODE = 200;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    Uri last_file_uri;
    boolean photoSelected = false;
    EditText foodName, foodDesc, foodPrice;
    String foodType, foodAvailable = "yes";
    RadioButton radioNan, radioSamsa, radioSusyn;
    Spinner spinnerFoodAvailable;
    String yesNoArray [] = {"yes", "no"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food);
        initView();
        initTakePhoto();
    }

    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Food");

        changeIt = findViewById(R.id.changeIt);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        submit = findViewById(R.id.submit);

        cardView = findViewById(R.id.takePhoto);
        foodName = findViewById(R.id.foodName);
        foodDesc = findViewById(R.id.foodDesc);
        foodPrice = findViewById(R.id.foodPrice);

        radioNan = findViewById(R.id.radioNan);
        radioSamsa = findViewById(R.id.radioSamsa);
        radioSusyn = findViewById(R.id.radioSusyn);
        spinnerFoodAvailable = findViewById(R.id.spinnerAvailable);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        submit.setOnClickListener(this);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yesNoArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodAvailable.setAdapter(spinnerArrayAdapter);
        spinnerFoodAvailable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                foodAvailable = adapterView.getItemAtPosition(pos).toString();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    public void initTakePhoto() {
        file = null;
        fileUri = null;
        cardView = findViewById(R.id.takePhoto);
        bookImage = findViewById(R.id.bookImage);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(AddFood.this);
                } else {
                    requestPermission();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        boolean bookFilled = true;
        //book_scannered = true;

        if (foodName.getText().toString().trim().equals("")) {
            foodName.setError("Please fill name");
            bookFilled = false;
        }
        if (foodDesc.getText().toString().trim().equals("")) {
            foodDesc.setError("Please fill description");
            bookFilled = false;
        }
        if (foodPrice.getText().toString().trim().equals("")) {
            foodPrice.setError("Please fill food Price");
            bookFilled = false;
        }
        if(!photoSelected){
            bookFilled = false;
            changeIt.setError("Please select food image");
        }

        if(radioNan.isSelected()) foodType = "nan";
        if(radioSamsa.isSelected()) foodType = "samsa";
        else foodType = "susyn";


        if (bookFilled) {
            uploadImage();
        }else{
            Toast.makeText(AddFood.this, "Check errors and try again!", Toast.LENGTH_SHORT).show();
        }
    }

    String downloadUri;

    private String uploadImage() {
        if (fileUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.setTitle(getResources().getString(R.string.photoLoading));
            progressDialog.show();

            final String imageStorageName = UUID.randomUUID().toString();
            final String photoPath = "food_images/" + imageStorageName;
            final StorageReference ref = storageReference.child(photoPath);
            ref.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri.toString();


                                }
                            });

                            if (downloadUri != null) {
                                String fkey = getFId();
                                String fName = foodName.getText().toString();
                                String fDesc = foodDesc.getText().toString();
                                int fPrice = Integer.parseInt(foodPrice.getText().toString());

                                Food food = new Food(fkey, fName,fDesc,foodType,fPrice,foodAvailable);
                                databaseReference.child("food_list").child(fkey).setValue(food);

                                submit.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                Toast.makeText(AddFood.this, "Food added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddFood.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }else{
            Toast.makeText(this, "Error with food image, try again!", Toast.LENGTH_SHORT).show();
        }
        return downloadUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();
                bookImage.setImageURI(fileUri);
                changeIt.setText("Change Image");
                changeIt.setError(null);

                photoSelected = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == 10) {
            bookImage.setImageURI(last_file_uri);
        }
    }

    public String getFId() {
        Date date = new Date();
        String idN = "i" + date.getTime();
        return idN;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

}
