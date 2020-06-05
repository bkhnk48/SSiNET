package network.states.exb;

import events.BLeavingSourceQueueEvent;
import infrastructure.element.Buffer;
import infrastructure.entity.Node;
import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.EntranceBuffer;
import network.elements.ExitBuffer;
import network.elements.Packet;
import network.elements.SourceQueue;
import network.entities.SourceNode;
import network.entities.Switch;

public class X00 extends State {
	//�	State X00: EXB is not full and unable to transfer packet (due to the next ENB is full
	// or this EXB is empty).

    /*public X00(ExitBuffer exitBuffer){
    	countStateEXB++;
        this.element = exitBuffer;
    }*/

    @Override
    // co the lay vao para de biet previous state la gi
    public void act(){
        //giong X01
        ExitBuffer exitBuffer = (ExitBuffer)this.element;
        Node currentNode = exitBuffer.getNode();
        if(currentNode instanceof SourceNode){
            SourceNode sourceNode = (SourceNode)currentNode;
            SourceQueue sourceQueue = sourceNode.physicalLayer.sourceQueue;
            Packet packet = sourceQueue.getPeekPacket();
            if(packet != null){
                if(!sourceQueue.hasEventOfPacket(packet)) {
                    long time = sourceQueue.physicalLayer.simulator.time();
                    Event event = new BLeavingSourceQueueEvent(time, time, sourceQueue, packet);
                    sourceQueue.insertEvents(event); //chen them su kien moi vao
                }
            }
        }
        else if(currentNode instanceof Switch){
            Switch sw = (Switch)currentNode;
            exitBuffer.getNode().networkLayer.controlFlow(exitBuffer);
        }

    }
}
