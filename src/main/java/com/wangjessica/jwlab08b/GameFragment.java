package com.wangjessica.jwlab08b;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameFragment extends Fragment {
    String season;
    GameView game;
    Map<String, String> avatarCols;
    PointUpdater updater;
    int objCnt;
    int delay;
    public static GameFragment newInstance(String season, Map<String, String> avatarCols, int objCnt, int delay){
        GameFragment fragment = new GameFragment();
        fragment.season = season;
        fragment.avatarCols = avatarCols;
        fragment.objCnt = objCnt;
        fragment.delay = delay;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.minigame, container, false);
        game = view.findViewById(R.id.scene);
        game.avatar = avatarCols;
        game.season = season;
        game.objCnt = objCnt;
        game.delay = delay;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Play music
        MediaPlayer player = MediaPlayer.create(getContext(), getResources().getIdentifier(season, "raw", "com.wangjessica.jwlab08b"));
        player.start();
        // Continuously update points
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (game.gainedPoints) {
                                System.out.println("Points gained");
                                updater.updatePoints(game.score);
                                game.gainedPoints = false;
                                game.score = 0;
                            }
                        }
                    });
                }
            }
        }, 500, 500);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        updater = (PointUpdater) context;
    }

    public void moveLeft(){
        game.x-=0.1;
        game.invalidate();
    }
    public void moveRight(){
        game.x+=0.1;
        game.invalidate();
    }
    public interface PointUpdater{
        void updatePoints(int newPnts);
    }
}
