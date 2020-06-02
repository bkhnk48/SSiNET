package network.states.packet;

import events.BLeavingSourceQueueEvent;
import infrastructure.state.State;
import network.states.exb.X00;
import network.states.exb.X01;
import network.elements.ExitBuffer;
import infrastructure.event.Event;
import events.AGenerationEvent;
import network.elements.Packet;
import network.elements.SourceQueue;

public class StateP1 extends State {
	//�	State P1: the packet is generated
	public Packet packet;

	
	public StateP1(SourceQueue sq, Packet p, AGenerationEvent ev)
	{
		this.element = sq;
		this.packet = p;
		//this.ancestorEvent = ev;
	}

	@Override
	public void act(){

	}
}
