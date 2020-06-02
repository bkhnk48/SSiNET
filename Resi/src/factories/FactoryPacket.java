package factories;

import infrastructure.element.Element;
import network.elements.Packet;
import network.entities.Host;

public class FactoryPacket extends Factory{
	private Host sourceNode;
	private int generatedPacketNumber;

	public FactoryPacket(Host sourceNode){
		this.sourceNode = sourceNode;
	}
	//NhonLV comment, do not use this factory currently
//	public Packet generatePacket(long currentTime, int source, int destination, double startTime){
//		Packet packet = new Packet(id, source, destination, startTime);
//		packet.state = new StateP1(sourceNode.physicalLayer.sourceQueue, packet);
//		return packet;
//	}

	public void updateState(Packet p, Element e)
	{
		/*if(p.state == null && (e instanceof SourceQueue))
		{
			p.state = new StateP1();
		}
		else {
			p.state.getNextState(p);
		}*/
		
	}
}
