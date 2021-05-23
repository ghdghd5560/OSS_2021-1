package project.oss_2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LoginActivity extends AppCompatActivity {

    private Button nextBtn;
    private Spinner Email_spinner;
    private EditText Email_Id;
    private String textData;

    DatabaseReference dbref;
    ValueEventListener listener;
    ArrayList<String> Univ_List;
    ArrayAdapter<String> Adap_Univ_List;

    //다음화면으로 넘거기가 위한 버튼 이벤트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email_spinner = (Spinner) findViewById(R.id.Email_spinner);
        Email_Id = (EditText) findViewById(R.id.Email_Id);
        dbref = FirebaseDatabase.getInstance().getReference("Email");

        Univ_List = new ArrayList<>();
        Adap_Univ_List = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, Univ_List);

        Email_spinner.setAdapter(Adap_Univ_List);
        retrieveData();

        nextBtn = (Button) findViewById(R.id.nextPage2);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AuthenticationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void retrieveData() {
        listener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Univ_List.add(item.getValue().toString());
                }
                Adap_Univ_List.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void btnAddData(View view) {
        textData = Email_Id.getText().toString().trim();
        dbref.push().setValue(textData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Email_Id.setText("");
                Toast.makeText(LoginActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}