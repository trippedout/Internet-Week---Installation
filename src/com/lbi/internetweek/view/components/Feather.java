package com.lbi.internetweek.view.components;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;

import processing.core.PImage;
import processing.core.PVector;

public class Feather
{
	private Installation	_pa;
	private PImage 			_image;
	private PVector			_velocity, _position;
	//private PVector			_gravity = new PVector(0, 1.8f);
	private int				_fade = 255;
	private float 			_rot;
	
	public Feather( PImage feather, PVector velocity )
	{
		_pa			=	ApplicationFacade.app;
		_image 		=	feather;
		_velocity	=	velocity;
		_rot		=	_pa.radians(_pa.random(360) );
		
		_position 	=	new PVector();		
	}
	
	public void draw()
	{
		_position.add(_velocity);
		//_position.add(_gravity);
		
		_pa.pushMatrix();
			_pa.rotate(_rot);
			_pa.tint(255, _fade);
			_pa.image( _image, _position.x, _position.y );
			_pa.noTint();
		_pa.popMatrix();
		
		_fade -= 3;
		_velocity.mult(.95f);
	}

}
