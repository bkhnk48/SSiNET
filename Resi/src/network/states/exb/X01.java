package network.states.exb;

import config.Constant;
import events.BLeavingSourceQueueEvent;
import events.CLeavingEXBEvent;
import events.FLeavingSwitchEvent;
import infrastructure.entity.Node;
import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.ExitBuffer;
import network.elements.Packet;
import network.elements.SourceQueue;
import network.entities.SourceNode;
import network.entities.Switch;

public class X01 extends State {
	//�	State X01: EXB is not full and able to transfer packet.

    public X01(ExitBuffer exitBuffer){
        this.element = exitBuffer;
    }

    @Override
    public void act(){
        //giong X00
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
            // todo goi event E( goi ham controlFlow)
            exitBuffer.getNode().networkLayer.controlFlow(exitBuffer);
        }

        Packet packet = exitBuffer.getPeekPacket();
        if(packet != null){
            if(!(exitBuffer.hasEventOfPacket(packet))){
                if(exitBuffer.getNode() instanceof SourceNode){
                    long time = exitBuffer.physicalLayer.simulator.time();
                    Event event = new CLeavingEXBEvent(time, time, exitBuffer, packet);
                    exitBuffer.insertEvents(event); //chen them su kien moi vao
                }
                else if(exitBuffer.getNode() instanceof Switch){
                    long time = exitBuffer.physicalLayer.simulator.time();
                    Event event = new FLeavingSwitchEvent(time, time + Constant.SWITCH_CYCLE, exitBuffer, packet);
                    exitBuffer.insertEvents(event); //chen them su kien moi vao
                }
            }
        }

    }
}
