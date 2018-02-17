package com.example.hardlanding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Acceleration extends Activity implements OnClickListener {
	// definition des variables
	private ImageButton ledV = null; // Button led green
	private ImageButton ledR = null; // Button led rouge
	private Button home = null; // bouton de retour vers ?cran d'acceuil
	private ImageButton param = null; // Button parameter
	private TextView mroueA = null, mroueD = null, mroueG = null; // cadre roues
	private int masseA = 0, masseD = 0, masseG = 0; // masse mesuree sur les
													// roues Avant Droite Gauche
	private int masseA1 = 0, masseD1 = 0, masseG1 = 0; // masse mesuree sur les
														// roues Avant Droite
														// Gauche
	private Object masseAO, masseDO, masseGO;
	private Button masseB;
	private float amax = 0;

	// bargarphes d'acc?l?ration
	private TextView rougemD = null, rougeD = null, rougemG = null,
			rougeG = null, red = null, redm = null, bnoirA = null,
			bnoirD = null, bnoirG = null;

	// valeur des acceleration en temps r?el et maximale sur chaque roue
	private TextView acct = null,accelerat=null,vitesse=null,temperature=null,textmasse=null,textacc=null;
	private TextView accmA = null, accmD = null, accmG = null;
	private RelativeLayout roueA,roueD,roueG;
	private ImageButton gopro = null; // Button vers application gopro
	private TextView atterit = null; // texte
	private TextView envol = null;
	private ImageButton graph = null; // Button vers graphe
	private int couleur = 1, hauteur = -10, hauteurAcc = 0, hauteurMasse = 0,
			hRoue = 0;// diff?rentes variables
	int hauteurB = 10;
	double valeur = 0;
	public static int hmax = 0;
	int taille;
	// Relative layout, d?coupant l'activit?e
	private RelativeLayout contourA = null, contourD = null, contourG = null,
			haut = null, bas = null, masse = null, acceleration = null,
			menuaff = null, ecran = null, basdroit = null, basgauche = null;
	private ImageButton noir=null,noirD=null,noirG=null;
	private TextView gpsConnect = null, bluetoothConnect = null,
			nouveauFichier = null; // Textview du menu param?tres
	double dpi = 0;
	// vecteur pour "sauvegard?e" les acc?l?rations et les transmettre au graphe
	public static ArrayList<Integer> vect = new ArrayList<Integer>();
	static final ArrayList<Integer> vectAcce = new ArrayList<Integer>();
	static final String vectAcc = "valeur acc?l?ration";
	Set<Integer> set = new HashSet<Integer>();
	Context ctx;
	private int hauteurEcran = 0, largeurEcran = 0;

	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // module
																				// Bluetooth
	LocationManager locationManager = null; // Localisation GPS
	private TextView masseGA = null, masseGD = null, masseGG = null;
	private TextView message = null;

	String lineSep = System.getProperty("line.separator");

	// BLUETOOTH
	private BtInterface bt = null;
	private TextView connection = null;
	private int newHauteur = 20;
	float accelerationV;
	Object accelerationO;
	public static final int MESSAGE_READ_ACC = 1;
	public static final int MESSAGE_READ_MASSE1 = 2;
	public static final int MESSAGE_READ_MASSE2 = 3;
	public static final int MESSAGE_READ_MASSE3 = 4;


	Calendar temps = Calendar.getInstance();
	private int jour = 0;
	private int mois = 0;
	private int annee = 0;
	private int heure = 0;
	private int minute = 0;
	private String m = "";
	private String d = "";
	private String h = "";
	private String min = "";
	String fileName = null;
	public static List<String> filesName = null;
	public static List<File> files = null;

	// POUR RECUPERER LES ACCELERATIONS ET LES STOCKES DANS UN FICHIER TEXTE
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			files = getListFiles(getFilesDir());
			filesName = getFilesName(files);
			hmax = contourA.getHeight();

			switch (msg.what) {// Identifier le message
			case MESSAGE_READ_ACC:
				accelerationO = msg.obj;
				accelerationV = (Float) accelerationO;
				break;
			case MESSAGE_READ_MASSE1:
				masseAO = msg.obj; // R?ception de l'acc?l?ration suivant z
									// envoy?e depuis BT Interface
				masseA1 = (Integer) masseAO;
				// Affichage de la masse
				break;
			case MESSAGE_READ_MASSE2:
				masseDO = msg.obj; // R?ception de l'acc?l?ration suivant z
									// envoy?e depuis BT Interface
				masseD1 = (Integer) masseDO;
				break;
			case MESSAGE_READ_MASSE3:
				masseGO = msg.obj; // R?ception de l'acc?l?ration suivant z
									// envoy?e depuis BT Interface
				masseG1 = (Integer) masseGO;
				break;
			}

			if (accelerationV < -16) {
				accelerationV = -16;
			}
			if (accelerationV > 16) {
				accelerationV = 16;
			}

			writeacc(accelerationV);
			newHauteur = ((int) (accelerationV)) * (hmax / 16);
			// si acc?l?ration n?gative on affiche la partie n?gative et rend
			// invisible la partie positive la hauteur du bargraph est
			// proportionelle ? la valeur mesur?e
			if (accelerationV < 0) {
				redm.getLayoutParams().height = -newHauteur;
				red.setBackgroundColor(0x00000000);
				redm.setBackgroundColor(Color.parseColor("#33aa00"));
				rougemD.getLayoutParams().height = -newHauteur;
				rougeD.setBackgroundColor(0x00000000);
				rougemD.setBackgroundColor(Color.parseColor("#33aa00"));
				rougemG.getLayoutParams().height = -newHauteur;
				rougeG.setBackgroundColor(0x00000000);
				rougemG.setBackgroundColor(Color.parseColor("#33aa00"));
				acct.setText(" " + accelerationV + "g");
			} else if (accelerationV >= 0) {
				red.getLayoutParams().height = newHauteur;
				red.setBackgroundColor(Color.parseColor("#33aa00"));
				redm.setBackgroundColor(0x00000000);
				rougeD.getLayoutParams().height = newHauteur;
				rougeD.setBackgroundColor(Color.parseColor("#33aa00"));
				rougemD.setBackgroundColor(0x00000000);
				rougeG.getLayoutParams().height = newHauteur;
				rougeG.setBackgroundColor(Color.parseColor("#33aa00"));
				rougemG.setBackgroundColor(0x00000000);
				acct.setText(" " + accelerationV + "g");
			}
			if (accelerationV > amax) {
				amax = accelerationV;
				hauteurB = newHauteur;
				bnoirA.getLayoutParams().height = hauteurB;
				bnoirD.getLayoutParams().height = hauteurB;
				bnoirG.getLayoutParams().height = hauteurB;
				accmA.setText("amax = " + amax + "g");
				accmD.setText("amax = " + amax + "g");
				accmG.setText("amax = " + amax + "g");
			}
			// Visualisation de l'aterrissage
			if (accelerationV > 2) {
				ledV.setVisibility(View.VISIBLE);
				ledR.setVisibility(View.INVISIBLE);
				atterit.setVisibility(View.VISIBLE);
				envol.setVisibility(View.INVISIBLE);

			}
			// else if (accelerationV < 0.5) {
			// ledV.setVisibility(View.INVISIBLE);
			// ledR.setVisibility(View.VISIBLE);
			// envol.setVisibility(View.VISIBLE);
			// atterit.setVisibility(View.INVISIBLE);
			// }
			if (msg.what != MESSAGE_READ_ACC) {
				masseA = masseA1 / 82;
				masseD = masseD1 / 82;
				masseG = masseG1 / 82;
				masseGA.setText(String.valueOf(masseA) + "kg");
				masseGD.setText(String.valueOf(masseD) + "kg"); // Affichage de
																// la masse
				masseGG.setText(String.valueOf(masseG) + "kg"); // Affichage de
																// la masse
				mroueD.getLayoutParams().height = hRoue * masseD / 360 - 6; // affichage
																			// des
																			// masses
																			// graphique
				mroueG.getLayoutParams().height = hRoue * masseG / 360 - 6;
				mroueA.getLayoutParams().height = hRoue * masseA / 360 - 6;
				if (masseA < 240) {
					mroueA.setBackgroundColor(Color.parseColor("#33aa00"));
				} else {
					mroueA.setBackgroundColor(0xffff8c00);
				}
				if (masseD < 240) {
					mroueD.setBackgroundColor(Color.parseColor("#33aa00"));
				} else {
					mroueD.setBackgroundColor(0xffff8c00);
				}
				if (masseG < 240) {
					mroueG.setBackgroundColor(Color.parseColor("#33aa00"));
				} else {
					mroueG.setBackgroundColor(0xffff8c00);
				}

				if (masseA + masseD + masseG > 1100) {
					message.setText("!!! ATTENTION POIDS SUPERIEUR A 1100 KG !!!");
					message.setGravity(0x00000011);
					message.setTextSize(30);
					message.setTextColor(Color.RED);
					message.setVisibility(View.VISIBLE);
				}
				if (masseA + masseD + masseG < 600) {
					message.setText("!!! PROBLEME AVEC LES CAPTEURS !!!");
					message.setGravity(0x00000011);
					message.setTextSize(20);
					message.setTextColor(Color.BLUE);
					message.setVisibility(View.VISIBLE);
				}
				if (masseD > 360 || masseG > 360 || masseA > 360) {
					message.setVisibility(View.VISIBLE);
					message.setText("!!!! Attention charges mal ?quilibr?es ou avion surcharg? !!!!");
					message.setGravity(0x00000011);
					message.setTextSize(30);
					message.setTextColor(Color.RED);

					if (masseD > 360) {
						mroueD.getLayoutParams().height = hRoue - 6;
						mroueD.setBackgroundColor(0xffff0000);
					}
					if (masseG > 360) {
						mroueG.getLayoutParams().height = hRoue - 6;
						mroueG.setBackgroundColor(0xffff0000);
					}
					if (masseA > 360) {
						mroueA.getLayoutParams().height = hRoue - 6;
						mroueA.setBackgroundColor(0xffff0000);
					}
				}

				if (masseG == masseD && (masseG + masseD > 2 * masseA)) {
					message.setText("Trop de charge sur l'arri?re de l'appareil");
				}
				if (masseG > masseD) {
					if (masseG + masseD > 2 * masseA) {
						message.setText("R?partir la charge vers la droite et l'avant");
					} else {
						message.setText("R?partir la charge vers la droite");
					}
				}
				if (masseG < masseD) {
					if (masseG + masseD > 2 * masseA) {
						message.setText("R?partir la charge vers la gauche et l'avant");
					} else {
						message.setText("R?partir la charge vers la gauche");
					}
				}
				if (masseG + masseD < masseA) {
					message.setText("R?partir la charge vers l'arri?re");
				}
			}
		}
	};
	// pour v?rifier la bonne connection au bluetooth
	final Handler handlerStatus = new Handler() {
		public void handleMessage(Message msg) {
			int co = msg.arg1;
			if (co == 1) {
				connection.append("Connected\n");
			} else if (co == 2) {
				connection.append("Disconnected\n");
			}
		}
	};

	// fonction permettant d'?crire dans le dernier fichier cr?e :
	// filesName.get(0)
	public void writeacc(double d) {
		try {
			FileOutputStream fOut = openFileOutput(filesName.get(0),
					MODE_APPEND);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(d + ":");
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Function initialization ? l'ouverture
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// mettre l'activit?e en plein ?cran
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acceleration); // associ? l'activit?e	// avec le xml

		DisplayMetrics metrics = new DisplayMetrics(); // prendre les "mesure" de l'?cran permet d'adapter le programme ? diff?rent taille d'?cran
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		bt = new BtInterface(handlerStatus, handler);

		home = (Button) findViewById(R.id.menu);
		home.setOnClickListener(this);
		gopro = (ImageButton) findViewById(R.id.gopro);
		gopro.setOnClickListener(this);
		param = (ImageButton) findViewById(R.id.param);
		param.setOnClickListener(this);
		graph = (ImageButton) findViewById(R.id.graph);
		graph.setOnClickListener(this);
		masseB = (Button) findViewById(R.id.masseButton);
		masseB.setOnClickListener(this);

		ledV = (ImageButton) findViewById(R.id.ledV);
		ledV.setVisibility(View.INVISIBLE);
		atterit = (TextView) findViewById(R.id.atterit);
		atterit.setVisibility(View.INVISIBLE);
		ledR = (ImageButton) findViewById(R.id.ledR);
		envol = (TextView) findViewById(R.id.envol);
		red = (TextView) findViewById(R.id.rouge);
		red.setMaxHeight(160);
		redm = (TextView) findViewById(R.id.rougem);
		redm.setMaxHeight(160);
		rougeD = (TextView) findViewById(R.id.rougeD);
		rougeD.setMaxHeight(160);
		rougemD = (TextView) findViewById(R.id.rougemD);
		rougemD.setMaxHeight(160);
		rougeG = (TextView) findViewById(R.id.rougeG);
		rougeG.setMaxHeight(160);
		rougemG = (TextView) findViewById(R.id.rougemG);
		rougemG.setMaxHeight(160);
		connection = (TextView) findViewById(R.id.connection);

		bnoirA = (TextView) findViewById(R.id.bnoir);
		bnoirD = (TextView) findViewById(R.id.bnoirD);
		bnoirG = (TextView) findViewById(R.id.bnoirG);
		bnoirA.getLayoutParams().height = 1;
		bnoirD.getLayoutParams().height = 1;
		bnoirG.getLayoutParams().height = 1;
		noir= (ImageButton) findViewById(R.id.noir);
		noirD= (ImageButton) findViewById(R.id.noirD);
		noirG= (ImageButton) findViewById(R.id.noirG);
		acct = (TextView) findViewById(R.id.acct);
		accelerat = (TextView) findViewById(R.id.accelerat);
		vitesse = (TextView) findViewById(R.id.vitesse);
		temperature = (TextView) findViewById(R.id.altitude);
		textmasse = (TextView) findViewById(R.id.textmasse);
		textacc = (TextView) findViewById(R.id.textacc);
		accmA = (TextView) findViewById(R.id.textacmA);
		accmD = (TextView) findViewById(R.id.textacmD);
		accmG = (TextView) findViewById(R.id.textacmG);
		contourA = (RelativeLayout) findViewById(R.id.contourA);
		contourD = (RelativeLayout) findViewById(R.id.contourD);
		contourG = (RelativeLayout) findViewById(R.id.contourG);
		basdroit = (RelativeLayout) findViewById(R.id.basdroit);
		basgauche = (RelativeLayout) findViewById(R.id.basgauche);
		menuaff = (RelativeLayout) findViewById(R.id.menuaff);

		roueA = (RelativeLayout) findViewById(R.id.r1);
		roueD = (RelativeLayout) findViewById(R.id.r2);
		roueG = (RelativeLayout) findViewById(R.id.r3);
		hRoue = roueA.getHeight();
		mroueA = (TextView) findViewById(R.id.mr1);
		mroueA.setMaxHeight(hRoue - 10);
		mroueD = (TextView) findViewById(R.id.mr2);
		mroueD.setMaxHeight(hRoue - 10);
		mroueG = (TextView) findViewById(R.id.mr3);
		mroueG.setMaxHeight(hRoue - 10);

		haut = (RelativeLayout) findViewById(R.id.haut);
		bas = (RelativeLayout) findViewById(R.id.bas);
		acceleration = (RelativeLayout) findViewById(R.id.acceleration);
		masse = (RelativeLayout) findViewById(R.id.masse);
		ecran = (RelativeLayout) findViewById(R.id.ecran);
		ecran.setOnClickListener(this);
		gpsConnect = (TextView) findViewById(R.id.gps);
		gpsConnect.setOnClickListener(this);
		bluetoothConnect = (TextView) findViewById(R.id.blue);
		bluetoothConnect.setOnClickListener(this);
		nouveauFichier = (TextView) findViewById(R.id.nouveau);
		nouveauFichier.setOnClickListener(this);
		nouveauFichier.setTypeface(null, Typeface.BOLD);

		masseGA = (TextView) findViewById(R.id.masseA);
		masseGD = (TextView) findViewById(R.id.masseD);
		masseGG = (TextView) findViewById(R.id.masseG);
		message = (TextView) findViewById(R.id.message);
		message.getLayoutParams().height = 35 * hauteurEcran / 100;

		// take the screen height and width
		hauteurEcran = metrics.heightPixels;
		largeurEcran = metrics.widthPixels;
		// centre les information d'atterrissage
		envol.setGravity(0x00000001);
		atterit.setGravity(0x00000001);
		// fix height and width of each bloc
		bas.getLayoutParams().height = 25 * hauteurEcran / 100;
		haut.getLayoutParams().height = 7 * hauteurEcran / 100;
		acceleration.getLayoutParams().height = 60 * hauteurEcran / 100;
		acceleration.getLayoutParams().width = 50 * largeurEcran / 100;
		masse.getLayoutParams().height = 60 * hauteurEcran / 100;
		masse.getLayoutParams().width = 45 * largeurEcran / 100;
		roueA.getLayoutParams().height=roueD.getLayoutParams().height =roueG.getLayoutParams().height= 45 * hauteurEcran / 200;
		roueA.getLayoutParams().width=roueD.getLayoutParams().width=roueG.getLayoutParams().width=45 * largeurEcran / 250;
		basgauche.getLayoutParams().width = basdroit.getLayoutParams().width = 22 * largeurEcran / 100;
		graph.getLayoutParams().width=masseB.getLayoutParams().width=(int) (22 * largeurEcran / 100);
		graph.getLayoutParams().height=8 * hauteurEcran / 100;
		masseB.getLayoutParams().height=8 * hauteurEcran / 100;
		masseB.setTextSize(convertFromDp(50));
		accmA.setTextSize(convertFromDp(50));
		accmD.setTextSize(convertFromDp(50));
		accmG.setTextSize(convertFromDp(50));
		envol.setTextSize(convertFromDp(50));
		atterit.setTextSize(convertFromDp(50));
		acct.setTextSize(convertFromDp(50));
		accelerat.setTextSize(convertFromDp(50));
		connection.setTextSize(convertFromDp(50));
		vitesse.setTextSize(convertFromDp(50));
		temperature.setTextSize(convertFromDp(50));
		masseGA.setTextSize(convertFromDp(50));
		masseGD.setTextSize(convertFromDp(50));
		masseGG.setTextSize(convertFromDp(50));
		textmasse.setTextSize(convertFromDp(50));
		textacc.setTextSize(convertFromDp(50));
		param.getLayoutParams().height=home.getLayoutParams().height=gopro.getLayoutParams().height=7 * hauteurEcran / 100;
		gopro.getLayoutParams().width=largeurEcran/3;
		param.getLayoutParams().width=home.getLayoutParams().width= 7*largeurEcran/100;
		hauteurAcc = acceleration.getHeight();
		hauteurMasse = masse.getHeight();

		contourA.getLayoutParams().height = contourD.getLayoutParams().height = contourG.getLayoutParams().height = 45 * (55 * hauteurEcran / 100) / 100;
		contourA.getLayoutParams().width = contourD.getLayoutParams().width = contourG.getLayoutParams().width =  15 * (50 * largeurEcran / 100) / 100;
		bnoirA.getLayoutParams().width = bnoirD.getLayoutParams().width=bnoirG.getLayoutParams().width=15 * (50 * largeurEcran / 100) / 100;
		red.getLayoutParams().width=rougeD.getLayoutParams().width=rougeG.getLayoutParams().width=15 * (50 * largeurEcran / 100) / 100;
		redm.getLayoutParams().width=rougemD.getLayoutParams().width=rougemG.getLayoutParams().width=15 * (50 * largeurEcran / 100) / 100;
		noir.getLayoutParams().width =noirD.getLayoutParams().width = noirG.getLayoutParams().width=15 * (50 * largeurEcran / 100) / 100;
		ledV.getLayoutParams().height=ledR.getLayoutParams().height=  30*(25 * hauteurEcran / 100)/100;

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// on r?cup?re la date
		jour = temps.get(Calendar.DAY_OF_MONTH);
		mois = temps.get(Calendar.MONTH) + 1;
		annee = temps.get(Calendar.YEAR);
		heure = temps.get(Calendar.HOUR_OF_DAY);
		minute = temps.get(Calendar.MINUTE);

		files = getListFiles(getFilesDir());
		filesName = getFilesName(files);


	}

	// fonction "clique"
	@Override
	public void onClick(View v) {
		hRoue = roueA.getHeight();
		files = getListFiles(getFilesDir());
		filesName = getFilesName(files);
		// si on clique sur l'?cran on d?sactive le menu
		if (v == ecran) {
			menuaff.setVisibility(View.INVISIBLE);
			message.setVisibility(View.INVISIBLE);
		} else if (v == gopro) {
			Intent intentGopro = getPackageManager().getLaunchIntentForPackage(
					"com.gopro.smarty");
			if (intentGopro != null) {
				startActivity(intentGopro);
			}

		}
		// retour sur l'?cran d'acceuil : fl?che en haut ? gauche
		else if (v == home) {
			Intent intent = new Intent(this, HomeScreen.class);
			startActivity(intent);
			// graphe en bas ? gauche : afiche l'activit?e graphe
		} else if (v == graph) {
			Intent intent = new Intent(this, Analyse.class);
			startActivity(intent);
		}
		// en haut ? droite : menu des param?tres : GPS, Bluetooth
		else if (v == param) {
			menuaff.setVisibility(View.VISIBLE);
			// connection au bluetooth
		} else if (v == bluetoothConnect) {
			
			bt.connect();
			// connection du GPS
		} else if (v == gpsConnect) {

			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Intent myIntent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(myIntent);
			}
			// creation d'un nouveau fichier
		} else if (v == nouveauFichier) {

			if (jour < 10) {
				d = "0";
			}

			if (mois < 10) {
				m = "0";
			}

			if (heure < 10) {
				h = "0";
			}

			if (minute < 10) {
				min = "0";
			}

			// pour avoir le nom du fichier du type : jour:mois:ann?e
			// heure:minutes on ne peut avoir jour/mois car "/" cr?e un nouveau
			// r?pertoire
			fileName = d + Integer.toString(jour) + ":" + m
					+ Integer.toString(mois) + ":" + Integer.toString(annee)
					+ " " + h + Integer.toString(heure) + ":" + min
					+ Integer.toString(minute) + ".txt";
			{
				try {
					FileOutputStream fOut = openFileOutput(fileName,
							MODE_APPEND);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}// fin nouveaufichier
		else if (v == masseB) {
			if (bt != null) {
				try {
					bt.sendData("M");// Envoi du caract?re M pour demande de
										// masse
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}// fin du onclicklistener

	// Fonction pour r?cup?rer la liste des fichiers
	private List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			if (file.isDirectory()) {
				inFiles.addAll(getListFiles(file));
			} else {
				if (file.getName().endsWith(".txt")) {
					inFiles.add(file);
				}
			}
		}
		return inFiles;
	}

	// Fonction pour r?cup?rer le nom des fichiers
	private List<String> getFilesName(List<File> file) {
		ArrayList<String> FileName = new ArrayList<String>();
		String name = null;
		taille = file.size();
		for (int i = taille - 1; i >= 0; i--) {
			name = (file.get(i)).getName();
			FileName.add(name);
		}
		return FileName;
	}

	public float convertFromDp(int input) {
		final float scale = getResources().getDisplayMetrics().density;
		return ((input - 0.5f) / scale);
	}

}
