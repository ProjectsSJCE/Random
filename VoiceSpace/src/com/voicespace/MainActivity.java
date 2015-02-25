package com.voicespace;

import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	private Button call_aircel, call_airtel;
	private TextView t;
//	private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

//    private PlayButton   mPlayButton = null;
//    private MediaPlayer mPlayer = null;

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private String callDate;
    private long last_call_time, current_call_time;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	call_aircel = (Button)findViewById(R.id.aircel);
    	call_airtel = (Button)findViewById(R.id.airtel);
    	t = (TextView)findViewById(R.id.textbox1);
    	t.setText("VoiceSpace");
    	mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        
    	call_aircel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				last_call_time = getLastCall();
				Intent intent = new Intent(Intent.ACTION_CALL); 
				intent.setData(Uri.parse("tel:121,111")); 
				startActivity(intent);
				start_record();
				
			}
		});
        
        call_airtel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				last_call_time = getLastCall();
				Intent intent = new Intent(Intent.ACTION_CALL); 
				intent.setData(Uri.parse("tel:121,111")); 
				startActivity(intent);
				start_record();
			}
		});
 
    }
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }
    
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }
    
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
    
    public long getLastCall()
    {
    	Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
//        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);

        while (managedCursor.moveToNext())
        {
            managedCursor.getString(number);
            callDate = managedCursor.getString(date);

            new Date(Long.valueOf(callDate));

//            callDuration = managedCursor.getString(duration);
        }    
        managedCursor.close();
        
        return Long.valueOf(callDate);
    }
    
    public void start_record()
    {
    	t.setText("Works");
		onRecord(true);
		try {
			Thread.sleep(10000);
		}
		catch(Exception e){
		}
		current_call_time = getLastCall();
		while(current_call_time == last_call_time)
			current_call_time = getLastCall();
		onRecord(false);
		t.setText(mFileName);
    }
//    public void AudioRecordTest() {
//        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        mFileName += "/audiorecordtest.3gp";
//    }
}
