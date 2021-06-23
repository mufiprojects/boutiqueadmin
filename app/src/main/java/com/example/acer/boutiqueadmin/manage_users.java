package com.example.acer.boutiqueadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static com.example.acer.boutiqueadmin.common.items; //ITEM KEY | ITEM NAME
import static com.example.acer.boutiqueadmin.common.userHashMap; //USER ID | USER NAME

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.example.acer.boutiqueadmin.common.userHashMap;

public class manage_users extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseActiveUsers;

    HashMap<String,designerOrderDetail> designerOrderMap= new HashMap<>();

    TextView exampleText;

    String selectedDate;

    //XML
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noOrderMsgText;

    //FIREBASE OBJECTS
    private FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerOptions<user> options;


    //VARIABLES
    private long itemCount;

    //OBJECTS
    private common common=new common();

    ListView userListViw;
    HashMap<String,String> userMap=new HashMap<>();
    List<String> userList=new ArrayList<>();




    String option="orderDate";
    private String TAG="manage_users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        databaseActiveUsers = database.getReference("activeUsers");
//        getEachOrderToMap();


        progressBar = findViewById(R.id.progressBar);
        userListViw=findViewById(R.id.usersListView);
        recyclerView =findViewById(R.id.orderListRecyclerView);

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        fetChUsersFromDatabase();
        setAdapter();

        userListViw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(manage_users.this, userMap.get(userList.get(position)), Toast.LENGTH_SHORT).show();

                Intent userStateActivity=new Intent(getApplicationContext(),user_state.class);
                userStateActivity.putExtra("userId",userMap.get(userList.get(position)));
                userStateActivity.putExtra("userName",userList.get(position));
                startActivity(userStateActivity);
            }
        });

//        fetchData();
//        fetchDataToAdapter();


    }

    private void fetChUsersFromDatabase() {

        databaseActiveUsers.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userList.add(userHashMap.get(snapshot.getKey()));
                userMap.put(userHashMap.get(snapshot.getKey()),snapshot.getKey());
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
    private void setAdapter(){

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,
                userList);

        userListViw.setAdapter(arrayAdapter);

    }

//    private void getEachOrderToMap(){
//        databaseOrders.orderByChild("orderDate").equalTo("24/04/2021").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//                int i = 0;
//                int lastPrice = 0;
//                int newNo = 0;
//                int newPrice = 0;
//                int countOrder = 0;
//                designerOrderDetail designerOrderDetail = new designerOrderDetail();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    String designerId = (String) dataSnapshot.child("designerId").getValue().toString();
//                    int orderAmount = Integer.parseInt(dataSnapshot.child("orderAmount").getValue().toString());
//
//
//                    if (designerOrderMap.containsKey(designerId)) {
//                        countOrder = designerOrderMap.get(designerId).getNoOfOrders();
//                        lastPrice = designerOrderMap.get(designerId).getOrderAmount();
//
//                        designerOrderDetail.setNoOfOrders(countOrder + 1);
//                        designerOrderDetail.setOrderAmount(lastPrice + orderAmount);
//                        designerOrderMap.put(designerId, designerOrderDetail);
//
//                        Log.d("IF LOOP",i+" "+designerId+" "+"countOrder"+" "+countOrder
//                                +" "+"lastprice"+" "+lastPrice +"+"+"orderAmount"+" "+
//                                orderAmount);
//                    }
//                    else {
//                        designerOrderDetail.setNoOfOrders(1);
//                        designerOrderDetail.setOrderAmount(orderAmount);
//                        designerOrderMap.put(designerId, designerOrderDetail);
//                        Log.d("ELSE LOOP",i+" "+designerId+" "+"countOrder"+" "+
//                                designerOrderDetail.getNoOfOrders()+" "+"Amount" +
//                                designerOrderDetail.getOrderAmount()+" "+"Firebase Amount"+" "+
//                                orderAmount);
//                    }
//                    i++;
//
//
//
//                }
//
//                Log.d("OrderDetail", designerOrderMap.toString());
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


//    //Fetch data from Fire base and add to Fire base RecyclerAdapter
//    private void fetchData() {
//
//
//
//        final Query query = FirebaseDatabase.getInstance().
//                getReference("activeUsers").orderByKey();
//        //Query for fetch data
//
//        options = new FirebaseRecyclerOptions.Builder<user>()
//                        .setQuery(query, new SnapshotParser<user>() {
//                            @NonNull
//                            @Override
//                            public user parseSnapshot(@NonNull DataSnapshot snapshot) {
//                                Log.d(TAG,"parseSnapshotRunned");
//
//                                String uid = (String) snapshot.getKey();
//                                Log.d(TAG,uid);
//                                Boolean isUserActive=(Boolean)  snapshot.getValue();
//                                return new user(uid,isUserActive);
//                            }
//                        }).build();
//    }
//
//
//
//
//    private void fetchDataToAdapter()
//
//    {
//        adapter = new FirebaseRecyclerAdapter<user,ViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull user user) {
//
//                String designerName=userHashMap.get(user.getUserName());
//                viewHolder.setUserNameTextView(designerName);
//
//            }
//
//
//
//            @NonNull
//            @Override
//            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user,
//                        parent, false);
//                return new ViewHolder(view);
//            }
//        };
//
//    }
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView userNameTextView;
//        private MaterialButton activateBtn;
//        private MaterialButton deActivateBtn;
//
//
//
//
//        private ViewHolder(View itemView) {
//            super(itemView);
//            userNameTextView=itemView.findViewById(R.id.userName);
//            activateBtn = itemView.findViewById(R.id.activate_btn);
//            deActivateBtn=itemView.findViewById(R.id.deactivate_btn);
//
//
//        }
//
//
//        public void setUserNameTextView(String userNameTextView) {
//            this.userNameTextView.setText(userNameTextView);
//        }
//
//
//    }
//
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//
//    private void setProgressBarOn()
//    {
//        progressBar.setVisibility(View.VISIBLE);
//    }
//
//    private void setProgressBarOff(){
//        progressBar.setVisibility(View.GONE);
//    }

    //Moving to orderDetailsActivity by intent
//    private void toOrderDetailsActivity(orderbook orderbook) {
//        Intent orderDetailsActivity = new Intent(getContext(), orderDetailsActivity.class);
//        common.putExtra(orderDetailsActivity,orderbook);
//        startActivity(orderDetailsActivity);
//    }


}