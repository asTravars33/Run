package com.wangjessica.jwlab08b;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class AvatarFragment extends Fragment {
    // Layout components
    EditText numObjs;
    SeekBar speed;
    int delay = 800;
    Button applyColor;
    TextView colorMsg;
    AvatarView avatar;
    String lastColor;
    String selectedComp;
    Spinner sp;
    AvatarPainter painter;
    HashMap<String, String> labelToTag = new HashMap<String, String>();
    Map<String, String> avatarCols;
    Button[] colorButtons = new Button[7]; // ONE INDEXED
    public static AvatarFragment newInstance(Map<String, String> avatarCols){
        AvatarFragment fragment = new AvatarFragment();
        fragment.avatarCols = avatarCols;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.avatar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Buttons and listeners
        colorButtons[1] = view.findViewById(R.id.minicol1);
        colorButtons[2] = view.findViewById(R.id.minicol2);
        colorButtons[3] = view.findViewById(R.id.minicol3);
        colorButtons[4] = view.findViewById(R.id.minicol4);
        colorButtons[5] = view.findViewById(R.id.minicol5);
        colorButtons[6] = view.findViewById(R.id.minicol6);
        for(Button b: colorButtons){
            if(b==null) continue;
            b.setOnClickListener(this::colorAvatar);
        }
        applyColor = view.findViewById(R.id.current);
        applyColor.setOnClickListener(this::updateAvatar);
        // Spinner
        sp = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.components, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedComp = labelToTag.get(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Avatar
        avatar = view.findViewById(R.id.avatar);
        avatar.colors = avatarCols;
        // TextView
        colorMsg = view.findViewById(R.id.col_message);
        // Settings panel
        numObjs = view.findViewById(R.id.num_objs);
        speed = view.findViewById(R.id.speed_control);
        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                delay = -i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // label to tag
        labelToTag.put("Head", "head");
        labelToTag.put("Body", "body");
        labelToTag.put("Left Foot", "foot1");
        labelToTag.put("Right Foot", "foot2");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        painter = (AvatarPainter) context;
    }

    public void colorAvatar(View view){
        lastColor = ((Button)view).getTag().toString();
        colorMsg.setText("Apply color "+lastColor+" to:");
    }
    public void updateAvatar(View view){
        avatar.changeColor(selectedComp, lastColor);
        painter.updateAvatarColors(avatar.colors);
    }
    public int getObjCnt(){
        return Integer.parseInt(numObjs.getText().toString());
    }
    public boolean getMusic(){
        return true;

    }
    // Interfaces
    public interface AvatarPainter{
        void updateAvatarColors(Map<String, String> colors);
    }
}
