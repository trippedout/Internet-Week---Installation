package com.lbi.internetweek.states;

import twitter4j.Status;

public interface IBirdState 
{
	void draw();
	void setState(IBirdState state);
	
	boolean setTweetState();
}
