package com.example.acer.boutiqueadmin;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philliphsu.bottomsheetpickers.BottomSheetPickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import static com.example.acer.boutiqueadmin.common.items;
import static com.example.acer.boutiqueadmin.common.userHashMap;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, BottomSheetTimePickerDialog.OnTimeSetListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseOrders;

    TextView totalNoOfOrdersText;
    TextView totalOrderAmountText;
    TextView dateView,delBtn,ManageUsersBtn;


    CardView salesOverviewCard;
    LinearLayout salesReportLayout;
    String childName;
    String orderDate;
    String TAG="Dashboard";

    String selectedDate;

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    Map<String, Boolean> productionStatusMap = new HashMap<>();

    //FIREBASE OBJECTS
    DatabaseReference databaseItems;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRegistered();

        databaseOrders = database.getReference("orders");

        totalNoOfOrdersText = findViewById(R.id.totalOrdersNoText);
        totalOrderAmountText=findViewById(R.id.totalAmountText);
        salesOverviewCard=findViewById(R.id.salesOverviewCard);
        dateView=findViewById(R.id.dateView);
        delBtn=findViewById(R.id.deliveryBtn);
        ManageUsersBtn=findViewById(R.id.manageUsersBtn);


        getTotalNoOfOrders();
        fetchItems();
        fetchUsers();
        childName="orderDate";
        orderDate="24/04/2021";
        salesOverviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent salesActivity=new Intent(getApplicationContext(),salesReport.class);
                salesActivity.putExtra("selectedDate",selectedDate);
                salesActivity.putExtra("deliveryDate","orderDate");
                startActivity(salesActivity);

            }
        });


        //DateViewListner

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newBottomDatePicker=createNewBottomDatePicker();
                newBottomDatePicker.show(getSupportFragmentManager(),"datePicker");
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent salesActivity=new Intent(getApplicationContext(),salesReport.class);
                salesActivity.putExtra("selectedDate",selectedDate);
                salesActivity.putExtra("deliveryDate","deliveryDate");
                startActivity(salesActivity);

            }
        });
        ManageUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,manage_users.class));
            }
        });
        

    }
    private DialogFragment createNewBottomDatePicker(){
        BottomSheetPickerDialog dialog =null;
        Calendar now=Calendar.getInstance();
        dialog= DatePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog datePickerDialog=(DatePickerDialog) dialog;
        datePickerDialog.setMinDate(now);
        Calendar max=Calendar.getInstance();
        max.add(Calendar.YEAR,10);
        datePickerDialog.setYearRange(1970,2032);
        return dialog;


    }


    private void getTotalNoOfOrders() {
        databaseOrders.orderByChild("orderDate").equalTo(selectedDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalNoOfOrdersText.setText(String.valueOf(snapshot.getChildrenCount()));

                int total=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    try {
                        String totalOrderAmount = (String) dataSnapshot.child("orderAmount").getValue();
                        int amount=Integer.parseInt(totalOrderAmount);
                        total=total+amount;
                    }
                    catch (Exception e){
                        e.getMessage();
                    }

                }
                totalOrderAmountText.setText(" ₹"+String.valueOf(total));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Date date =new GregorianCalendar(year,monthOfYear,dayOfMonth+1).getTime();
        setSelectedDate(dateToString(date));
        dateView.setText(dateToString(date));
        getTotalNoOfOrders();


    }

    @Override
    public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {

    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;

    }
    public String dateToString(Date selectedDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateToString = dateFormat.format(selectedDate);
        return dateToString;
    }
    private void fetchItems()
    {
        databaseItems=database.getReference("items");
        databaseItems.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists())
                    try {

                        items.put(dataSnapshot.getKey(), (String) dataSnapshot.getValue());
                    }
                    catch (NullPointerException e)
                    {
                        Toast.makeText(MainActivity.this, "Loading Items Failed..",
                                Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void fetchUsers(){
        databaseUsers=database.getReference("users");
        databaseUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userHashMap.put(dataSnapshot.getKey(), (String) dataSnapshot.child("name").getValue());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        isRegistered();
    }
    private void isRegistered()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this, authentication.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}