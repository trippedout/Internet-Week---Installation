package com.lbi.internetweek.view.components;

import processing.core.PApplet;
import processing.core.PConstants;
import net.nexttext.Book;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;

import de.looksgood.ani.Ani;

public class Tweet
{
	private Installation 		_pa;
	private TweetsView			_view;

	private int					_life			=	360;
	private int 				x, y;
	
	private boolean				_isAnimatingIn	=	false;
	public float				littleScale 	=	0f;
	public float				mediumScale 	= 	0f;
	public float				largeScale 		= 	0f;
	public int	 				textAlpha		=	0;

	private String _text;

	public Tweet(String str, TweetsView tweetsView)
	{
		_pa			=	ApplicationFacade.app;
		
		_text		=	str;
		_view		=	tweetsView;
		
		_pa.println( "Tweet: create() " + this );
	}

	public void draw(float x2, float y2)
	{
		_life--;
		
		_pa.imageMode(PConstants.CENTER);
		
		_pa.smooth();
		_pa.pushMatrix();		
			_pa.translate(x2 + 75, y2 - 125);
			
			_pa.pushMatrix();
				_pa.scale( littleScale, littleScale );
				_pa.image( _view.little, -90, 0);
			_pa.popMatrix();
			
			_pa.pushMatrix();
				_pa.scale( mediumScale, mediumScale );
				_pa.image( _view.medium, -72, -65);
			_pa.popMatrix();
			
			_pa.pushMatrix();
				_pa.scale( largeScale, largeScale );
				_pa.image( _view.large, 125, 15);
			_pa.popMatrix();
			
			_pa.fill(0,textAlpha);
			_pa.textAlign(_pa.CENTER);
			_pa.textSize(25);
//			_pa.text( _text, 42, -25, 180, 120 );
			_pa.text( _text, -42, -100, 320, 320 );
			_pa.noFill();
			
		_pa.popMatrix();
		_pa.noSmooth();
		
		_pa.imageMode(PConstants.CORNER);
	}

	public void animateIn()
	{
		Ani.to(this, .6f, .2f, "littleScale", 1, Ani.BACK_OUT );
		Ani.to(this, .6f, .4f, "mediumScale", 1.5f, Ani.BACK_OUT );
		Ani.to(this, .6f, .6f, "largeScale", 1, Ani.BACK_OUT, "onEnd:onAnimateInComplete" );
		Ani.to(this, .6f, .8f, "textAlpha", 255, Ani.BACK_OUT );		
	}
	
	public void onAnimateInComplete( Ani ani )
	{
		
	}
	
	public void kill()
	{
		_pa.println( "Tweet: kill() " + this );
	}

	public boolean hasLife()
	{
		return _life > 0;
	}


}
