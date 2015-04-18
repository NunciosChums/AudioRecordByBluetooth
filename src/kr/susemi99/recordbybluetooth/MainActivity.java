package kr.susemi99.recordbybluetooth;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity
{
  private AudioManager audioManager;
  private MediaRecorder recorder;
  private TextView textFilePath, textStatus;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    
    textFilePath = (TextView) findViewById(R.id.text_file_path);
    textStatus = (TextView) findViewById(R.id.text_status);
    
    findViewById(R.id.btn_record).setOnClickListener(clickListener);
    findViewById(R.id.btn_stop).setOnClickListener(clickListener);
  }
  
  
  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    stopRecord();
  }
  
  
  private void startRecord()
  {
    File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/AudioRecordTest");
    if (!path.exists())
      path.mkdirs();
    
    stopRecord();
    
    try
    {
      File file = File.createTempFile("audio_", ".m4a", path);
      
      audioManager.startBluetoothSco();
      recorder = new MediaRecorder();
      recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
      recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
      recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      recorder.setOutputFile(file.toString());
      recorder.prepare();
      recorder.start();
      
      textFilePath.setText(file.getAbsolutePath());
      textStatus.setText("recording");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  private void stopRecord()
  {
    try
    {
      audioManager.stopBluetoothSco();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    try
    {
      recorder.stop();
      recorder.release();
      recorder = null;
      
      textStatus.setText("stopped");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_record)
        startRecord();
      else if (v.getId() == R.id.btn_stop)
        stopRecord();
    }
  };
}
