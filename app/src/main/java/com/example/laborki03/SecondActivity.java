package com.example.laborki03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private int selected_sound = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent received_intent = getIntent();
        Integer sound_id = received_intent.getIntExtra(MainActivity.SOUND_ID,0);
        TextView txV = (TextView)findViewById(R.id.current_sound_text);
        txV.setText(getText(R.string.current_sound_str)+sound_id.toString());
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        if(checked){
            RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
            switch (view.getId()){
                case R.id.sound1: selected_sound = 0; break;
                case R.id.sound2: selected_sound = 1; break;
                case R.id.sound3: selected_sound = 2; break;
                case R.id.sound4: selected_sound = 3; break;
            }
        }
    }

    public void setSoundClick(View v){
        Intent data = new Intent();
        data.putExtra(MainActivity.SOUND_ID,selected_sound);
        setResult(RESULT_OK, data);
        finish();
    }
}
