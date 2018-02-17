package com.example.hardlanding;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.util.AttributeSet;


public class Graphe extends View{
	Context ctx;
	Paint paint1 = new Paint();
	Paint paint2 = new Paint();

	public static List<Float> accel = Analyse.vecteurA; //vecteur avec toutes les acc�l�rations
	float vmin=Vmin();
	float vmax=Vmax();
	int tmax=0;	

	//calcul
	int maxH = Analyse.maxH;
	int maxW = Analyse.maxW/2;	
	float milieu = (vmax-vmin)/2;
	float val= maxW/(vmax-vmin);
	int intervalle = (accel.size()/Analyse.maxH);
	float coef = Analyse.maxW/vmax;
	
	private float Vmax()
	{
		float max=0;
		for(int i=1; i<accel.size(); i++)
		{
			accel.get(i);
			if(accel.get(i)<16 && accel.get(i)!=0)
			{
			if(accel.get(i)>max)
				{max=accel.get(i);}
			}
		}return max;		
	}
	private float Vmin()
	{
		float min=0;
		for(int i=1; i<accel.size(); i++)
		{
			accel.get(i);
			if(accel.get(i)>-16 && accel.get(i)!=0.0)
			{
			if(accel.get(i)<min)
				{
				min=accel.get(i);
				}
			}
		}return min;		
	}
	//configuration des param�tres de dessin
	public Graphe(Context context) {
		super(context);
		paint1.setColor(Color.BLUE);
		paint1.setAntiAlias(true);
		paint2.setColor(Color.BLACK);
		paint2.setAntiAlias(true);
		paint2.setTextSize((float) 15);
					
	}

	public Graphe(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//fonction permettant de dessiner
	protected void onDraw(Canvas canvas)
	{
		if(Analyse.maxH>=accel.size()){ tmax=accel.size();intervalle=1;}
		else {tmax=Analyse.maxH;intervalle = (accel.size()/Analyse.maxH);}	
		float valeur = 0;
		
		int j=10;
		canvas.drawLine(vmax*coef-40,0,vmax*coef-40,maxH,  paint2);		
		canvas.drawText(Float.toString(vmax), Analyse.maxW-40, 25, paint2);
		canvas.drawLine(vmin*coef,0,vmin*coef,maxH,  paint2);
		canvas.drawText(Float.toString(vmin), 10, 25, paint2);
		for(int i=1; i<accel.size()-intervalle; i= i+intervalle)
		{
			valeur = accel.get(i); //on r�cup�re la valeur de l'acc�l�ration i
			canvas.drawLine((accel.get(i))*coef-40,j,(accel.get(i+intervalle))*coef-40,j+1,  paint1); // on trace entre l'acc�l�ration i et la suivante
			if(valeur == vmax)	{canvas.drawText(Float.toString(valeur), valeur*coef-100, j,paint2);}
			j=j+1;
		}
	}	//fin du canvas

}
