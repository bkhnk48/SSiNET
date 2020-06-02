package network.states.packet;

import events.AGenerationEvent;
import events.DReachingENBEvent;
import infrastructure.state.State;
import network.elements.EntranceBuffer;
import network.elements.Packet;
import network.elements.SourceQueue;

public class StateP4 extends State{
	//�	State P4: the packet is located at ENB of switch.

    public Packet packet;

    public StateP4(EntranceBuffer entranceBuffer, Packet p, DReachingENBEvent ev)
    {
        this.element = entranceBuffer;
        this.packet = p;
        //this.ancestorEvent = ev;
    }

    @Override
    public void act(){

    }
}
