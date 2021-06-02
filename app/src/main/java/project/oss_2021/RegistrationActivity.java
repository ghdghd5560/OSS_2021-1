package project.oss_2021;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mPhone, mName, mIntroduction;
    private ImageView mProfileImage;

    private RadioGroup mRadioGroup;

    private DatabaseReference userDb;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if(user != null) { //회원가입이 성공하면
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class); // RegistrationActivity에서 MainActivity를 호출
                    startActivity(intent);
                    finish(); //액티비티 종료
                    return;
                }
            }
        };

        mRegister = findViewById(R.id.register);

        mPhone = findViewById(R.id.phone);
        mName = findViewById(R.id.name);
        mIntroduction = findViewById(R.id.intro);
        mProfileImage = findViewById(R.id.profileImage);

        mRadioGroup = findViewById(R.id.radioGroup);

        mRegister.setOnClickListener(view -> {
            int selectId = mRadioGroup.getCheckedRadioButtonId(); // 라디오버튼 체크 확인

            final RadioButton radioButton = (RadioButton) findViewById(selectId);

            if(radioButton.getText() == null) {
                return;
            } // 성별을 정하지 않을 경우 이 리스너는 여기서 종료

            final String name = mName.getText().toString();
            final String phone = mPhone.getText().toString();
            final String intro = mIntroduction.getText().toString();

            //일단 보류
            //String userId = mAuth.getCurrentUser().getUid();


            //userDb.child(radioButton.getText().toString()).child(userId).child("Info").child("Name").child(name);
            //userDb.child(radioButton.getText().toString()).child(userId).child("Info").child("Phone").child(phone);
            //userDb.child(radioButton.getText().toString()).child(userId).child("Info").child("Introduction").child(intro);

            //유저 정보
            Map userInfo = new HashMap<>();
            userInfo.put("name", name);
            userInfo.put("phone", phone);
            userInfo.put("introduction", intro);
            userInfo.put("profileImageUrl", "default");
            userDb.updateChildren(userInfo);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}