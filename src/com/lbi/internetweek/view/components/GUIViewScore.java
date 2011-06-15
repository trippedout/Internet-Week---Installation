package com.lbi.internetweek.view.components;

import processing.core.PApplet;
import processing.core.PConstants;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.model.AppProxy;

public class GUIViewScore
{
	private Installation		_pa;
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
		_pa.textAlign(PConstants.CENTER);
		
		_pa.textFont( AppProxy.getScoreTextFont() );
		_pa.textSize(17);
		_pa.text("BIRDS SMACKED:", 0, -22);
		
		_pa.textFont( AppProxy.getScoreFont() );
		_pa.textSize(56);
		_pa.text(_score, 0, 30);
		
		_pa.noFill();
	}

}