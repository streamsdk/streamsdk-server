package com.ugc.facade;

import com.ugc.domain.FlushObject;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

@Service(value = "FlushQueueFacade")
public class FlushQueueFacadeImp implements FlushQueueFacade
{

    private List<FlushObject> objects = Collections.synchronizedList(new Vector<FlushObject>());

    public void addObjectToFlushQueue(FlushObject fo) {
        objects.add(fo);
    }

    public void removeAll() {
        objects.clear();
    }

    public List<FlushObject> getObjects() {
        return objects;
    }
}
