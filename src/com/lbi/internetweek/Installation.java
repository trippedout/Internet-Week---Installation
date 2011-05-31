package com.lbi.internetweek;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Timer;

import org.puremvc.java.patterns.facade.Facade;

import processing.core.*;
import processing.opengl.PGraphicsOpenGL;

import hypermedia.video.*;

import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;
import com.lbi.internetweek.model.TwitterProxy;
import com.lbi.internetweek.utils.Stats;
import com.lbi.internetweek.view.components.BackgroundView;
import com.lbi.internetweek.view.components.Bird;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import twitter4j.Status;

public class Installation extends PApplet 
{
	// --------------------------------------------------------------------------------------------------------
	// MAIN AND VERS

	public static void main(String args[]) 
	{
		PApplet.main(new String[] { "--present", "com.lbi.internetweek.Installation" });
	}

	// --------------------------------------------------------------------------------------------------------
	// VARS	

	ApplicationFacade 		_facade;
	Stats 					_stats;

	//fonts
	public PFont font;

	//images
	PImage pi_LBiLogo;
	
	// --------------------------------------------------------------------------------------------------------
	// PROCESSING

	public void setup()
	{
		//processing calls
		//-------------------------------------
		size( 1280, 960, OPENGL );
		
		_facade = ApplicationFacade.getInst();
		_facade.startup(this);
		
		_stats = new Stats(this);
	}

	public void draw()
	{
		_facade.update();
		_facade.draw();
		
		_stats.draw(0,0);
		
		/*
		birds.updatePhysics(blob);
		birds.draw();

		kinect.drawGuide();
		//*/
	}
	
	public void keyPressed()
	{
		if ( key == CODED) 
		{
		    if (keyCode == UP)
		    	_facade.keyPressed(UP);
		    else if( keyCode == DOWN )
		    	_facade.keyPressed(DOWN);
		}
	}

}
