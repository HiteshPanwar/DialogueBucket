package com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dialogueapp.dialoguebucket.dialoguesharingapp.R;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.models.Dialogue;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HITESH on 9/1/2017.
 */
public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> implements Filterable {
    String link,storageLinker,filepath="null";
    List<Dialogue> listdata;
    Context context;

    private LayoutInflater inflater;
    FirebaseStorage storage;
    StorageReference pathReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private static final String USER_ID = "53";
    File localFile,localFile1;
    File dest;
    MediaPlayer mediaPlayer;
    ImageButton audioButton;
    //File dest =new File(Environment.getExternalStoragePublicDirectory
    //       (Environment.DIRECTORY_DOWNLOADS), "audio1.mp3");

    StorageReference pathRef;
    private List<Dialogue> mFilteredList;









    public RecyclerAdapter(List<Dialogue> listdata,Context context  ) {
        this.listdata = listdata;
        this.context= context;
        this.mFilteredList=listdata;
        inflater=LayoutInflater.from(context);
        //this.storageReference=storageReference;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_board_item, parent, false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;













    }


    public void onBindViewHolder(MyHolder holder, int position) {
        Dialogue data = listdata.get(position);
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

                    List<Dialogue> filteredList = new ArrayList<>();

                    for (Dialogue androidVersion : mFilteredList) {

                        if (androidVersion.getDialogueName().toLowerCase().contains(charString)||androidVersion.getMovieName().toLowerCase().contains(charString)) {

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
                listdata = (List<Dialogue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView vname,dname;
        ImageView imageView;
        ImageButton play,share,whatsAppshare,messengerShareButton;
        Dialogue data;
        int pos,activePlay=0;

        public MyHolder(View itemView) {
            super(itemView);
            vname = (TextView) itemView.findViewById(R.id.tv_name);
            imageView=(ImageView)itemView.findViewById(R.id.iv_movie_poster);
            dname = (TextView) itemView.findViewById(R.id.d_name);
            play = (ImageButton) itemView.findViewById(R.id.playButton);
            share = (ImageButton) itemView.findViewById(R.id.shareButton);
            whatsAppshare = (ImageButton) itemView.findViewById(R.id.whatsAppShareButton);
            messengerShareButton = (ImageButton) itemView.findViewById(R.id.messengerShareButton);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    link=data.getMovieName();

                    System.out.println(link);
                    Toast.makeText(view.getContext(),data.getMovieName(),Toast.LENGTH_SHORT).show();
                }
            });*/


        }
        public void setData(Dialogue data ,int postion){
            this.vname.setText(data.getMovieName());
            this.dname.setText(data.getDialogueName());

            this.data=data;
            String posterLink =data.getDialoguePoster();
            this.pos=postion;
            Picasso.with(context).load(posterLink).into(this.imageView);


        }
        public void setListeners() {
            imageView.setOnClickListener(MyHolder.this);
            play.setOnClickListener(MyHolder.this);
            share.setOnClickListener(MyHolder.this);
            whatsAppshare.setOnClickListener(MyHolder.this);
            messengerShareButton.setOnClickListener(MyHolder.this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {


                case R.id.playButton:
                    //Toast.makeText(view.getContext(),data.getDialogueName(),Toast.LENGTH_SHORT).show();

                    if(activePlay==1&& mediaPlayer != null && mediaPlayer.isPlaying())
                    {mediaPlayer.stop();

                        ImageButton rotateImage = (ImageButton) view.findViewById(R.id.playButton);
                        Animation startRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_stop_rotate);
                        rotateImage.startAnimation(startRotateAnimation);
                        rotateImage.setImageResource(R.drawable.play);
                        activePlay=0;}
                    else {
                        Toast.makeText(view.getContext(),"Playing...",Toast.LENGTH_SHORT).show();
                        link = data.getDialogueId();
                        storageLinker = data.getStorageLinker();

                        ImageButton rotateImage = (ImageButton) view.findViewById(R.id.playButton);
                        Animation startRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation);
                        rotateImage.startAnimation(startRotateAnimation);
                        rotateImage.setImageResource(R.drawable.play);




                        pathRef = FirebaseStorage.getInstance().getReference(storageLinker+link+".mp3");
                        activePlay=1;
                        //getaudioPlay(view);
                        playAudio(pathRef,view);
                    }



                    break;
                case R.id.shareButton:
                    Toast.makeText(view.getContext(),"Sharing...",Toast.LENGTH_LONG).show();

                    link = data.getDialogueId();
                    storageLinker = data.getStorageLinker();
                    ImageButton shareButton = (ImageButton) view.findViewById(R.id.shareButton);
                    Animation startRotateAnimationShareButton = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation_share);
                    shareButton.startAnimation(startRotateAnimationShareButton);
                    pathRef = FirebaseStorage.getInstance().getReference(storageLinker+link+".mp3");
                    Long tsLongShare = System.currentTimeMillis()/1000;
                    String tsShare = "-"+tsLongShare.toString();
                    dest =new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS),tsShare+".mp3");
                    System.out.println(dest);
                    getaudioShare(view);
                    break;

                case R.id.whatsAppShareButton:
                    Toast.makeText(view.getContext(),"Sharing...",Toast.LENGTH_LONG).show();

                    link = data.getDialogueId();
                    storageLinker = data.getStorageLinker();
                    ImageButton whatsaAppButton = (ImageButton) view.findViewById(R.id.whatsAppShareButton);
                    Animation startRotateAnimationWhatsAppButton = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation_share);
                    whatsaAppButton.startAnimation(startRotateAnimationWhatsAppButton);
                    pathRef = FirebaseStorage.getInstance().getReference(storageLinker+link+".mp3");
                    Long tsLongWhatsAppShare = System.currentTimeMillis()/1000;
                    String tsWhatsAppShare = "-"+tsLongWhatsAppShare.toString();
                    dest =new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS),tsWhatsAppShare+".mp3");
                    System.out.println(dest);
                    getaudioWhatsAppShare(view);


                    break;
                case R.id.messengerShareButton:
                    Toast.makeText(view.getContext(),"Sharing...",Toast.LENGTH_LONG).show();

                    link = data.getDialogueId();
                    storageLinker = data.getStorageLinker();
                    ImageButton messengerShareButton = (ImageButton) view.findViewById(R.id.messengerShareButton);
                    Animation startRotateAnimationmessengerShareButton = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation_share);
                    messengerShareButton.startAnimation(startRotateAnimationmessengerShareButton);
                    pathRef = FirebaseStorage.getInstance().getReference(storageLinker+link+".mp3");
                    Long tsLongMessengerShare = System.currentTimeMillis()/1000;
                    String tsMessengerShare = "-"+tsLongMessengerShare.toString();
                    dest =new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS),tsMessengerShare+".mp3");
                    System.out.println(dest);
                    getaudioMessengerShare(view);


                    break;

            }
        }
    }



    /*public void getaudioPlay(final View v){

        final ImageButton audioButton1=(ImageButton)v.findViewById(R.id.playButton);
        filepath="null";
        try {
            localFile = File.createTempFile("audio","mp3");


            pathRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    filepath=localFile.getAbsolutePath();

                    int file_size = Integer.parseInt(String.valueOf(localFile.length()));
                    if(file_size==0||filepath.equals("null")){System.out.println("path/file not there ");}

                    else{
                        playAudio(filepath,v);}
                    audioButton1.setImageResource(R.drawable.play);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //handle error

                    Toast.makeText(v.getContext(), e.getMessage()+"\n"+e.getCause(), Toast.LENGTH_LONG).show();

                }
            });
        }catch (IOException exception){
            Toast.makeText(v.getContext(), "IOEXCEPTION", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void getaudioShare(final View v){

        try {
            localFile = File.createTempFile("audio","mp3");


            pathRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                  //  Toast.makeText(v.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    copybutton(v);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //handle error
                    Toast.makeText(v.getContext(), e.getMessage()+"\n"+e.getCause(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (IOException exception){
            Toast.makeText(v.getContext(), "IOEXCEPTION", Toast.LENGTH_SHORT).show();
        }
    }

    public void playAudio(final StorageReference filePath, final View view){


        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {mediaPlayer.stop();}



                mediaPlayer = new  MediaPlayer();
                try {
                    mediaPlayer.setDataSource(String.valueOf(downloadUrl));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(mediaPlayer !=null) {
                    mediaPlayer.start();
                }
                long totalDuration = mediaPlayer.getDuration();
                rotate(totalDuration,view);
            }
        });



    }










    public void copybutton(View v)
    {


        File source = new File(localFile.getAbsolutePath());


        File destination = new File(dest.getAbsolutePath());
        try
        {
            FileUtils.copyFile(source, destination);

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        dest = destination.getAbsoluteFile();
        System.out.println(dest.getAbsolutePath());
        System.out.println(destination.getAbsolutePath());
        // Bitmap bitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
        //imageView2.setImageBitmap(bitmap);
        onClickMeShare(v);
    }


    public void onClickMeShare(View view){

        // System.out.println(localFile.getAbsolutePath());
        //   System.out.println(dest.getAbsoluteFile());

        // Toast.makeText(MainActivity.this,localFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
//for share

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Uri uri = Uri.fromFile(dest);

        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", dest);
        System.out.println(uri);

        share.putExtra(Intent.EXTRA_STREAM,uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.setType("audio/mp3");
        //context.startActivity(Intent.createChooser(share, "Share Audio"));

        Intent chooserIntent = Intent.createChooser(share, "Open With");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        view.getContext().startActivity(chooserIntent);

    }

    public void getaudioWhatsAppShare(final View v){

        try {
            localFile = File.createTempFile("audio","mp3");


            pathRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                   // Toast.makeText(v.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    copybuttonWhatsApp(v);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //handle error
                    Toast.makeText(v.getContext(), e.getMessage()+"\n"+e.getCause(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (IOException exception){
            Toast.makeText(v.getContext(), "IOEXCEPTION", Toast.LENGTH_SHORT).show();
        }
    }


    public void getaudioMessengerShare(final View v){

        try {
            localFile = File.createTempFile("audio","mp3");


            pathRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    // Toast.makeText(v.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    copybuttonMessenger(v);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //handle error
                    Toast.makeText(v.getContext(), e.getMessage()+"\n"+e.getCause(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (IOException exception){
            Toast.makeText(v.getContext(), "IOEXCEPTION", Toast.LENGTH_SHORT).show();
        }
    }

    public void copybuttonWhatsApp(View v)
    {


        File source = new File(localFile.getAbsolutePath());


        File destination = new File(dest.getAbsolutePath());
        try
        {
            FileUtils.copyFile(source, destination);

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        dest = destination.getAbsoluteFile();
        System.out.println(dest.getAbsolutePath());
        System.out.println(destination.getAbsolutePath());
        // Bitmap bitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
        //imageView2.setImageBitmap(bitmap);
        onClickMeWhatsAppShare(v);
    }


    public void copybuttonMessenger(View v)
    {


        File source = new File(localFile.getAbsolutePath());


        File destination = new File(dest.getAbsolutePath());
        try
        {
            FileUtils.copyFile(source, destination);

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        dest = destination.getAbsoluteFile();
        System.out.println(dest.getAbsolutePath());
        System.out.println(destination.getAbsolutePath());
        // Bitmap bitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
        //imageView2.setImageBitmap(bitmap);
        onClickMeMessengerShare(v);
    }


    public void onClickMeWhatsAppShare(View view){
    /*    Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.parse(dest.getAbsolutePath());
        share.putExtra(Intent.EXTRA_STREAM,uri);
        share.setType("audio/mp3");
        //context.startActivity(Intent.createChooser(share, "Share Audio"));
        share.setPackage("com.whatsapp");
        view.getContext().startActivity(share);  */
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Uri uri = Uri.fromFile(dest);

        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", dest);



        share.putExtra(Intent.EXTRA_STREAM,uri);
        share.setType("audio/mp3");
        //context.startActivity(Intent.createChooser(share, "Share Audio"));

        Intent chooserIntent = Intent.createChooser(share, "Open With");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        view.getContext().startActivity(chooserIntent);



    }
    public void onClickMeMessengerShare(View view){
     /*   Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.parse(dest.getAbsolutePath());
        share.putExtra(Intent.EXTRA_STREAM,uri);
        share.setType("audio/mp3");
        //context.startActivity(Intent.createChooser(share, "Share Audio"));
        share.setPackage("com.whatsapp");
        view.getContext().startActivity(share);  */



        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
       // Uri uri = Uri.fromFile(dest);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", dest);

        share.putExtra(Intent.EXTRA_STREAM,uri);
        share.setType("audio/mp3");
        //context.startActivity(Intent.createChooser(share, "Share Audio"));

        Intent chooserIntent = Intent.createChooser(share, "Open With");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        view.getContext().startActivity(chooserIntent);



    }


    public void rotate(long timeDuration,View view){
        if(timeDuration<=5000) {
            ImageButton rotateImage = (ImageButton) view.findViewById(R.id.playButton);
            Animation startRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation);
            rotateImage.startAnimation(startRotateAnimation);
        }
        else if(timeDuration>5000&&timeDuration<=10000){

            ImageButton rotateImage = (ImageButton) view.findViewById(R.id.playButton);
            Animation startRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation1);
            rotateImage.startAnimation(startRotateAnimation);
        }
        else if(timeDuration>10000&&timeDuration<=15000){

            ImageButton rotateImage = (ImageButton) view.findViewById(R.id.playButton);
            Animation startRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation2);
            rotateImage.startAnimation(startRotateAnimation);
        }
        else if(timeDuration>15000&&timeDuration<=20000){

            ImageButton rotateImage = (ImageButton) view.findViewById(R.id.playButton);
            Animation startRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation3);
            rotateImage.startAnimation(startRotateAnimation);
        }
        else{

            ImageButton rotateImage = (ImageButton) view.findViewById(R.id.playButton);
            Animation startRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.android_rotate_animation4);
            rotateImage.startAnimation(startRotateAnimation);
        }

    }




}
