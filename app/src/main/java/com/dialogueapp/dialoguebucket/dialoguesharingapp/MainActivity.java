package com.dialogueapp.dialoguebucket.dialoguesharingapp;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.adapter.FragmentAdapter;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmenteight;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmentfive;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmentfour;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmentone;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmentseven;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmentsix;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmentthree;
import com.dialogueapp.dialoguebucket.dialoguesharingapp.fragments.Fragmenttwo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;




import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{


    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    TabLayout tab;
    ViewPager viewPager;

        Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);




        int perm = ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perm == PackageManager.PERMISSION_GRANTED) {
            initialize();
            prepareDataResources();
            FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
            viewPager.setAdapter(adapter);
            tab.setupWithViewPager(viewPager);
        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    45
            );
        }
        //Initializing our Recyclerview



    }

    private void initialize() {

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tab = (TabLayout) findViewById(R.id.tab);
    }

    private void prepareDataResources() {
        addData(new Fragmentseven(),"HOT");
        addData(new Fragmentsix(),"CATEGORIES");
        addData(new Fragmentfour(),"FAMOUS");
        addData(new Fragmenttwo(), "EVERGREEN");
        addData(new Fragmentone(), "TRENDING");
        addData(new Fragmentfive(),"DAILY");
        addData(new Fragmentthree(), "NEW");
        addData(new Fragmenteight(),"APP");



    }

    private void addData(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);


    }






    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {



        if (requestCode == 45) { //read request
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialize();
                prepareDataResources();
                FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
                viewPager.setAdapter(adapter);
                tab.setupWithViewPager(viewPager);
            } else {
                Toast.makeText(this, "Sharing Dialogues required this permission", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setMessage("We need this permission to read the audio file.\n" +
                                "Please allow this permission")
                        .setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                        45
                                );
                            }
                        })
                        .setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Sorry! this app needs permissions to run", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
            }
        }
    }

}