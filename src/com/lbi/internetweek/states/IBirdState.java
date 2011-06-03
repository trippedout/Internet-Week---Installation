package com.lbi.internetweek.states;

import com.lbi.internetweek.model.AppProxy;

import processing.core.PImage;
import twitter4j.Status;

public interface IBirdState 
{
	void draw();
	void setState(IBirdState state);
}
