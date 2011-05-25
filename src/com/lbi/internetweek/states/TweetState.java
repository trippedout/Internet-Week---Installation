package com.lbi.internetweek.states;

import twitter4j.Status;

import com.lbi.internetweek.views.Bird;

public class TweetState extends BirdState 
{	
	
	public TweetState( Bird b )
	{
		super(b);
	}

	public void draw() 
	{
		
	}
	
	@Override
	public boolean setTweetState()
	{
		return false;
	}

}
