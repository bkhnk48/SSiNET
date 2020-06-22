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
   
   public void generatePacket(int destination)
   {
       if(this.physicalLayer.sourceQueue == null)
           this.physicalLayer.sourceQueue = new SourceQueue(this.id, destination);
       else
           this.physicalLayer.sourceQueue.setDestinationID(destination);

       long time = (long)this.physicalLayer.simulator.time();
       Event ev = new AGenerationEvent(this.physicalLayer.simulator, time, time, this.physicalLayer.sourceQueue);
       this.physicalLayer.sourceQueue.insertEvents(ev);
   }
}