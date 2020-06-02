package infrastructure.event;

import infrastructure.element.Element;
import network.elements.Packet;

public abstract class Event {
	protected Packet packet; //packet ID
	protected long startTime;
	protected long endTime;

	protected Element element;

	public Packet getPacket()
	{
		return packet;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public Element getElement()
	{
		return element;
	}
	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public void execute()
	{}
}
