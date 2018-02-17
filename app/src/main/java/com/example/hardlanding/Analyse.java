package com.example.hardlanding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Analyse extends Activity implements OnClickListener{
	
    public static int maxH= 0;
    public static int maxW= 0;
    public static List<Float> vecteurA;
    public static float maxV;
    public static float minV;
    public static int tailleV;
    float accelerationV;
    private CheckBox check1=null,check2=null,check3=null,check4=null,check5=null; //checkbox
    private Button courbe=null,effacer=null;
    public View drawView=null;
    public static List<File> files = null; 
	public static List<String> filesName=null;	
	public int numeroFichier = -1;	
	List<CheckBox> checkBoxes = new ArrayList<CheckBox>();	//vecteur contenant les checkboxs
	private int taille =0;
    
//    private BtInterface bt = null;
//    private TextView connection =null;
    
    
//    final Handler handler = new Handler() {
//        public void handleMessage(Message msg) {       
//        	accelerationV=msg.getData().getFloat("receivedData1");	
//        	writeacc((accelerationV));
//        	
//        }
//    };
//    final Handler handlerStatus = new Handler() {
//        public void handleMessage(Message msg) {
//            int co = msg.arg1;
//            if(co == 1) {
//            	connection.append("Connected\n");
//            } else if(co == 2) {
//            	connection.append("Disconnected\n");
//            }
//        }
//    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		// Desactiver la barre de titre de notre application
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Passer la fenêtre en full-screen == cacher la barre de notification
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        maxH = metrics.heightPixels;  //récupére la hauteur de l'écran
        maxW = metrics.widthPixels;	//récupére la largeur de l'écran        

        files = getListFiles(getFilesDir());		
        filesName=getFilesName(files);      

//        afficher le graphe
//        drawView = new Graphe(this);
//        setContentView(drawView);      

        //affiche le menu principale avec les checkbox
        setContentView(R.layout.activity_analyse);       
//        maxV =Vmax(); //récupérer les valeur max et min d'accélération
//        minV= Vmin();

//        bt = new BtInterface(handlerStatus, handler);   
         //initialisation des checkbox
        check1=(CheckBox) findViewById(R.id.fichier1); check1.setChecked(true); check1.setOnClickListener(this); check1.setTextSize(20);
        check2=(CheckBox) findViewById(R.id.fichier2); check2.setChecked(false); check2.setOnClickListener(this); check2.setTextSize(20);
        check3=(CheckBox) findViewById(R.id.fichier3); check3.setChecked(false); check3.setOnClickListener(this); check3.setTextSize(20);
        check4=(CheckBox) findViewById(R.id.fichier4); check4.setChecked(false); check4.setOnClickListener(this); check4.setTextSize(20);
        check5=(CheckBox) findViewById(R.id.fichier5); check5.setChecked(false); check5.setOnClickListener(this); check5.setTextSize(20);
        check1.setText("");
		check2.setText("");
		check3.setText("");
		check4.setText("");
		check5.setText("");
        courbe = (Button) findViewById(R.id.courbe); courbe.setOnClickListener(this);
        effacer = (Button) findViewById(R.id.effacer); effacer.setOnClickListener(this);       
		
		//affecter chaque checkbox au vecteur filesNames et stocké les checkbox dans un vecteur chexBoxes
		
		
		
		if(files.size()==4)
		{
			check1.setText(filesName.get(0));
			check2.setText(filesName.get(1));
			check3.setText(filesName.get(2));
			check4.setText(filesName.get(3));
			check5.setText("pas de données sauvegardées");
		}
		else if(files.size()==3)
		{
			check1.setText(filesName.get(0));
			check2.setText(filesName.get(1));
			check3.setText(filesName.get(2));
			check5.setText("pas de données sauvegardées");
			check4.setText("pas de données sauvegardées");
		}			
			
		else if(files.size()==2)
		{
			check1.setText(filesName.get(0));
			check2.setText(filesName.get(1));
			check5.setText("pas de données sauvegardées");
			check4.setText("pas de données sauvegardées");
			check3.setText("pas de données sauvegardées");
		}
		else if(files.size()==1)
		{
			check1.setText(filesName.get(0));
			check5.setText("pas de données sauvegardées");
			check4.setText("pas de données sauvegardées");
			check3.setText("pas de données sauvegardées");
			check2.setText("pas de données sauvegardées");
		}
		else if(files.size()==0)
		{
			check5.setText("pas de données sauvegardées");
			check4.setText("pas de données sauvegardées");
			check3.setText("pas de données sauvegardées");
			check2.setText("pas de données sauvegardées");
			check1.setText("pas de données sauvegardées");
			
		}	
				
		else
		{
			check1.setText(filesName.get(0));
			check2.setText(filesName.get(1));
			check3.setText(filesName.get(2));
			check4.setText(filesName.get(3));
			check5.setText(filesName.get(4));
		}
		checkBoxes.add(check1);
		checkBoxes.add(check2);
		checkBoxes.add(check3);
		checkBoxes.add(check4);
		checkBoxes.add(check5);
		checkAll(false);

	}
	
	//function read the file character by character
		public List<Float> readAccel (String file) {
			final List<Float> vecteur= new ArrayList<Float>();
		        String datax = "" ;
		        try {
		            FileInputStream fIn = openFileInput (file ) ; //ouvre le fichier
		            InputStreamReader isr = new InputStreamReader ( fIn ) ;        
		            
		            char c = (char) isr.read();
	         
		            while ( c !=(char) -1 ) {  //tant que l'on lit des caracteres
		            	if(c !=58)
		            	{		            		
		            		datax+=c; //sinon on rajoute le caractére à la chaine de caractere		            		
		            	} else//si on a deux points (entre deux valeurs d'accélérations)
		                {
		            		float donnée = Float.parseFloat(datax);
		            		if(donnée<0){donnée=-donnée;}
		                	if(donnée>1) //on ne prend que les valeurs supérieurs à 1g
		                	{
		                	vecteur.add(donnée); //on stock la chaine de caractére (la valeur d'accélération) dans un vecteur 
		                	}
		                	datax=""; //on initialise datax
		                }
		            	c = (char) isr.read(); 	
		            }
		           isr.close ( ) ;  //ferme le fichier
		        } catch ( IOException ioe ) {
		            ioe.printStackTrace ( ) ;
		        }
		    return vecteur;
	  	}
		//fonction pour récupérer la valeur max d'accélération
		private float Vmax()
		{
			float max=0;
			for(int i=0; i<vecteurA.size(); i++)
			{
				vecteurA.get(i);
				if(vecteurA.get(i)>-16 || vecteurA.get(i)!=0.0)
				{
				if(vecteurA.get(i)>max)
				{	max=vecteurA.get(i);	}
				}
			}return max;		
		}
		//fonction pour récupérer la valeur minimum d'accélération
		private float Vmin()
		{
			float min=0;
			for(int i=0; i<vecteurA.size(); i++)
			{
				vecteurA.get(i);
				if(vecteurA.get(i)>-16 || vecteurA.get(i)!=0.0)
				{
				if(vecteurA.get(i)<min)
				{	min=vecteurA.get(i);	}
				}
			}return min;	
		}
