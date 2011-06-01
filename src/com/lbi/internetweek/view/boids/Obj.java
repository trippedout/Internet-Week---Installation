package com.lbi.internetweek.view.boids;

import com.lbi.internetweek.utils.Vector2D;

public class Obj
{
	Flock fl;
	Vector2D pos;
	int rad = 45;
	int aura;

	public Obj(Flock flock, Vector2D p)
	{
		fl = flock;
		pos = p;
		aura = 4 * rad;
	}
}
