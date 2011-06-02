/*
  Login Example - J2ME / PureMVC Example
  Copyright(c) 2008 Jari Kemppinen <jari@viddem.com>
  Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.ICommand;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.view.BackgroundMediator;
import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.GUIMediator;
import com.lbi.internetweek.view.KinectMediator;
import com.lbi.internetweek.view.PoofMediator;
import com.lbi.internetweek.view.TweetsMediator;

public class PrepViewCommand extends SimpleCommand implements ICommand
{
    public void execute(INotification notification)
    {
		this.facade.registerMediator( new BackgroundMediator() );
		this.facade.registerMediator( new KinectMediator() );
		this.facade.registerMediator( new BirdsMediator() );
		this.facade.registerMediator( new TweetsMediator() );
		this.facade.registerMediator( new PoofMediator() );
		this.facade.registerMediator( new GUIMediator() );
		
		this.facade.registerCommand(AppProxy.SCREEN_UPDATED, new ScreenUpdatedCommand() );
		
		this.facade.sendNotification(AppProxy.SCREEN_UPDATED);
    }
}
