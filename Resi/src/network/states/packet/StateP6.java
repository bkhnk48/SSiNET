package network.states.packet;

import events.EMovingInSwitchEvent;
import events.FLeavingSwitchEvent;
import events.GReachingDestinationEvent;
import infrastructure.state.State;
import network.elements.Packet;
import network.elements.SourceQueue;
import network.entities.DestinationNode;

public class StateP6 extends State {
	//�	State P6: the packet is received by the destination node.

    public Packet packet;

    public StateP6(Packet p, GReachingDestinationEvent ev)
    {
        this.element = null;
        this.packet = p;
        //this.ancestorEvent = ev;
    }

    @Override
    public void act(){

    }
}
