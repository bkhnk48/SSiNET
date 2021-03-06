package events;

import config.Constant;
import infrastructure.element.Element;
import infrastructure.event.Event;
import infrastructure.state.Type;
import network.elements.EntranceBuffer;
import network.elements.ExitBuffer;
import network.elements.Packet;
import network.elements.UnidirectionalWay;

import network.entities.Switch;
import network.states.enb.N0;
import network.states.enb.N1;

/*import network.states.packet.SStateP3;
import network.states.packet.StateP4;*/
import network.states.unidirectionalway.W0;
import network.states.unidirectionalway.W1;
import network.states.unidirectionalway.W2;
import simulator.DiscreteEventSimulator;

enum TypeD
{
	D, D1, D2
}

public class DReachingENBEvent extends Event {
	public TypeD type = TypeD.D;
	//Event dai dien cho su kien loai (D): goi tin den duoc ENB cua nut tiep theo
	public DReachingENBEvent(DiscreteEventSimulator sim, long startTime, long endTime, Element elem, Packet p)
	{
		super(sim, endTime);
		//countSubEvent++;
		this.startTime = startTime;
		this.endTime = endTime;
		this.element = elem;
		this.packet = p;
	}
	@Override
	public void actions()
	{
		//if(getElement() instanceof UnidirectionalWay) 
		{
			UnidirectionalWay unidirectionalWay = (UnidirectionalWay)element;
			
			EntranceBuffer entranceBuffer = unidirectionalWay.getToNode().physicalLayer.entranceBuffers
					.get(unidirectionalWay.getFromNode().getId());

			if(//packet.getState() instanceof SStateP3 
				packet.getState().type == Type.P3
					&& unidirectionalWay.getState() instanceof W1
					&& unidirectionalWay.getToNode() instanceof Switch && entranceBuffer.getState() instanceof N0
					&& unidirectionalWay.getPacket() == packet
			){
				unidirectionalWay.removePacket();
				entranceBuffer.insertPacket(packet);

				//change state packet
				//packet.setState(new StateP4(entranceBuffer, packet, this));
				packet.setType(Type.P4);
				//packet.getState().act();

				if (entranceBuffer.isFull()) {
					type = TypeD.D2; // ENB full
					//change state//ENB
					entranceBuffer.setState(new N1(entranceBuffer));
					entranceBuffer.getState().act();
					//change state of way
					unidirectionalWay.setState(new W2(unidirectionalWay));
					unidirectionalWay.getState().act();
				} else {
					type = TypeD.D1; // ENB not full
					//change state of EXB
					ExitBuffer sendExitBuffer = unidirectionalWay.getFromNode().physicalLayer
							.exitBuffers.get(unidirectionalWay.getToNode().getId());
					if (sendExitBuffer.getState().type == Type.X00) {
						//sendExitBuffer.setState(new X01(sendExitBuffer));
						sendExitBuffer.setType(Type.X01);
						sendExitBuffer.getState().act();
					}
					if (sendExitBuffer.getState().type == Type.X10) {
						//sendExitBuffer.setState(new X11(sendExitBuffer));
						sendExitBuffer.setType(Type.X11);
						sendExitBuffer.getState().act();
					}
					//change state of way
					unidirectionalWay.setState(new W0(unidirectionalWay));
					unidirectionalWay.getState().act();
				}

				entranceBuffer.getNode().getNetworkLayer().route(entranceBuffer);
			}
		}
		//else 
		{
			//System.out.println("ERROR: Event " + this.toString() + "khong the chua element: " + getElement().toString());
		}
	}
}
