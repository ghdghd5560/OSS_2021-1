package project.oss_2021;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRegister, Previous, nextPage;
    private EditText mEmail, mPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mRegister = (Button) findViewById(R.id.sendAuthEmail);
        mRegister.setOnClickListener(SignUpActivity.this);
        Previous = (Button) findViewById(R.id.Previous);
        Previous.setOnClickListener(SignUpActivity.this);
        nextPage = (Button) findViewById(R.id.nextPage);
        nextPage.setOnClickListener(SignUpActivity.this);
    }

    @Override
    public void onClick(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        switch(v.getId()){
            case R.id.Previous:
                startActivity(new Intent(SignUpActivity.this, StartActivity.class));
                break;

            case R.id.sendAuthEmail:
                userCreate();
                break;

            case R.id.nextPage:
                nextPage();
                break;
        }
    }


    private void nextPage(){
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(SignUpActivity.this, RegistrationActivity.class));
                        finish();
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(SignUpActivity.this, "Check your email to verify", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(SignUpActivity.this, "Failed to login! Please Check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void userCreate() {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        if (email.isEmpty()) {
            mEmail.setError("Email is empty");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please enter a valid email");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("password is empty");
            mPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Min password length is 6 character");
            mPassword.requestFocus();
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        startActivity(new Intent(SignUpActivity.this, RegistrationActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(SignUpActivity.this, "Check your email to verify", Toast.LENGTH_SHORT).show();
                        if (user.isEmailVerified()) {
                            startActivity(new Intent(SignUpActivity.this, RegistrationActivity.class));
                        }
                    }

                }else {
                    Toast.makeText(SignUpActivity.this, "Login has already been taken ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}