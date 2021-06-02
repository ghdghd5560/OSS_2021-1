package project.oss_2021;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Cards cards_data[];
    private ArrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference userDb;

    ListView listView;
    List<Cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();


        checkUserSex();


        rowItems = new ArrayList<Cards>(); // 카드 배열들

        arrayAdapter = new ArrayAdapter(this, R.layout.item, rowItems ); //카드마다 al의 텍스트를 출력, item은 텍스트뷰, 색깔

        //al.add("java") <- 실행은 되지만 이것만 쓰면 앱 상에선 출력이 되질않음
        //arrayAdapter.notifyDataSetChanged(); <- 변경되었다는 것을 써야함

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            // 카드를 없앰
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            //카드를 왼쪽으로 넘길 때
            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards object = (Cards) dataObject;
                String userId = object.getUserId();
                userDb.child(userSexOpp).child(userId).child("connection").child("nope").child(currentUId).setValue(true); //왼쪽으로 넘기면 connection 노드 -> nope 노드의 값을 true

                Toast.makeText(MainActivity.this, "Nope!", Toast.LENGTH_SHORT).show();
            }

            //카드를 오른쪽으로 넘길 때
            @Override
            public void onRightCardExit(Object dataObject) {
                Cards object = (Cards) dataObject;
                String userId = object.getUserId();
                userDb.child(userSexOpp).child(userId).child("connection").child("like").child(currentUId).setValue(true); //오른쪽으로 넘기면 connection 노드 -> like 노드 -> 현재 유저의 노드 값을 true
                isConnectionMatch(userId);
                Toast.makeText(MainActivity.this, "Like!", Toast.LENGTH_SHORT).show();
            }


            //카드를 다 소진 시켰을 때
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //매칭 함수
    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = userDb.child(userSex).child(currentUId).child("connection").child("like").child(userId); // 나에게 like를 보낸 유저
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(MainActivity.this, "new Connection" , Toast.LENGTH_SHORT).show();
                    userDb.child(userSexOpp).child(snapshot.getKey()).child("connection").child("matches").child(currentUId).setValue(true);  //"나"의 matches 노드에 "나에게 like를 보낸 유저"의 노드 값을 true
                    userDb.child(userSex).child(currentUId).child("connection").child("matches").child(snapshot.getKey()).setValue(true);  //"나에게 like를 보낸 유저"의 matches 노드에 "나"의 노드 값을 true
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String userSex; // 사용자의 성별
    private String userSexOpp; // 사용자의 반대 성별
    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // maleDb의 이벤트리스너
        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals(user.getUid())) {
                    userSex = "Male";
                    userSexOpp = "Female";
                    getOppositeSexUsers();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //femaleDb의 이벤트리스너
        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals(user.getUid())) {
                    userSex = "Female";
                    userSexOpp = "Male";
                    getOppositeSexUsers();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //반대 성별의 유저함수
    public void getOppositeSexUsers() {
        DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userSexOpp);
        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // nope 또는 like의 유저 값이 true가 아니면 카드 배열에 추가
                if(snapshot.exists() && !snapshot.child("connection").child("nope").hasChild(currentUId) && !snapshot.child("connection").child("like").hasChild(currentUId)) {
                    String profileImageUrl = "dafault";

                    if(snapshot.child("profileImageUrl").getValue().equals("default")){
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    Cards item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), profileImageUrl); //반대 성별의 name, profileImageUrl을 카드 배열에 추가한다
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //로그아웃
    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, StartActivity.class); // MainActivity -> ChooseLoginRegistrationActivity
        startActivity(intent);
        finish();
        return;
    }
    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
        return;
    }

    /*
    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }
    */

}