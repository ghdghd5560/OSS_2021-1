package project.oss_2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private Button nextBtn, addBtn;
    private Spinner Email_Spinner;
    private EditText Email_Id;
    DatabaseReference dbref;
            
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Email_Id = (EditText) findViewById(R.id.Email_Id);
        Email_Spinner = (Spinner) findViewById(R.id.Email_spinner);
        addBtn = (Button) findViewById(R.id.nextPage2);
        dbref = FirebaseDatabase.getInstance().getReference("Email");
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertdata();
                }
            });
        


//        nextBtn = (Button) findViewById(R.id.nextPage2);
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, AuthenticationActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void insertdata() {
        String data = Email_Id.getText().toString().trim();
        dbref.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Email_Id.setText("");
                Toast.makeText(LoginActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}