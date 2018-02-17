package com.example.hardlanding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.screen);
		Thread logoTimer = new Thread() {
            public void run(){
                try{
                    int logoTimer = 0;
                    while(logoTimer < 3000){
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    startActivity(new Intent("com.tutorial.CLEARSCREEN"));
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    finish();
                }
            }
        };         
        logoTimer.start();
    }
}