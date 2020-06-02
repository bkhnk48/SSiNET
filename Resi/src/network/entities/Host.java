package network.entities;

import infrastructure.event.Event;
import events.AGenerationEvent;
import infrastructure.entity.Node;
import network.elements.SourceQueue;
import network.layers.PhysicalLayer;



/**
 * Created by Dandoh on 6/27/17.
 */


public class Host extends Node {



	public Host(int id) {
	     super(id);
//	     this.physicalLayer = new PhysicalLayer(this);
	}

   @Override
    public void clear() {

    }
   
   public void receive()
   {
	   
   }
}