//		Fonction pour récupérer la liste des fichiers
		private List<File> getListFiles(File parentDir) {
		    ArrayList<File> inFiles = new ArrayList<File>();
		    File[] files = parentDir.listFiles();
		    Arrays.sort(files);
		    for (File file : files) {
		        if (file.isDirectory()) {
		            inFiles.addAll(getListFiles(file));
		        } else {
		            if(file.getName().endsWith(".txt")){
		                inFiles.add(file);
		            }
		        }
		    }return inFiles;
		}
//		Fonction pour récupérer le nom des fichiers
		private List<String> getFilesName(List<File> file){	
			ArrayList<String> FileName = new ArrayList<String>();
			String name=null;
			taille = file.size();
			for(int i=taille-1;i>=0;i--)
			{
				name = (file.get(i)).getName();
				FileName.add(name);
			}return FileName;				
		}
		
//		protected void onStart() {super.onStart();}
//		protected void onRestart()	{super.onRestart();}		
//		protected void onResume(){super.onResume();	}
//		protected void onPause(){super.onPause();}
//		protected void onStop() { super.onStop(); }
//	    protected void onDestroy()    { super.onDestroy();  }

		@Override
		public void onClick(View v) {
			//affiche la courbe du fichier d'accélération sélectionnée
			if(v==courbe)
			{
				for(int i=0;i<checkBoxes.size();i++) //regarde quel fichier est sélectionné
				{
					boolean value = checkBoxes.get(i).isChecked();
					if(value == true)
					{	numeroFichier = i;	}
				}
					
				try
				{vecteurA=readAccel(filesName.get(numeroFichier));
					drawView = new Graphe(this);
				setContentView(drawView); }
				catch ( Exception e ) {
		            e.printStackTrace ( );
		        }

			}
			//efface le fichier sélectionner
			else if(v==effacer)
			{
				taille = files.size();
				for(int i=0;i<checkBoxes.size();i++)//regarde quel fichier est sélectionné
				{
					boolean value = checkBoxes.get(i).isChecked();
					if(value == true)
					{	numeroFichier = i;	}
				}
				try
				{
				files.get(taille-1-numeroFichier).delete();}
				catch ( Exception e ) {
		            e.printStackTrace ( );
		        }
			}
			else if (v==check1)
			{
				checkAll(false);
				check1.setChecked(true);
			}
			else if (v==check2)
			{
				checkAll(false);
				check2.setChecked(true);
			}
			else if (v==check3)
			{
				checkAll(false);
				check3.setChecked(true);
			}
			else if (v==check4)
			{
				checkAll(false);
				check4.setChecked(true);
			}
			else if (v==check5)
			{
				checkAll(false);
				check5.setChecked(true);
			}
		}
		private void checkAll(boolean value) {
		    for(CheckBox cb : checkBoxes) {
		        cb.setChecked(value);
		    }
		}
}
