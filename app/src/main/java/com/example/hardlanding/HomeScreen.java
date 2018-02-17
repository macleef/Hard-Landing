package com.example.hardlanding;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity implements OnClickListener

{	
	//déclaration des quatres bouton du menu :
	private Button acc = null;  //accés aux hard landing
	private Button gopro = null; //ouvre l'application gopro
	private Button GPS = null; //accés a l'activitée gps
	private Button analyse = null;	//accés au courbes (Historique des vols)
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private int hauteurEcran = 0, largeurEcran = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		hauteurEcran = metrics.heightPixels;
		largeurEcran = metrics.widthPixels;

		acc =  (Button) findViewById(R.id.acceleration);
	    acc.setOnClickListener(this);
	    gopro =  (Button) findViewById(R.id.gopro);
	    gopro.setOnClickListener(this);
	    GPS =  (Button) findViewById(R.id.GPS);
	    GPS.setOnClickListener(this);
	    analyse =  (Button) findViewById(R.id.analyse);
	    analyse.setOnClickListener(this);
	    acc.getLayoutParams().width=gopro.getLayoutParams().width=GPS.getLayoutParams().width=analyse.getLayoutParams().width=50*largeurEcran/100;
	    acc.getLayoutParams().height=gopro.getLayoutParams().height=GPS.getLayoutParams().height=analyse.getLayoutParams().height=10*hauteurEcran/100;
	    acc.setTextSize(convertFromDp(60));
	    analyse.setTextSize(convertFromDp(50));
	    GPS.setTextSize(convertFromDp(60));
	    gopro.setTextSize(convertFromDp(60));
		if(!mBluetoothAdapter.isEnabled())
		{
			mBluetoothAdapter.enable();
		}
	}	
	@Override
	public void onClick(View v) {
		if(v==acc)
		{
			Intent intentHard = new Intent(HomeScreen.this, Acceleration.class);
			startActivity(intentHard);
		}
		else if(v==gopro)
		{
			Intent intentGopro = getPackageManager().getLaunchIntentForPackage("com.gopro.smarty");
			if(intentGopro!=null){startActivity(intentGopro);}
		}
		else if(v==GPS)
		{			
			Intent intentGPS = new Intent(HomeScreen.this, GPS.class);
			startActivity(intentGPS);	
		}
		else if(v==analyse)
		{			
			Intent intentAnalyse = new Intent(HomeScreen.this, Analyse.class);
			startActivity(intentAnalyse);					
		}
	}

	public float convertFromDp(int input) {
		final float scale = getResources().getDisplayMetrics().density;
		return ((input - 0.5f) / scale);
	}
}

