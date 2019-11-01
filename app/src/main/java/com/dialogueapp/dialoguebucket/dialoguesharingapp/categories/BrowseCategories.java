package com.dialogueapp.dialoguebucket.dialoguesharingapp.categories;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter.RecyclerAdapterCategory;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Category;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseCategories extends AppCompatActivity {


    List<Category> list;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    String link;
    SearchView searchView;
    RecyclerAdapterCategory recycler;


    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private static final String USER_ID = "53";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_categories);



//Initializing our Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.my_category_recycler_view);
        tvNoMovies = (TextView) findViewById(R.id.category_name);
        searchView = (SearchView) findViewById(R.id.sv);
        search(searchView);



        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview.child(USER_ID).child("movies")
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        tvNoMovies = (TextView) findViewById(R.id.tv_no_movies);
        mRecyclerView.setLayoutManager(mLayoutManager);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            link = extras.getString("key");
//get the value based on the key
        }



        System.out.println("tttttttttttttthhhhhhhhhhhhhhhhiiiiiiiiiiiiiiiissssssssssssssssssssssssss:"+link);


        mDatabaseReference=  database.getReference("AUDIO").child("BROWSEDATA").child("BROWSE"+link);

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

                recycler = new RecyclerAdapterCategory(list,getApplicationContext());
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
