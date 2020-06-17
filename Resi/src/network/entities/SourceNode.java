package network.entities;

import events.AGenerationEvent;
import factories.FactoryPacket;
import infrastructure.event.Event;
import network.elements.SourceQueue;

public class SourceNode extends Host {
    private FactoryPacket factoryPacket;

    public SourceNode(int id){
        super(id);
        factoryPacket = new FactoryPacket(this);
    }

    public void generatePacket(int destination)
    {
        if(this.physicalLayer.sourceQueue == null)
            this.physicalLayer.sourceQueue = new SourceQueue(this.id, destination);
        else
            this.physicalLayer.sourceQueue.setDestinationID(destination);

        long time = (long)this.physicalLayer.simulator.time();
        Event ev = new AGenerationEvent(time, time, this.physicalLayer.sourceQueue);
        this.physicalLayer.sourceQueue.insertEvents(ev);
    }
}
