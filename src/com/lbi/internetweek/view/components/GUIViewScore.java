package com.lbi.internetweek.view.components;

import processing.core.PApplet;
import processing.core.PConstants;
import net.nexttext.Book;
import net.nexttext.TextPage;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.model.AppProxy;

public class GUIViewScore
{
	private Installation		_pa;
	private Book 				_book;
	private int					_score;
	
	public GUIViewScore()
	{
		_pa			=	ApplicationFacade.app;
		
		
	}

	public void update(int newScore)
	{
		PApplet.println("GUIViewScore: update() " + newScore );
		
		_score = newScore;
	}
	
	public void draw()
	{	
		_pa.fill(255);
		_pa.textFont( AppProxy.getScoreFont() );
		_pa.textAlign(PConstants.CENTER);
		_pa.textSize(72);
		_pa.text(_score, 0, 0);
		_pa.noFill();
	}

}