package network.entities;

import events.AGenerationEvent;
import infrastructure.event.Event;
import network.elements.SourceQueue;

public class SourceNode extends Host {
   

    public SourceNode(int id){
        super(id);
   
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
