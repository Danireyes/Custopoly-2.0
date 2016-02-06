package com.iplusplus.custopoly.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.iplusplus.custopoly.model.MusicPlayerService;

/* This file contains the source code for examples discussed in Tutorials 1-9 of developerglowingpigs YouTube channel.
 *  The source code is for your convenience purposes only. The source code is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/

//---Implement OnSeekBarChangeListener to keep track of seek bar changes ---
public class MusicActivity extends Activity implements OnSeekBarChangeListener {
    Intent serviceIntent;
    private Button buttonPlayStop;

    // -- PUT THE NAME OF YOUR AUDIO FILE HERE...URL GOES IN THE SERVICE
    String strAudioLink = "65.mp3";

    private boolean isOnline;
    private boolean boolMusicPlaying = false;
    TelephonyManager telephonyManager;
    PhoneStateListener listener;

    // --Seekbar variables --
    private SeekBar seekBar;
    private int seekMax;
    private static int songEnded = 0;
    boolean mBroadcastIsRegistered;

    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.glowingpigs.tutorialstreamaudiopart1b.sendseekbar";
    Intent intent;

    // Progress dialogue and broadcast receiver variables
    boolean mBufferBroadcastIsRegistered;
    private ProgressDialog pdBuff = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        try {
            serviceIntent = new Intent(this, MusicPlayerService.class);

            // --- set up seekbar intent for broadcasting new position to service ---
            intent = new Intent(BROADCAST_SEEKBAR);

            initViews();
            setListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // -- Broadcast Receiver to update position of seekbar from service --
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent serviceIntent) {
            updateUI(serviceIntent);
        }
    };

    private void updateUI(Intent serviceIntent) {
        String counter = serviceIntent.getStringExtra("counter");
        String mediamax = serviceIntent.getStringExtra("mediamax");
        String strSongEnded = serviceIntent.getStringExtra("song_ended");
        int seekProgress = Integer.parseInt(counter);
        seekMax = Integer.parseInt(mediamax);
        songEnded = Integer.parseInt(strSongEnded);
        seekBar.setMax(seekMax);
        seekBar.setProgress(seekProgress);
        if (songEnded == 1) {
            buttonPlayStop.setBackgroundResource(R.drawable.play_button);
        }
    }

    // --End of seekbar update code--

    // --- Set up initial screen ---
    private void initViews() {
        buttonPlayStop = (Button) findViewById(R.id.ButtonPlayStop);
        buttonPlayStop.setBackgroundResource(R.drawable.play_button);

        // --Reference seekbar in main.xml
        seekBar = (SeekBar) findViewById(R.id.seekBar01);
    }

    // --- Set up listeners ---
    private void setListeners() {
        buttonPlayStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlayStopClick();
            }
        });
        seekBar.setOnSeekBarChangeListener(this);

    }

    // --- invoked from ButtonPlayStop listener above ----
    private void buttonPlayStopClick() {
        if (!boolMusicPlaying) {
            buttonPlayStop.setBackgroundResource(R.drawable.pause_button);
            playAudio();
            boolMusicPlaying = true;
        } else {
            if (boolMusicPlaying) {
                buttonPlayStop.setBackgroundResource(R.drawable.pause_button);
                stopMyPlayService();
                boolMusicPlaying = false;
            }
        }
    }

    // --- Stop service (and music) ---
    private void stopMyPlayService() {
        // --Unregister broadcastReceiver for seekbar
        if (mBroadcastIsRegistered) {
            try {
                unregisterReceiver(broadcastReceiver);
                mBroadcastIsRegistered = false;
            } catch (Exception e) {
                // Log.e(TAG, "Error in Activity", e);
                // TODO Auto-generated catch block

                e.printStackTrace();
                Toast.makeText(

                        getApplicationContext(),

                        e.getClass().getName() + " " + e.getMessage(),

                        Toast.LENGTH_LONG).show();
            }
        }

        try {
            stopService(serviceIntent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        boolMusicPlaying = false;
    }

    // --- Start service and play music ---
    private void playAudio() {

        checkConnectivity();
        if (isOnline) {
            stopMyPlayService();

            serviceIntent.putExtra("sentAudioLink", strAudioLink);

            try {
                startService(serviceIntent);
            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(

                        getApplicationContext(),

                        e.getClass().getName() + " " + e.getMessage(),

                        Toast.LENGTH_LONG).show();
            }

            // -- Register receiver for seekbar--
            registerReceiver(broadcastReceiver, new IntentFilter(
                    MusicPlayerService.BROADCAST_ACTION));
            ;
            mBroadcastIsRegistered = true;

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Network Not Connected...");
            alertDialog.setMessage("Please connect to a network and try again");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            });
            alertDialog.setIcon(R.drawable.ic_launcher);
            buttonPlayStop.setBackgroundResource(R.drawable.play_button);
            alertDialog.show();
        }
    }

    // Handle progress dialogue for buffering...
    private void showPD(Intent bufferIntent) {
        String bufferValue = bufferIntent.getStringExtra("buffering");
        int bufferIntValue = Integer.parseInt(bufferValue);

        // When the broadcasted "buffering" value is 1, show "Buffering"
        // progress dialogue.
        // When the broadcasted "buffering" value is 0, dismiss the progress
        // dialogue.

        switch (bufferIntValue) {
            case 0:
                // Log.v(TAG, "BufferIntValue=0 RemoveBufferDialogue");
                // txtBuffer.setText("");
                if (pdBuff != null) {
                    pdBuff.dismiss();
                }
                break;

            case 1:
                BufferDialogue();
                break;

            // Listen for "2" to reset the button to a play button
            case 2:
                buttonPlayStop.setBackgroundResource(R.drawable.play_button);
                break;

        }
    }

    // Progress dialogue...
    private void BufferDialogue() {

        pdBuff = ProgressDialog.show(MusicActivity.this, "Buffering...",
                "Acquiring song...", true);
    }

    // Set up broadcast receiver
    private BroadcastReceiver broadcastBufferReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent bufferIntent) {
            showPD(bufferIntent);
        }
    };

    private void checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting()
                || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting())
            isOnline = true;
        else
            isOnline = false;
    }

    // -- onPause, unregister broadcast receiver. To improve, also save screen data ---
    @Override
    protected void onPause() {
        // Unregister broadcast receiver
        if (mBufferBroadcastIsRegistered) {
            unregisterReceiver(broadcastBufferReceiver);
            mBufferBroadcastIsRegistered = false;
        }
        super.onPause();
    }


    // -- onResume register broadcast receiver. To improve, retrieve saved screen data ---
    @Override
    protected void onResume() {
        // Register broadcast receiver
        if (!mBufferBroadcastIsRegistered) {
            registerReceiver(broadcastBufferReceiver, new IntentFilter(
                    MusicPlayerService.BROADCAST_BUFFER));
            mBufferBroadcastIsRegistered = true;
        }
        super.onResume();
    }


    // --- When user manually moves seekbar, broadcast new position to service ---
    @Override
    public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        if (fromUser) {
            int seekPos = sb.getProgress();
            intent.putExtra("seekpos", seekPos);
            sendBroadcast(intent);
        }
    }


    // --- The following two methods are alternatives to track seekbar if moved.
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch(view.getId()) {
            case R.id.radioButton:
                if (checked)
                    strAudioLink = "10.mp3";
                serviceIntent = new Intent(this, MusicPlayerService.class);
                intent = new Intent(BROADCAST_SEEKBAR);
                break;
            case R.id.radioButton2:
                if(checked)
                    strAudioLink = "65.mp3";
                serviceIntent = new Intent(this, MusicPlayerService.class);
                intent = new Intent(BROADCAST_SEEKBAR);
                break;
            case R.id.radioButton3:
                if(checked)
                    strAudioLink = "30.mp3";
                serviceIntent = new Intent(this, MusicPlayerService.class);
                intent = new Intent(BROADCAST_SEEKBAR);
                break;
            case R.id.radioButton4:
                if(checked)
                    strAudioLink ="60.mp3";
                serviceIntent = new Intent(this, MusicPlayerService.class);
                intent = new Intent(BROADCAST_SEEKBAR);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Intent back = new Intent(MusicActivity.this, MainActivity.class);
        MusicActivity.this.finish();
        startActivity(back);
    }
}

