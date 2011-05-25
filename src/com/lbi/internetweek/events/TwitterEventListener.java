package com.lbi.internetweek.events;

import java.util.EventListener;

public interface TwitterEventListener extends EventListener 
{
    public void onEvent(TwitterEvent evt);
}
