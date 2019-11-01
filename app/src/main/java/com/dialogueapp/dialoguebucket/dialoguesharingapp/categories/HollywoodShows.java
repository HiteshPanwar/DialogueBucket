
package com.dialogueapp.dialoguebucket.dialoguesharingapp.categories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter.RecyclerAdapter;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Dialogue;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;

public class HollywoodShows extends AppCompatActivity {
    List<Dialogue> list;
    String link;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    SearchView searchView;
    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    RecyclerAdapter recycler;
    private static final String USER_ID = "53";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_audio_hollywoodshows);

        //Initializing our Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView) findViewById(R.id.tv_no_movies);
        searchView = (SearchView) findViewById(R.id.sv);
        search(searchView);


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview.child(USER_ID).child("movies")
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            link = extras.getString("key");

//get the value based on the key
        }

        mDatabaseReference=  database.getReference("AUDIO").child("BROWSE").child("HOLLYWOOD").child("SHOWS").child(link);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

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

                recycler = new RecyclerAdapter(list,getApplicationContext());
                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator( new DefaultItemAnimator());
                mRecyclerView.setAdapter(recycler);
                tvNoMovies.setText("");



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
