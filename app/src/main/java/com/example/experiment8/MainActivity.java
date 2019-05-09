package com.example.experiment8;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView) findViewById(R.id.video_view);
        EditText et_path = (EditText) findViewById(R.id.et_path);
        Button play = (Button) findViewById(R.id.play);
        Button ok = (Button) findViewById(R.id.ok);
        Button pause = (Button) findViewById(R.id.pause);
        Button replay = (Button) findViewById(R.id.replay);
        play.setOnClickListener(this);
        ok.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            String path = et_path.getText().toString().trim();
            initVideoPath(path); //初始化VideoView
        }
    }

    protected void initVideoPath(String path) {
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
           Toast.makeText(this, "视频文件路径错误", 0).show();
            return;
        }
        videoView.setVideoPath(file.getPath()); //指定视频文件的路径33
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EditText et_path = (EditText) findViewById(R.id.et_path);
                    String path = et_path.getText().toString().trim();
                    initVideoPath(path);
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.play:
                if (!videoView.isPlaying()) {
                    videoView.start(); //开始播放
                }
                break;
            case R.id.pause:
                if (videoView.isPlaying()) {
                    videoView.pause(); //停止播放
                }
                break;
            case R.id.replay:
                if (videoView.isPlaying()) {
                    videoView.resume(); //重新播放
                }
                break;
            case R.id.ok:
                EditText et_path = (EditText) findViewById(R.id.et_path);
                String path = et_path.getText().toString().trim();
                initVideoPath(path);break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null) {
            videoView.suspend(); //将videoView占用的资源释放掉
        }
    }
}
