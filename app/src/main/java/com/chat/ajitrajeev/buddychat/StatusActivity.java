package com.chat.ajitrajeev.buddychat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    TextInputLayout mStatus;
    Button mSaveBtn;

    //firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    //Progress
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mToolbar = (Toolbar)findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String status_value = getIntent().getStringExtra("status_value");

        mSaveBtn = (Button)findViewById(R.id.status_save_btn);
        mStatus = (TextInputLayout)findViewById(R.id.updated_status);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mStatus.getEditText().setText(status_value);

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progress
                mProgressDialog = new ProgressDialog(StatusActivity.this);
                mProgressDialog.setTitle("Saving Changes");
                mProgressDialog.setMessage("Please wait while we save the changes");
                mProgressDialog.show();
                String status = mStatus.getEditText().getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgressDialog.dismiss();
                        }
                        else
                        {
                            mProgressDialog.hide();
                            Toast.makeText(getApplicationContext(),"There are some error while saving changes",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }
}
