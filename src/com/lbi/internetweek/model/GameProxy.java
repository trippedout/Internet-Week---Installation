package com.lbi.internetweek.model;

import org.puremvc.java.patterns.proxy.Proxy;

public class GameProxy extends Proxy
{
	public static final String 	NAME 			=	"GameProxy";	
	public static final String 	SCORE_UPDATED 	=	"score_updated";
	
	private int			_score		=	0;

	public GameProxy()
	{
		super(NAME);
	}
	
	public void addPointToScore()
	{
		_score++;
		this.facade.sendNotification( SCORE_UPDATED, _score );
	}
	
	public void resetScore()
	{
		_score = 0;
		this.facade.sendNotification( SCORE_UPDATED, _score );
	}

}
