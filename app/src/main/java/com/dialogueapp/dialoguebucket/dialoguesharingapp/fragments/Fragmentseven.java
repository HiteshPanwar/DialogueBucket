package com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter.RecyclerAdapterExclusive;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Category;

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

public class Fragmentseven extends Fragment {
    List<Category> list;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
     ProgressBar progress;
    SearchView searchView;
    ImageView imageView;
    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    RecyclerAdapterExclusive recycler;
    Button shareApp;



    public Fragmentseven() {
        Log.i("Fragment check", "Fragment seven Created");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exclusive, container, false);



        shareApp = (Button) v.findViewById(R.id.shareApp);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView) v.findViewById(R.id.tv_no_movies);
        searchView = (SearchView) v.findViewById(R.id.sv);
        search(searchView);
        progress = (ProgressBar) v.findViewById(R.id.progressBar);
        imageView = (ImageView) v.findViewById(R.id.imageView);
         getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shareBody = "https://play.google.com/store/apps/details?id=com.dialogueapp.dialoguebucket.dialoguesharingapp";
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.setType("text/plain");
                share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                Intent chooserIntent = Intent.createChooser(share, "Open With");
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(chooserIntent);
            }
        });


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview.child(USER_ID).child("movies")
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatabaseReference = database.getReference("AUDIO").child("EXCLUSIVEDATA");


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

                 recycler = new RecyclerAdapterExclusive(list,getActivity().getApplicationContext());

                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator( new DefaultItemAnimator());
                mRecyclerView.setAdapter(recycler);
                tvNoMovies.setText("");
                progress.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
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