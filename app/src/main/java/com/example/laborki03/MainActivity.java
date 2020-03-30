package com.example.laborki03;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String SOUND_ID = "sound id";
    public static final int BUTTON_REQUEST = 1;
    private int current_sound = 0;
    private int current_sound2 = 0;
    private int last_player = 1;
    private boolean paused = false;

    private MediaPlayer backgroundPlayer;
    private MediaPlayer buttonPlayer;
    static public Uri[] sounds;
    static public Uri[] sounds2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(paused){
                    onResume();
                    paused = false;
                }
                else{
                    backgroundPlayer.pause();
                    paused = true;
                }
            }
        });

        final ImageButton imButton = (ImageButton) findViewById(R.id.face_button);
        imButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random_color = 0xff000000;
                for (int i = 0; i < 3; i++) {
                    int component_color = (int) (255 * Math.random());
                    component_color <<= (8 * i);
                    random_color |= component_color;
                }
                imButton.setColorFilter(random_color);

                buttonPlayer.reset();
                try{
                    buttonPlayer.setDataSource(getApplicationContext(), sounds[current_sound]);
                } catch(IOException e){
                    e.printStackTrace();
                }
                buttonPlayer.prepareAsync();
                last_player = 1;
            }
        });
        imButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent soundPick = new Intent(getApplicationContext(), SecondActivity.class);
                soundPick.putExtra(SOUND_ID, current_sound);
                startActivityForResult(soundPick, BUTTON_REQUEST);
                return true;
            }
        });

        final ImageButton secondButton = (ImageButton) findViewById(R.id.second_button);
        secondButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                buttonPlayer.reset();
                try{
                    buttonPlayer.setDataSource(getApplicationContext(), sounds2[current_sound2]);
                } catch(IOException e){
                    e.printStackTrace();
                }
                buttonPlayer.prepareAsync();
                last_player = 2;
            }
        });
        secondButton.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Intent soundPick = new Intent(getApplicationContext(), SecondActivity.class);
                soundPick.putExtra(SOUND_ID, current_sound2);
                startActivityForResult(soundPick, BUTTON_REQUEST);
                return true;
            }
        });

        sounds = new Uri[4];
        sounds[0] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ringd);
        sounds[1] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring01);
        sounds[2] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring02);
        sounds[3] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring03);

        sounds2 = new Uri[4];
        sounds2[0] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.drugi1);
        sounds2[1] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.drugi2);
        sounds2[2] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.drugi3);
        sounds2[3] = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.drugi4);

        buttonPlayer = new MediaPlayer();
        buttonPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        buttonPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                backgroundPlayer.pause();
                paused = true;
                mp.start();
            }
        });

        buttonPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                backgroundPlayer.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BUTTON_REQUEST) {
                switch(last_player){
                    case 1:
                        current_sound = data.getIntExtra(SOUND_ID, 0);
                        break;
                    case 2:
                        current_sound2 = data.getIntExtra(SOUND_ID, 0);
                        break;
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), getText(R.string.back_message), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundPlayer.pause();
        paused = true;
        buttonPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch(last_player){
            case 1:
                backgroundPlayer = MediaPlayer.create(this, R.raw.mario);
                break;
            case 2:
                backgroundPlayer = MediaPlayer.create(this, R.raw.xx);
                break;
        }
        paused = false;
        backgroundPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        backgroundPlayer.release();
    }
}