package com.ugc.facade;

import com.ugc.domain.FlushObject;

import java.util.List;

public interface FlushQueueFacade
{
     public void addObjectToFlushQueue(FlushObject fo);

     public void removeAll();

     public List<FlushObject> getObjects();

}
