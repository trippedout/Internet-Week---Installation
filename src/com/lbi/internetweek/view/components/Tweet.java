package com.lbi.internetweek.view.components;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;

public class Tweet
{
	private Installation 		_pa;

	private int					_life		=	300;
	private int 				x, y;

	private String _text;

	public Tweet(String str)
	{
		_pa			=	ApplicationFacade.app;
		_text		=	str;
		//x			=	sx;
		//y			=	sy;

		_pa.println( "Tweet: create() " + this );
	}

	public void draw(float x2, float y2)
	{
		_life--;
		
		_pa.pushMatrix();
		
			_pa.translate(x2, y2);
						
			_pa.fill(0);
			_pa.text(_text, 0, 0, 150, 200);
			_pa.noFill();
			
		_pa.popMatrix();
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
