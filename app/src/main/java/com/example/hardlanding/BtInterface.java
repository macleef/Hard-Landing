package com.example.hardlanding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
//import android.util.Log;

public class BtInterface {
	
	private BluetoothDevice device = null;
	private BluetoothSocket socket = null;
	private InputStream receiveStream = null;
	private OutputStream sendStream = null; // Déclaration flux sortant
	private BluetoothAdapter btAdapter = null;
	private ReceiverThread receiverThread;
	private static String address = "00:0B:CE:0A:78:2C";
	private static final String TAG = "bluetooth1";
	private boolean keepingLoopAlive = true;
	

	Handler handler;

	public BtInterface(Handler hstatus, Handler h) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
//		if (!btAdapter.isEnabled()) {
//		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//		}
		device = btAdapter.getRemoteDevice(address);
				
		    try {
				// Obtention du BluetoothSocket	
					socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
					receiveStream = socket.getInputStream();
					sendStream = socket.getOutputStream(); // Obtention d'un OutputStream
//					Log.d(TAG, "BluetoothSocket found");
				} 
		    catch (IOException e) 
				{
					e.printStackTrace();
//					Log.d(TAG, "BluetoothSocket not found");
				}
				
		handler = hstatus;
		
		receiverThread = new ReceiverThread(h);
	} // FIN methode BtInterface
	

	public void connect() {
		new Thread() {
			@Override public void run() {
				try {
					socket.connect(); // Connection
//					Log.d(TAG, "Connexion ok");
					Message msg = handler.obtainMessage();
					msg.arg1 = 1;
	                handler.sendMessage(msg);
	                
					receiverThread.start();
					
				} catch (IOException e) {
//					Log.v("N", "Connection Failed : "+e.getMessage());
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void close() {
		try {
			socket.close();
			receiveStream.close();
		} catch (IOException e) {
			//Le block catch est exécuté que si il y a une erreur dans le block try
//			Log.d("Close", "Connection close failed " + e);
		}
		finally
		{
			//le bloc finally est toujours exécuté soit après le bloc try si aucune erreur n'est detectée
			//soit après le bloc catch si une erreur a été relevée. 
			socket = null;
			receiveStream = null;
		}
	}
	public void sendData(String data)
	{
		try
		{
		    sendStream.write(data.getBytes());
		    sendStream.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public BluetoothDevice getDevice() {
		return device;
	}
	
	private class ReceiverThread extends Thread {
		Handler handler;
		
		ReceiverThread(Handler h) {
			handler = h;
		}
		
		@Override public void run() {
			//while(true) {
			while(keepingLoopAlive){
				try {
					if(receiveStream.available() > 0) { 
						 
						 int variable=0;
						 variable=receiveStream.read();
						 switch(variable)
						 {
						 case 10: // Réception d'une donnée d'accélération
							 int synchronisation=0;
	                         int k=0;
	                         // Boucle while pour la synchronisation avec les données d'accélérations envoyées par l'accéléro+µP
							 while(k!=3) // Tant que k!=3, on boucle
							 {
		                      synchronisation=receiveStream.read();
		                        
		                      if (synchronisation==36)
		                      { 
		                          synchronisation=0;
		                          k++;
		                      }
		                      else 
		                      {
		                          synchronisation=0;
		                          k=0;		
		                      }
							  }
	                      
						      // k=3, on sort de la boucle
							      int rawdata[]=new int[6]; 
							    
								  for (int i=0;i<6;i++) // On reçoit 6 entiers
								{
							      rawdata[i]=receiveStream.read(); // On reçoit les octets 
							      // en provenance du microcontrôleur sous format entier
								}
							      byte rawData[]=new byte[6]; // On transforme les entiers reçus
							      // en octets, on ne s'interesse qu'aux 2 derniers octets (accélération suivant Z)
							      rawData[4]=(byte)rawdata[4];
							      rawData[5]=(byte)rawdata[5];
							    
								  int accelResult;
								  // Concaténation des 2 octets d'accélération suivant
								  // l'axe z
								  accelResult=(((int)rawData[5])<<8)| rawData[4];
								  float acceleration;
								  // Obtention de l'accélération suivant z en g
								  acceleration=(float)(accelResult*0.0312);
								
	                              // Envoi de l'accélération suivant z dans l'activité principale MainActivity
								  // grâce à un handler
								  handler.obtainMessage(Acceleration.MESSAGE_READ_ACC,-1,-1, acceleration).sendToTarget();
				                  //handler.sendMessage(msg);
				                  break;
				                  
						 case 77: // Réception d'une donnée de masse
						          int massedata[]=new int[6];
						          int masse1;
						          int masse2;
						          int masse3;
						          for (int i=0;i<6;i++)
						          {
						    	  massedata[i]=receiveStream.read();  
						          }

						          masse1=(massedata[0]<<8)| massedata[1];
    					          masse2=(massedata[2]<<8)| massedata[3];
     					          masse3=(massedata[4]<<8)| massedata[5];
						          
						          
						          handler.obtainMessage(Acceleration.MESSAGE_READ_MASSE1,-1,-1, masse1).sendToTarget();
						          handler.obtainMessage(Acceleration.MESSAGE_READ_MASSE2,-1,-1, masse2).sendToTarget();
  					              handler.obtainMessage(Acceleration.MESSAGE_READ_MASSE3,-1,-1, masse3).sendToTarget();
						          
			                      break;
						         
						      
						 } // Fin du switch
						                  
						 } // Fin du if  

					  } // Fin du try
					
				catch (IOException e) {
					e.printStackTrace();
					//Si une erreur arrive on bloque la boucle et on detruit le socket
					keepingLoopAlive = false;
					close();
				}
			} // Fin du while
		} // Fin de la méthode run
	} // Fin du thread de traitement des données

} // Fin class BtInterface
