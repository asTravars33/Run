package com.wangjessica.jwlab08b;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity<Seekbar> extends AppCompatActivity implements AvatarFragment.AvatarPainter, GameFragment.PointUpdater{
    FrameLayout frame;
    int points = 0;
    TextView pointsView;
    int cur = 0;
    String curTag = "avatar";
    Map<String, String> avatarCols = new HashMap<String, String>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String TAG = "com.wangjessica.jwlab08b.values";
    int objCnt=6;
    int delay=800;
    boolean music=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frame = findViewById(R.id.frame);
        pointsView = findViewById(R.id.points);
        // Transferring avatar colors from shared preferences
        sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String[] info = sharedPreferences.getString("avatar", "head:white;body:white;foot1:white;foot2:white").split(";");
        for(String s: info){
            String[] mininfo = s.split(":");
            avatarCols.put(mininfo[0], mininfo[1]);
        }
        // Setting the initial avatar fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame, AvatarFragment.newInstance(avatarCols), "avatar");
        ft.commit();
    }

    @Override
    public void updateAvatarColors(Map<String, String> colors) {
        avatarCols = colors;
        String pairs = "";
        for(String part: avatarCols.keySet()){
            pairs+=part+":"+avatarCols.get(part)+";";
        }
        editor.putString("avatar", pairs.substring(0, pairs.length()-1)).apply();
        //for(String s: avatarCols.keySet()) System.out.println(s+" "+avatarCols.get(s));
    }

    public void switchFrag(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AvatarFragment frag = (AvatarFragment) getSupportFragmentManager().findFragmentByTag("avatar");
        if(frag!=null) {
            objCnt = frag.getObjCnt();
            delay = frag.delay;
            music = frag.getMusic();
        }
        System.out.println(objCnt+" "+delay);
        switch(cur){
            case 0:
                ft.replace(R.id.frame, GameFragment.newInstance("winter", avatarCols, objCnt, delay), "winter");
                curTag = "winter";
                break;
            case 1:
                ft.replace(R.id.frame, GameFragment.newInstance("spring", avatarCols, objCnt, delay), "spring");
                curTag = "spring";
                break;
            case 2:
                ft.replace(R.id.frame, GameFragment.newInstance("summer", avatarCols, objCnt, delay), "summer");
                curTag = "summer";
                break;
            case 3:
                ft.replace(R.id.frame, GameFragment.newInstance("fall", avatarCols, objCnt, delay), "fall");
                curTag = "fall";
                break;
        }
        cur++;
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        GameFragment frag = (GameFragment) getSupportFragmentManager().findFragmentByTag(curTag);
        switch(keyCode){
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                frag.moveRight();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                frag.moveLeft();
                break;
        }
        return true;
    }

    @Override
    public void updatePoints(int newPnts) {
        points+=newPnts;
        pointsView.setText("Points: "+points);
    }
}