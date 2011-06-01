package com.lbi.internetweek.states;

import processing.core.PApplet;
import processing.core.PImage;

import com.lbi.internetweek.view.components.Bird;

public class BirdState implements IBirdState {
	
	Bird		bird;
	
	int 		frameWidth		=	128;
	int 		frameHeight		=	64;
		
	PImage[]	frames;
	
	public BirdState( Bird b )
	{
		bird = b;
	}

	public void draw() 
	{
		
	}

	@Override
	public void setState(IBirdState state)
	{
		bird.isTweeting = false;
		bird.isHurt = false;
	}

}
