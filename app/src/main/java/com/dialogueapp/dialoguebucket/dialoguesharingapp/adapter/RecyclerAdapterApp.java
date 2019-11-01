package com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HITESH on 9/6/2017.
 */
public class RecyclerAdapterApp extends RecyclerView.Adapter<RecyclerAdapterApp.MyHolder> implements Filterable {

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




    public RecyclerAdapterApp(List<Category> listdata,Context context  ) {
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
            imageView.setOnClickListener(RecyclerAdapterApp.MyHolder.this);
        }



        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.category_poster:
                    // Toast.makeText(view.getContext(),data.getMovieName(),Toast.LENGTH_SHORT).show();
                    // Toast.makeText(view.getContext(),"Loading...",Toast.LENGTH_SHORT).show();



                    /*
                        String link = data.getCategoryLinker();
                        Intent intent = new Intent(context,BrowseCategories.class);
                        intent.putExtra("key",link);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

*/

                    break;
            }
        }
    }



}
