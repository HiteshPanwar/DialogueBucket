package com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter.RecyclerAdapter;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Dialogue;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HITESH on 9/4/2017.
 */

public class Fragmenttwo extends Fragment {
    List<Dialogue> list;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    SearchView searchView;
    RecyclerAdapter recycler;
    ProgressBar progress;
    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private static final String USER_ID = "53";


    public Fragmenttwo() {
        Log.i("Fragment check", "Fragment Two Created");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_greatest, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView) v.findViewById(R.id.tv_no_movies);
        searchView = (SearchView) v.findViewById(R.id.sv);
        search(searchView);
        progress = (ProgressBar) v.findViewById(R.id.progressBar);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview.child(USER_ID).child("movies")
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatabaseReference = database.getReference("AUDIO").child("GREATEST");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String movieName = dataSnapshot1.child("movieName").getValue().toString();
                    String  path = dataSnapshot1.child("moviePoster").getValue().toString();
                    String  dialogueName = dataSnapshot1.child("dialogueName").getValue().toString();
                    String  dialogueId = dataSnapshot1.child("dialogueId").getValue().toString();
                    String storageLinker=dataSnapshot1.child("storageLinker").getValue().toString();

                    Dialogue listdata = new Dialogue();



                    listdata.setMovieName(movieName);
                    listdata.setDialoguePoster(path);
                    listdata.setDialogueName(dialogueName);
                    listdata.setDialogueId(dialogueId);
                    listdata.setStorageLinker(storageLinker);


                    list.add(listdata);
                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                recycler = new RecyclerAdapter(list, getActivity().getApplicationContext());
                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(recycler);
                tvNoMovies.setText("");
                progress.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;


    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                recycler.getFilter().filter(newText);
                return true;
            }
        });


    }
}