package com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.BollywoodMovies;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.BollywoodShows;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.BollywoodSingers;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.BrowseCategories;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.BollywoodActors;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.Haryanvi;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.HollywoodActors;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.HollywoodMovies;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.HollywoodShows;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.HollywoodSingers;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.Other;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.PunjabiActors;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.PunjabiMovies;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.PunjabiSingers;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.categories.Youtubers;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HITESH on 9/6/2017.
 */
public class RecyclerAdapterCategory extends RecyclerView.Adapter<RecyclerAdapterCategory.MyHolder> implements Filterable {

    String link1;
    List<Category> listdata;
    Context context;
    private List<Category> mFilteredList;
    private LayoutInflater inflater;
    FirebaseStorage storage;
    StorageReference pathReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private static final String USER_ID = "53";
    File localFile;
    File dest;

    //File dest =new File(Environment.getExternalStoragePublicDirectory
    //       (Environment.DIRECTORY_DOWNLOADS), "audio1.mp3");

    StorageReference pathRef;
    String mystring1[];
    String a;




    public RecyclerAdapterCategory(List<Category> listdata,Context context  ) {
        this.listdata = listdata;
        this.context= context;
        this.mFilteredList=listdata;
        inflater=LayoutInflater.from(context);
        //this.storageReference=storageReference;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_board_item, parent, false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }


    public void onBindViewHolder(MyHolder holder, int position) {
        Category data = listdata.get(position);
        holder.setData(data , position);
        holder.setListeners();


    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    listdata=mFilteredList ;
                } else {

                    List<Category> filteredList = new ArrayList<>();

                    for (Category androidVersion : mFilteredList) {

                        if (androidVersion.getCategoryName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    listdata = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listdata;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listdata = (List<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cname;
        ImageView imageView;
        Category data;
        int pos;

        public MyHolder(View itemView) {
            super(itemView);
            cname = (TextView) itemView.findViewById(R.id.category_name);
            imageView=(ImageView)itemView.findViewById(R.id.category_poster);


        }
        public void setData(Category data ,int postion){
            this.cname.setText(data.getCategoryName());


            this.data=data;
            String posterLink =data.getCategoryPoster();
            this.pos=postion;
            Picasso.with(context).load(posterLink).into(this.imageView);


        }
        public void setListeners() {
            imageView.setOnClickListener(RecyclerAdapterCategory.MyHolder.this);
            cname.setOnClickListener(RecyclerAdapterCategory.MyHolder.this);
        }



        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.category_poster:
                    // Toast.makeText(view.getContext(),data.getMovieName(),Toast.LENGTH_SHORT).show();
                   // Toast.makeText(view.getContext(),"Loading...",Toast.LENGTH_SHORT).show();

                    mystring1 =new String[] {"BROWSE","BROWSEBOLLYWOOD","BROWSEHOLLYWOOD","BROWSEPUNJABI"} ;
                    String x = data.getCategoryId();

                    if(Arrays.asList(mystring1).contains(x)){
                        String link = data.getCategoryLinker();
                        Intent intent = new Intent(context,BrowseCategories.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if(data.getCategoryId().equals("BROWSEBOLLYWOODACTORS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodActors.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if(data.getCategoryId().equals("BROWSEBOLLYWOODSHOWS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodShows.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                    else if (data.getCategoryId().equals("BROWSEBOLLYWOODMOVIES")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodMovies.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEBOLLYWOODSINGERS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodSingers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if(data.getCategoryId().equals("BROWSEHOLLYWOODACTORS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodActors.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if (data.getCategoryId().equals("BROWSEHOLLYWOODMOVIES")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodMovies.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEHOLLYWOODSINGERS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodSingers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }

                    else if (data.getCategoryId().equals("BROWSEHOLLYWOODSHOWS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodShows.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEHARYANVI")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,Haryanvi.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEOTHER")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,Other.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if(data.getCategoryId().equals("BROWSEPUNJABIACTORS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,PunjabiActors.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if(data.getCategoryId().equals("BROWSEPUNJABISINGERS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,PunjabiSingers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                    else if (data.getCategoryId().equals("BROWSEPUNJABIMOVIES")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,PunjabiMovies.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if (data.getCategoryId().equals("BROWSEYOUTUBERS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,Youtubers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

/*
                String link2 = data.getCategoryName();
                String link3 = data.getCategoryName();
                Intent intent = new Intent(context,BrowseCategories.class);
                intent.putExtra("key",link2);
                intent.putExtra("key1",link3);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
                break;
*/
                    break;
                case R.id.category_name:
                    // Toast.makeText(view.getContext(),data.getMovieName(),Toast.LENGTH_SHORT).show();
                    // Toast.makeText(view.getContext(),"Loading...",Toast.LENGTH_SHORT).show();

                    mystring1 =new String[] {"BROWSE","BROWSEBOLLYWOOD","BROWSEHOLLYWOOD","BROWSEPUNJABI"} ;
                    String x1 = data.getCategoryId();
                    if(Arrays.asList(mystring1).contains(x1)){
                        String link = data.getCategoryLinker();
                        Intent intent = new Intent(context,BrowseCategories.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if(data.getCategoryId().equals("BROWSEBOLLYWOODACTORS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodActors.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if(data.getCategoryId().equals("BROWSEBOLLYWOODSHOWS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodShows.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                    else if (data.getCategoryId().equals("BROWSEBOLLYWOODMOVIES")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodMovies.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEBOLLYWOODSINGERS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,BollywoodSingers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if(data.getCategoryId().equals("BROWSEHOLLYWOODACTORS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodActors.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if (data.getCategoryId().equals("BROWSEHOLLYWOODMOVIES")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodMovies.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEHOLLYWOODSINGERS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodSingers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }

                    else if (data.getCategoryId().equals("BROWSEHOLLYWOODSHOWS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,HollywoodShows.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEHARYANVI")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,Haryanvi.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if (data.getCategoryId().equals("BROWSEOTHER")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,Other.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                    }
                    else if(data.getCategoryId().equals("BROWSEPUNJABIACTORS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,PunjabiActors.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if(data.getCategoryId().equals("BROWSEPUNJABISINGERS")){
                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,PunjabiSingers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                    else if (data.getCategoryId().equals("BROWSEPUNJABIMOVIES")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,PunjabiMovies.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }
                    else if (data.getCategoryId().equals("BROWSEYOUTUBERS")){

                        String link = data.getCategoryLinker();

                        Intent intent = new Intent(context,Youtubers.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

/*
                String link2 = data.getCategoryName();
                String link3 = data.getCategoryName();
                Intent intent = new Intent(context,BrowseCategories.class);
                intent.putExtra("key",link2);
                intent.putExtra("key1",link3);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
                break;
*/
                    break;
            }
        }
    }



}
