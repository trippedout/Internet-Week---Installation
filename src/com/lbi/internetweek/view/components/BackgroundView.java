package com.lbi.internetweek.view.components;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.view.BackgroundMediator;

import processing.core.PApplet;
import processing.core.PImage;

public class BackgroundView 
{
	BackgroundMediator mediator;

	PApplet 	p;
	PImage 		grass;
	PImage		bg;
	
	public BackgroundView(BackgroundMediator m)
	{
		mediator 	=	m;		
		p 			=	(PApplet) ApplicationFacade.app;
	}
	
	public void loadImages()
	{
		//grass		=	p.loadImage("grass.png");
		bg			=	p.loadImage("bg_right.png");
	}
	
	public void draw()
	{		
		//draw gradient		
		float d = (p.cos(p.frameCount*.005f) + 1) * .5f;
		int c1 = getHighColor(d);
		int c2 = getLowColor(d);	
		
		p.beginShape(p.QUAD_STRIP);
		p.fill(c1);
		p.vertex(0,0);
		p.vertex(p.width,0);
		p.fill(c2);
		p.vertex(0,p.height - 200);
		p.vertex(p.width,p.height - 200);
		p.endShape();
		
		//draw clouds
		
		//draw grass	
		/*
		int gw = grass.width,
			gh = grass.height;			

		p.pushMatrix();
			p.translate(0,p.height-gh);
			
			p.noStroke();			
			p.beginShape(p.QUADS);
			p.texture(grass);
		  	p.vertex(0,0,0,0);
		  	p.vertex(gw,0,gw,0);
		  	p.vertex(gw,gh,gw,gh);
		  	p.vertex(0,gh,0,gh);
		  	p.endShape();
	  	p.popMatrix();
	  	//*/
	  	
		int bw = bg.width,
			bh = bg.height;
		
		
		p.beginShape(p.QUADS);
		p.texture(bg);
	  	p.vertex(0,0,0,0);
	  	p.vertex(bw,0,bw,0);
	  	p.vertex(bw,bh,bw,bh);
	  	p.vertex(0,bh,0,bh);
	  	p.endShape();
	}
	
	//day vals for lower color start: low r, low g, low b, low r end, low b end, low b end
	int lr = 255, lg = 255, lb = 255;
	int lre = 255, lge = 200, lbe = 99;
	
	private int getLowColor(float d) 
	{
		int dr = lr - lre;
		int dg = lg - lge;
		int db = lb - lbe;
		
		float nr = lr - ( dr * d );
		float ng = lg - ( dg * d );
		float nb = lb - ( db * d );
		
		return p.color(nr, ng, nb );
	}
	
	//day vals for higher color start: high r, high g, high b, high r end, high b end, high b end
	int hr = 159, hg = 213, hb = 255;
	int hre = 57, hge = 81, hbe = 195;
	
	private int getHighColor(float d) 
	{
		int dr = hr - hre;
		int dg = hg - hge;
		int db = hb - hbe;
		
		float nr = hr - ( dr * d );
		float ng = hg - ( dg * d );
		float nb = hb - ( db * d );
		
		return p.color(nr, ng, nb );
	}

	
}
