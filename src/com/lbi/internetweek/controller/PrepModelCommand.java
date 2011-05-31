/*
  Login Example - J2ME / PureMVC Example
  Copyright(c) 2008 Jari Kemppinen <jari@viddem.com>
  Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.model.PhysicsProxy;
import com.lbi.internetweek.model.TwitterProxy;

public class PrepModelCommand extends SimpleCommand
{
    public void execute(INotification notification)
    {
        this.facade.registerProxy( new AppProxy() );
        this.facade.registerProxy( new PhysicsProxy() );
        this.facade.registerProxy( new TwitterProxy() );
    }
}