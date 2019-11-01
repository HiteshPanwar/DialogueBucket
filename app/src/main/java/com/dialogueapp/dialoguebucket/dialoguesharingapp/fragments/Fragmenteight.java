package com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter.RecyclerAdapterApp;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter.RecyclerAdapterCategory;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Category;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HITESH on 9/4/2017.
 */

public class Fragmenteight extends Fragment{
    List<Category> list;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private static final String USER_ID = "53";




    public Fragmenteight(){
        Log.i("Fragment check","Fragment eight Created");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_app,container,false);

        /*CardView c=(CardView) v.findViewById(R.id.cv1);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent myIntent = new Intent(getActivity(),Bactor.class);
                //startActivity(myIntent);
              //  startActivity(myIntent);




               Intent intent = new Intent(getContext(), BrowseCategories.class);
               startActivity(intent);


               // Intent j = new Intent(fBaseCtx, NewactivityName.class);
               // startActivity(j);
            }
        });*/

        mRecyclerView = (RecyclerView)v.findViewById(R.id.my_category_recycler_view);
        tvNoMovies = (TextView) v.findViewById(R.id.tv_no_movies);



        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview.child(USER_ID).child("movies")
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatabaseReference=  database.getReference("APP");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    String categoryName = dataSnapshot1.child("categoryName").getValue().toString();
                    String  path = dataSnapshot1.child("categoryPoster").getValue().toString();
                    String categoryId = dataSnapshot1.child("categoryId").getValue().toString();
                    String categoryLinker = dataSnapshot1.child("categoryLinker").getValue().toString();
                    Category listdata = new Category(categoryName,path,categoryId,categoryLinker);



                    listdata.setCategoryName(categoryName);
                    listdata.setCategoryPoster(path);
                    listdata.setCategoryId(categoryId);
                    listdata.setCategoryLinker(categoryLinker);


                    list.add(listdata);
                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                RecyclerAdapterApp recycler = new RecyclerAdapterApp(list,getActivity().getApplicationContext());

                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());
                mRecyclerView.setLayoutManager(layoutmanager);
                mRecyclerView.setItemAnimator( new DefaultItemAnimator());
                mRecyclerView.setAdapter(recycler);
                tvNoMovies.setText("");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        return v;






    }
}
