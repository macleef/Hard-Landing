package com.example.hardlanding;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
//import android.os.PowerManager.WakeLock;



public class GPS extends Activity {
	
	// Declaration des variables globales
	// GPS
	LocationManager locationManager;
	LocationListener locationListener;
	// Veille ecran
	//protected WakeLock vWakelock;
	// Interface graphique
	TextView vTextViewLatitude;
	TextView vTextViewLongitude;
	TextView vTextViewSpeed;
	TextView vTextViewBearing;
	TextView eTextViewSpeed;
	TextView eTextViewLatitude;
	TextView eTextViewLongitude;
	TextView eTextViewBearing;
	// Encadrer les resultats
	//DrawView drawView;
	
	
	public class mylocationlistener implements LocationListener {
		public static final int FORMAT_SECONDS=2; 
		public static final int FORMAT_MINUTES=1; 
			
		@Override
		public void onLocationChanged(Location location) {
		
	    // Si une position est renvoyée par le GPS
						if (location!=null)
						{
							setProgressBarIndeterminateVisibility(false);
						    double currentSpeed = location.getSpeed();
						    displaySpeed(currentSpeed);
							double currentAltitude = location.getAltitude();
							double currentLatitude = location.getLatitude();
							displayLatitude(currentLatitude);
							double currentLongitude = location.getLongitude();
							displayLongitude(currentLongitude);
							float  currentBearing=location.getBearing();
							displayBearing(currentBearing);
						}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
		
	    public void displaySpeed(double currentSpeed)
	    {
	    	
	    	currentSpeed=currentSpeed*3.6; // Vitesse GPS en km/h
	    	//currentSpeed=currentSpeed*0.54; // Vitesse GPS en noeuds
	    	int currentSpeed2=(int)currentSpeed;
	    	eTextViewSpeed.setText(String.valueOf(currentSpeed2) +"km/h");
	    	//eTextViewSpeed.setText(currentSpeed2+"km/h"); 
	    	// A la rigueur, on affiche les vitesses en km/h et noeuds
	    }
	    


	    public void displayLatitude(double currentLatitude)
	    {
	    	String currentLatitude2=Location.convert(currentLatitude, Location.FORMAT_SECONDS);
	    	// currentLatitude2 est une chaîne de caractères au format FORMAT_SECONDS	
	    	String []identification2=currentLatitude2.split(":");
	    	String LDP=identification2[0]; //Affichage degrés
	    	String LMP=identification2[1]; //Affichage minutes d'arc
	    	String Seconde=identification2[2]; //Chiffres des secondes d'arc
	    	String LSP=Seconde.substring(0,2); // On ne prend en compte que
	    	//les 2 premiers chiffres des secondes d'arc => précision
	    	eTextViewLatitude.setText(LDP+"°"+LMP+"'"+LSP+"''");
	    	
	    }

	    public void displayLongitude(double currentLongitude)
	    {
	    	String currentLongitude2=Location.convert(currentLongitude, FORMAT_SECONDS);
	    	String []identification=currentLongitude2.split(":");
	    	// On coupe currentLongitude2 dès qu'il y a un ":"
	    	String LODP=identification[0]; // Affichage degrés
	    	String LOMP=identification[1]; // Affichage minutes d'arc
	    	String Seconde=identification[2]; // Chiffres des secondes d'arc
	    	String LOSP=Seconde.substring(0,2); // On ne prend en compte que 
	    	//les 2 premiers chiffres des secondes d'arc => précision
	        eTextViewLongitude.setText(LODP+"°"+LOMP+"'"+LOSP+"''");	
	    	
	    }
	    
	    public void displayBearing(float currentBearing)
	    {
	    	String currentBearing2=Float.toString(currentBearing);
	    	eTextViewBearing.setText(currentBearing2);
	    }
	    
	} // Fin class interne mylocationlistener
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //On spécifie que l'on va avoir besoin de gérer l'affichage du cercle de chargement
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_gps);
        // GPS
        setProgressBarIndeterminateVisibility(true);
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new mylocationlistener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        // L'application bloque la mise en veille
        //final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    	//this.vWakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        //this.vWakelock.acquire();
        // Interface Graphique
        vTextViewSpeed = (TextView)findViewById(R.id.textView3);
        vTextViewLatitude = (TextView)findViewById(R.id.textView1);
        vTextViewLongitude = (TextView)findViewById(R.id.textView2);
        vTextViewBearing = (TextView)findViewById(R.id.textView7);
        eTextViewSpeed = (TextView)findViewById(R.id.textView6);
        eTextViewLatitude = (TextView)findViewById(R.id.textView4);
        eTextViewLongitude = (TextView)findViewById(R.id.textView5);
        eTextViewBearing = (TextView)findViewById(R.id.textView8);
	}
	
//	public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    
    //@Override
    // On relache l'interdiction de mise en veille ecran
   //public void onDestroy(){
    //this.vWakelock.release();
    //    super.onDestroy();
    //}
    
        
} // Fin de la class MainActivity
	

