package simulator;

import java.util.ArrayList;
import java.util.List;

import common.StdOut;
import config.Constant;
import infrastructure.event.Event;
import network.elements.EntranceBuffer;
import network.elements.ExitBuffer;
import network.elements.UnidirectionalWay;
import network.entities.Host;
import network.entities.Link;
import network.entities.SourceNode;
import network.entities.Switch;

public class DiscreteEventSimulator extends Simulator {
	public int numReceived = 0;
    public long receivedPacketPerUnit[];
    public int numSent = 0;
    public int numLoss = 0;
    public long totalPacketTime = 0;
    public int numEvent = 0;
    private boolean isLimit;
    private double timeLimit;
    private boolean verbose;
    public long totalHop = 0;



    public DiscreteEventSimulator(boolean isLimit, double timeLimit, boolean verbose) {
        super();
        this.isLimit = isLimit;
        this.verbose = verbose;
        this.timeLimit = timeLimit;
        this.receivedPacketPerUnit = new long[(int)(timeLimit/ Constant.EXPERIMENT_INTERVAL +1)];
    }

    public double getTime() {
        return currentTime;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public void start () {
		stopped = false;
		simulating = true;
		try {
			long startTime = System.currentTimeMillis();
			int lastPercentage = 0;
			while (!stopped && (!isLimit || currentTime < timeLimit)) {
				this.currentEvents = new ArrayList<>();
				//Loc ra tat ca cac event sap ket thuc o cac thiet bi
				addCurrentEventsFromDevices(currentTime);
				for (Event event : currentEvents) {
					event.execute();
				}
				currentTime = selectNextCurrentTime(currentTime);
				int percentage = (int) currentTime / (int) Constant.EXPERIMENT_INTERVAL;
				if (percentage > lastPercentage) {
					lastPercentage = percentage;
					//StdOut.printProgress("Progress", startTime, (long) timeLimit, currentTime);
				}
			}
			StdOut.print("\r");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			stopped = true;
			simulating = false;
		}
	}

    public boolean isVerbose() {
        return verbose;
    }

    public void log(String message) {
        if (this.verbose) {
            StdOut.printf("At %d: %s\n", (long) this.getTime(), message);
        }
    }



    public void addCurrentEventsFromDevices(long currentTime) {
		// todo chu y kiem tra moi thu extends Element Class
		List<Host> allHosts = this.topology.getHosts();
		for (Host host : allHosts) {
			if (host instanceof SourceNode) {
				//soonestEndTime will be updated later as events are executed
				if (host.physicalLayer.sourceQueue.getSoonestEndTime() == currentTime) {
					addCurrentEventsFromList(host.physicalLayer.sourceQueue.allEvents);
				}
				//soonestEndTime will be updated later as events are executed

				//add uniWay of host(way from it)
				UnidirectionalWay unidirectionalWay = host.physicalLayer.links.get(host.getId()).getWayToOtherNode(host);
				if (unidirectionalWay.getSoonestEndTime() == currentTime) {
					addCurrentEventsFromList(unidirectionalWay.allEvents);
				}

				int connectedNodeID = host.physicalLayer.links
						.get(host.getId()).getOtherNode(host).getId();
				if (host.physicalLayer.exitBuffers.get(connectedNodeID).getSoonestEndTime() == currentTime) {
					addCurrentEventsFromList(host.physicalLayer.exitBuffers.get(connectedNodeID).allEvents);//add events of EXB of hosts
				}
			}


		}
		//lay event tu tat ca ca link truoc
		List<Switch> allSwitches = this.topology.getSwitches();
		for (Switch aSwitch : allSwitches) {
			//add uniWay of switch(way from it)
			for (Link link : aSwitch.physicalLayer.links.values()) {
				if (link.getWayToOtherNode(aSwitch).getSoonestEndTime() == currentTime) {
					addCurrentEventsFromList(link.getWayToOtherNode(aSwitch).allEvents);
				}
			}
		}
		//den exb
		for (Switch aSwitch : allSwitches) {
			for (ExitBuffer exitBuffer : aSwitch.physicalLayer.exitBuffers.values()) {
				if (exitBuffer.getSoonestEndTime() == currentTime) {
					addCurrentEventsFromList(exitBuffer.allEvents);
				}
			}
		}
		for (Switch aSwitch : allSwitches) {
			for (EntranceBuffer entranceBuffer : aSwitch.physicalLayer.entranceBuffers.values()) {
				if (entranceBuffer.getSoonestEndTime() == currentTime) {
					addCurrentEventsFromList(entranceBuffer.allEvents);
				}
			}
		}

	}

    public void addCurrentEventsFromList(ArrayList<Event> allEvents)
    {
    	if(allEvents != null)
		{
			if(allEvents.size() > 0)
			{
				for(Event e: allEvents)
				{
					if(e.getEndTime() == this.currentTime)
					{
						this.currentEvents.add(e);
					}
				}
			}
		}
    }

    public long selectNextCurrentTime(long currentTime)
    {
    	long result = Long.MAX_VALUE;
    	List<Host> allHosts = this.topology.getHosts();
		for(Host host : allHosts)
		{
			if(host instanceof SourceNode) {
				if (result > host.physicalLayer.sourceQueue.getSoonestEndTime()
						&& host.physicalLayer.sourceQueue.getSoonestEndTime() >= currentTime
				) {
					result = host.physicalLayer.sourceQueue.getSoonestEndTime();
				}

				int connectedNodeID = host.physicalLayer.links
						.get(host.getId()).getOtherNode(host).getId();

				//check in EXB
				if (result > host.physicalLayer.exitBuffers.get(connectedNodeID).getSoonestEndTime()
						&& host.physicalLayer.exitBuffers.get(connectedNodeID).getSoonestEndTime() >= currentTime
				) {
					result = host.physicalLayer.exitBuffers.get(connectedNodeID).getSoonestEndTime();
				}

				// check in uniWay
				long time = host.physicalLayer.links.get(host.getId()).getWayToOtherNode(host).getSoonestEndTime();
				if (result > time && time >= currentTime
				) {
					result = time;
				}
			}
		}
		List<Switch> allSwitches = this.topology.getSwitches();
		for(Switch aSwitch : allSwitches)
		{
			for(EntranceBuffer entranceBuffer : aSwitch.physicalLayer.entranceBuffers.values()){
				if(result > entranceBuffer.getSoonestEndTime()
						&& entranceBuffer.getSoonestEndTime() >= currentTime) {
					result = entranceBuffer.getSoonestEndTime();
				}
			}
			for(ExitBuffer exitBuffer : aSwitch.physicalLayer.exitBuffers.values()){
				if(result > exitBuffer.getSoonestEndTime()
						&& exitBuffer.getSoonestEndTime() >= currentTime) {
					result = exitBuffer.getSoonestEndTime();
				}
			}

			//add uniWay of switch(way from it)
			for(Link link : aSwitch.physicalLayer.links.values()){
				long time = link.getWayToOtherNode(aSwitch).getSoonestEndTime();
				if(result > time && time >= currentTime) {
					result = time;
				}
			}

		}
    	return result;
    }
    
}
