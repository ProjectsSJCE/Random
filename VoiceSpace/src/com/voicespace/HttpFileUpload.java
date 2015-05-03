package com.voicespace;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpFileUpload implements Runnable{
    URL connectURL;
    String responseString;
    String Title;
    String Description;
    byte[ ] dataToServer;
    FileInputStream fileInputStream = null;
//    Context context = null;// = getApplicationContext();
//    CharSequence text = "Hello toast!";
//    int duration = Toast.LENGTH_SHORT;
//
//    Toast toast = Toast.makeText(context, text, duration);
//    public class notify extends Activity {
//
//    	private TextView t2 = (TextView)findViewById(R.id.textbox2);
//    	
//    	notify( String tex) {
////    		t2.setText(tex);
//    	}
////    	public void success(String text) {
////    		
////    		t2.setText(text);
////    	}
//    //	
////    	public void failure() {
////    		
////    		t2.setText("noo");
////    	}
//
//    }
//    notify obj; // = new notify();

    HttpFileUpload(String urlString, String vTitle, String vDesc){
            try{
                    connectURL = new URL(urlString);
                    Title= vTitle;
                    Description = vDesc;
            }catch(Exception ex){
                Log.i("HttpFileUpload","URL Malformatted");
            }
    }

    void Send_Now(FileInputStream fStream){
            fileInputStream = fStream;
            Sending();
    }

    void Sending(){
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
            }
            catch (MalformedURLException ex)
            {
                    Log.e(Tag, "Postdata URL error: " + ex.getMessage(), ex);
//                    obj.failure();
            }

            catch (IOException ioe)
            {
                    Log.e(Tag, "Postdata IO error: " + ioe.getMessage(), ioe);
//                    obj.failure();
            }
            catch (Exception e){
            	Log.e(Tag, "Postdata  last exception");
            }
    }

    @Override
    public void run() {
            // TODO Auto-generated method stub
    }
}
