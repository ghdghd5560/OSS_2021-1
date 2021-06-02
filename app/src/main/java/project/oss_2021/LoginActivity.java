package project.oss_2021;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button Previous, sendAuthBtn;
    private EditText mEmail, mPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        sendAuthBtn = (Button) findViewById(R.id.sendAuthEmail);
        sendAuthBtn.setOnClickListener(LoginActivity.this);
        Previous = (Button) findViewById(R.id.Previous);
        Previous.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View v){
        progressBar.setVisibility(View.INVISIBLE);

        switch(v.getId()){
            case R.id.Previous:
                startActivity(new Intent(LoginActivity.this, StartActivity.class));
                break;

            case R.id.sendAuthEmail:
                userLogin();
                break;
        }
    }

    private void userLogin(){
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        if(email.isEmpty()){
            mEmail.setError("Email is empty");
            mEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please enter a valid email");
            mEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            mPassword.setError("password is empty");
            mPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            mPassword.setError("Min password length is 6 character");
            mPassword.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Failed to login! Please Check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}