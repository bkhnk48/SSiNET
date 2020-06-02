package infrastructure.element;

import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.Packet;

import java.util.ArrayList;

public abstract class Element {
	protected int id;
	protected State state;
	protected long soonestEndTime = Long.MAX_VALUE; /// todo check NHONLV change from 0 to max
	public ArrayList<Event> allEvents;

	public Element(){
		allEvents = new ArrayList<Event>();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public void setSoonestEndTime(long soonestEndTime) {
		this.soonestEndTime = soonestEndTime;
	}

	public int getId() {
		return id;
	}

	public State getState() {
		return state;
	}

	public long getSoonestEndTime() { return soonestEndTime; }

	public void getNextState() { }

	public boolean hasEventOfPacket(Packet packet)
	{
		if(allEvents == null) return false;
		else if(allEvents.isEmpty()) return false;
		else if(packet == null) return false;
		else{
			for(Event event : allEvents){
				if(event.getPacket() == packet) return true;
			}
			return false;
		}
	}

	/**
	 * Xay dung phuong thuc insertEvent thuc hien viec
	 * chen mot Event co ten la ev.
	 * @param ev
	 */
	public void insertEvents(Event ev)
	{
		long endTime = ev.getEndTime();
		int i = 0 ;
		if(allEvents == null)
		{
			allEvents = new ArrayList<Event>();
			allEvents.add(ev);
			return;
		}
		if(allEvents.size() == 0)
		{
			allEvents.add(ev);
			updateSoonestEndTime();
			return;
		}
		for(i = 0; i < allEvents.size(); i++ )
		{
			if(allEvents.get(i).getEndTime() > endTime)
			{
				break;
			}
		}
		allEvents.add(i, ev);
		updateSoonestEndTime();
	}

	public void updateSoonestEndTime()
	{
		if(allEvents == null)
		{
			setSoonestEndTime(Long.MAX_VALUE);
			return;
		}
		if(allEvents.size() == 0)
		{
			setSoonestEndTime(Long.MAX_VALUE);
			return;
		}
		setSoonestEndTime(allEvents.get(0).getEndTime());
	}

	public void removeExecutedEvent(Event ev)
	{
		int index = allEvents.indexOf(ev);
		for(int i = index; i < allEvents.size() -1 ; i++)
		{
			allEvents.set(i, allEvents.get(i+1));
		}
		allEvents.remove(allEvents.size() -1);
		if (allEvents.isEmpty())
			setSoonestEndTime(Long.MAX_VALUE);
		else setSoonestEndTime(allEvents.get(0).getEndTime());
	}
	

}
