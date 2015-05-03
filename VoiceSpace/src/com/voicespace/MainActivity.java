package com.voicespace;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
	private TextView t; //, t2;
//	private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

//    private PlayButton   mPlayButton = null;
//    private MediaPlayer mPlayer = null;

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private String callDate;
    private long last_call_time, current_call_time;
    private String url = "http://posttestserver.com/post.php?dir=VS"; //Have to set this to our server name
    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response;
    
    FileInputStream fstrm;

    HttpPost httppost = new HttpPost(url);
    InputStreamEntity reqEntity;
//    InputStreamEntity reqEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	call_aircel = (Button)findViewById(R.id.aircel);
    	call_airtel = (Button)findViewById(R.id.airtel);
    	t = (TextView)findViewById(R.id.textbox1);
//    	t2 = (TextView)findViewById(R.id.textbox2);
    	t.setText("VoiceSpace");
    	mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.mp3";
        
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
		
		ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        t.setText("Internet Connection.");
		        try {
		            // Set your file path here
		            fstrm = new FileInputStream(mFileName);

		            // Set your server page url (and the file title/description)
//		            HttpFileUpload hfu = new HttpFileUpload(url, "my file title","my file description");

//		            hfu.Send_Now(fstrm);

		          } catch (FileNotFoundException e) {
		        	  
		        	  t.setText("Filen not fougn");
		          }
		        new send_audio().execute(url);
//		        t.setText("doneheee");

		    }
		    else {
		    	t.setText("No internet Connection.");
		    }


    }
    
    private class send_audio extends AsyncTask<String, Void, String> {
        
        URL connectURL;
//        String responseString;
        String Title;
        String Description;
//        byte[ ] dataToServer;
        FileInputStream fileInputStream = null;
        String urlString, vTitle, vDesc;
//        TextView t2;
		@Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
//                return downloadUrl(urls[0]);
//			t.setText("here1");
//        	try {	
//				reqEntity = new InputStreamEntity(
//    	            new FileInputStream(mFileName), -1);
//				t.setText("here2");
//        	}
//    		catch(Exception e1)
//    		{
//    			t.setText("Audio not found");
//    		}
//        	try {
//        		t.setText("here3");
//    		    reqEntity.setContentType("binary/octet-stream");
//    		    reqEntity.setChunked(true); // Send in multiple parts if needed
//    		    httppost.setEntity(reqEntity);
//    		    response = httpclient.execute(httppost);
//    		    //Do something with response...
//    		    t.setText("done");
//    		    t.setText(response.toString());
//
//    		} catch (Exception e) {
//    		    // show error
//    			t.setText(e.toString());
//    		}
    		
    		//Here starts the new code
//			t2 = (TextView)findViewById(R.id.textbox2);
			urlString = url;
	        vTitle = "my file title";
			vDesc = "my file title";
    		try{
                connectURL = new URL(urlString);
                Title= vTitle;
                Description = vDesc;
    			}
			
    		catch(Exception ex){
            Log.i("HttpFileUpload","URL Malformatted");
			}
    		
    		fileInputStream = fstrm;
            //Sending(); below is the function
    		String iFileName = "audiorecordtest.mp3";
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String Tag="fSnd";
//            obj = new notify("Constructor");
//            toast.show();
            try
            {
                    Log.e(Tag," Postdata Starting Http File Sending to URL");

//                    t2.setText("Sending Pdata");
                    // Open a HTTP connection to the URL
                    HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

                    Log.e(Tag," Postdata Starting Http File Sending to URL2");
                    // Allow Inputs
                    conn.setDoInput(true);

                    Log.e(Tag," Postdata Starting Http File Sending to URL3");
                    // Allow Outputs
                    conn.setDoOutput(true);

                    Log.e(Tag," Postdata Starting Http File Sending to URL4");
                    // Don't use a cached copy.
                    conn.setUseCaches(false);

                    Log.e(Tag," Postdata Starting Http File Sending to URL5");
                    // Use a post method.
                    conn.setRequestMethod("POST");

                    conn.setRequestProperty("Connection", "Keep-Alive");

                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

                    Log.e(Tag," Postdata Starting Http File Sending to URL6");
                    
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//                    t2.setText("passed");
                    Log.e(Tag," Postdata Starting Http File Sending to URL7");
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(Title);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    
                    Log.e(Tag," Postdata Starting Http File Sending to URL8");
                    
                    dos.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(Description);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                        
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName +"\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    Log.e(Tag,"Postdata Headers are written");

                    // create a buffer of maximum size
                    int bytesAvailable = fileInputStream.available();
                        
                    int maxBufferSize = 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[ ] buffer = new byte[bufferSize];

                    // read file and write it into form...
                    int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0)
                    {
                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable,maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,bufferSize);
                    }
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // close streams
                    fileInputStream.close();
                        
                    dos.flush();
                        
                    Log.e(Tag,"Postdata File Sent, Response: "+String.valueOf(conn.getResponseCode()));
//                    obj.success(String.valueOf(conn.getResponseCode()));    
                    
                    InputStream is = conn.getInputStream();
                        
                    // retrieve the response from server
                    int ch;

                    StringBuffer b =new StringBuffer();
                    while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
                    String s=b.toString();
                    Log.i("Postdata Response",s);
                    dos.close();
//                    t2.setText("Done bro final ;)");
//                    t2.setText(String.valueOf(conn.getResponseCode()));
            }
            catch (MalformedURLException ex)
            {
                    Log.e(Tag, "Postdata URL error: " + ex.getMessage(), ex);
//                    t2.setText("dijanx");
//                    obj.failure();
            }

            catch (IOException ioe)
            {
                    Log.e(Tag, "Postdata IO error: " + ioe.getMessage(), ioe);
//                    t2.setText("Io error");
//                    obj.failure();
            }
            catch (Exception e){
            	Log.e(Tag, "Postdata  last exception");
//            	t2.setText(e.getMessage());
            }
    		
    		
    		return "haha".toString();
        }
        
        @Override
        protected void onPostExecute(String result) {
//        	t.setText("Sending successful");
        }
//
//        // onPostExecute displays the results of the AsyncTask.
////        @Override
////        protected void onPostExecute(String result) {
////            t.setText("Sending successful");
////       }
    }

//    public void AudioRecordTest() {
//        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        mFileName += "/audiorecordtest.3gp";
//    }
}
