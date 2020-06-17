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
		if(sim.allEvents == null) return false;
		if(sim.allEvents.isEmpty()) return false;
		if(packet == null) return false;
		else
		{ 
			for(Event event : sim.allEvents)
			{ 
				if(event.getPacket() == packet) 
					return true; 
			} 
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
		long endTime = ev.getEndTime();
		
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
		/*
		 * for(i = 0; i < sim.allEvents.size(); i++ ) {
		 * if(sim.allEvents.get(i).getEndTime() > endTime) { break; } }
		 */
		//sim.allEvents.add(i, ev);
		/*
		 * if(ev instanceof EMovingInSwitchEvent) {
		 * System.out.println("Event is added?"); }
		 */
		sim.insertEvent(ev);
		updateSoonestEndTime();
	}

	public void updateSoonestEndTime()
	{
		if(sim.allEvents == null) { setSoonestEndTime(Long.MAX_VALUE); return; }
		if(sim.allEvents.size() == 0) { setSoonestEndTime(Long.MAX_VALUE); return; }
		  setSoonestEndTime(sim.allEvents.get(0).getEndTime());
		 
	}

	public void removeExecutedEvent(Event ev)
	{
		int index = 0; //sim.allEvents.indexOf(ev);
		try {
			//int[] minMax = sim.getMinMaxIndex(ev.getEndTime());
			
			//for(index = minMax[0]; index < minMax[1]; index++)
			/*int max = sim.allEvents.size();
			for(index = 0; index < max; index++)
			{
				if(ev == sim.allEvents.get(index))
				{
					break;
				}
			}*/
			
			sim.allEvents.remove(index);
			sim.removeOneElement(ev.getEndTime());
			
			/*
			 * for(int i = index; i < sim.allEvents.size() -1 ; i++) { sim.allEvents.set(i,
			 * sim.allEvents.get(i+1)); } sim.allEvents.remove(sim.allEvents.size() -1);
			 */
			if (sim.allEvents.isEmpty())
				setSoonestEndTime(Long.MAX_VALUE);
			else setSoonestEndTime(sim.allEvents.get(0).getEndTime());
		}
		catch(Exception e)
		{
			System.out.println("index = " + index + " ev: " + ev.toString() + " allEvents.size: " + sim.allEvents.size());
		}
		
	}
	

}
