package project.oss_2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class AuthenticationActivity extends AppCompatActivity {

    private Button nextBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        EditText edt1 = (EditText) findViewById(R.id.edt1);
        edt1.setNextFocusDownId(R.id.edt2);
        EditText edt2 = (EditText) findViewById(R.id.edt2);
        edt2.setNextFocusDownId(R.id.edt3);
        EditText edt3 = (EditText) findViewById(R.id.edt3);
        edt3.setNextFocusDownId(R.id.edt4);
        EditText edt4 = (EditText) findViewById(R.id.edt4);
        edt4.setNextFocusDownId(R.id.edt5);
        EditText edt5 = (EditText) findViewById(R.id.edt5);
        edt5.setNextFocusDownId(R.id.edt6);

    }
}