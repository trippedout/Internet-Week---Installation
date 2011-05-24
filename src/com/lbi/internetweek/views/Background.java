package com.lbi.internetweek.views;

import processing.core.PApplet;
import processing.core.PImage;

public class Background {

	PApplet 	p;
	PImage 		grass;
	
	public Background( PApplet parent )
	{
		p 			=	parent;
		grass		=	p.loadImage("grass.png");
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
		p.vertex(0,p.height);
		p.vertex(p.width,p.height);
		p.endShape();
		
		//draw clouds
		
		//draw grass		
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
	}
	
	//day vals for lower color start: low r, low g, low b, low r end, low b end, low b end
	int lr = 255, lg = 255, lb = 255;
	int lre = 237, lge = 153, lbe = 80;
	
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
	int hr = 200, hg = 230, hb = 255;
	int hre = 0, hge = 45, hbe = 100;
	
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
