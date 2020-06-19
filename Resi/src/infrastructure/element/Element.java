package infrastructure.element;

import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.Packet;
import simulator.DiscreteEventSimulator;
import infrastructure.state.*;

import java.util.ArrayList;

import events.EMovingInSwitchEvent;

public abstract class Element {
	protected int id;
	protected State state;
	protected long soonestEndTime = Long.MAX_VALUE; /// todo check NHONLV change from 0 to max
	//public ArrayList<Event> allEvents;
	public DiscreteEventSimulator sim;

	public Element(){
		//allEvents = new ArrayList<Event>();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setState(State state)
	{
		this.state = state;
	}
	
	public void setType(Type type)
	{
		if(this.state == null)
			this.state = new State();
		this.state.type = type;
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
		if(sim == null) return false;
		//if(sim.allEvents == null) return false;
		//if(sim.allEvents.isEmpty()) return false;
		if(packet == null) return false;
		else
		{ 
			/*for(Event event : sim.allEvents)
			{ 
				if(event.getPacket() == packet) 
					return true; 
			}*/ 
			return false; 
		}
		/*
		 * if(allEvents == null) return false; else if(allEvents.isEmpty()) return
		 * false; else if(packet == null) return false; else{ for(Event event :
		 * allEvents){ if(event.getPacket() == packet) return true; } return false; }
		 */
	}

	/**
	 * Xay dung phuong thuc insertEvent thuc hien viec
	 * chen mot Event co ten la ev.
	 * @param ev
	 */
	public void insertEvents(Event ev)
	{
		sim.addEvent(ev);
		/*long endTime = ev.getEndTime();
		
		if(sim == null)
			return;
		if(sim.allEvents == null)
		{
			sim.allEvents = new ArrayList<Event>();
			sim.allEvents.add(ev);
			sim.ongoingExecutionTimes.put(endTime, 0);
			return;
		}
		if(sim.allEvents.size() == 0)
		{
			sim.allEvents.add(ev);
			updateSoonestEndTime();
			sim.ongoingExecutionTimes.put(endTime, 0);
			return;
		}
		
		sim.insertEvent(ev);
		updateSoonestEndTime();*/
	}

	

	
	

}
