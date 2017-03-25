package com.example.pratyushsharma.test;


import android.content.Intent;
import android.sax.RootElement;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Vikramaditya Patil on 12-03-2017.
 */

public class BikeFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_bike, container, false);
        final ArrayList<Bike> bikeList = new ArrayList<>();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Cycle");


        final BikeAdapter<Bike> bikeAdapter = new BikeAdapter(getActivity(), bikeList);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(bikeAdapter);
        final Intent basic = new Intent(getContext(), BikeDetail.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(basic);
            }
        });



        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                bikeList.clear();
                while(items.hasNext()){
                    DataSnapshot item = items.next();
                    String address,name,uid;
                    int hourly , daily , weekly;
                    address = item.child("mBikeAddress").getValue().toString();
                    name = item.child("mBikename").getValue().toString();
                    uid = item.child("mUID").getValue().toString();
                    hourly = Integer.parseInt(item.child("mPrice").child("mHourly").getValue().toString());
                    daily = Integer.parseInt(item.child("mPrice").child("mDaily").getValue().toString());
                    weekly =Integer.parseInt(item.child("mPrice").child("mWeekly").getValue().toString());
                    Price rate = new Price(hourly,daily,weekly);
                    Bike value = new Bike(name,address,uid,rate);
                    bikeList.add(value);
                }

                    bikeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
