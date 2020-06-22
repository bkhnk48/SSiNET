package network.layers;

import infrastructure.entity.Node;
import network.elements.*;
import infrastructure.entity.Device;
import network.entities.*;
import simulator.DiscreteEventSimulator;
import simulator.Simulator;

import java.util.HashMap;

public class PhysicalLayer { //only transfers packets from a node to links,

	public HashMap<Integer, ExitBuffer> exitBuffers;
	public HashMap<Integer, EntranceBuffer> entranceBuffers;
	public SourceQueue sourceQueue;
	public HashMap<Integer, Link> links; // rieng link host luu id la id cua host
	public DiscreteEventSimulator simulator;
	public Node node;

	/*
	 * public PhysicalLayer(SourceNode host) { entranceBuffers = null; exitBuffers =
	 * new HashMap<>(); sourceQueue = new SourceQueue(host.getId());
	 * sourceQueue.physicalLayer = this; this.node = host; this.links = new
	 * HashMap<>(); } public PhysicalLayer(DestinationNode host) { this.node = host;
	 * this.links = new HashMap<>(); }
	 */
	public PhysicalLayer(Host host)
	{
		if(host.type == TypeOfHost.Source || host.type == TypeOfHost.Mix)
		{
			entranceBuffers = null;
			exitBuffers = new HashMap<>();
			sourceQueue = new SourceQueue(host.getId());
			sourceQueue.physicalLayer = this;
			this.node = host;
			this.links = new HashMap<>();
		}
		if(host.type == TypeOfHost.Destionation || host.type == TypeOfHost.Mix)
		{
			this.node = host;
			this.links = new HashMap<>();
		}
	}
	

	public PhysicalLayer(Switch sw, int k)
	{
		entranceBuffers = new HashMap<>();
		exitBuffers = new HashMap<>();
		this.node = sw;
		this.links = new HashMap<>();
	}

	
	/*public void addLocationOfEvents()
	{
		sim.addLocationOfEvents(node);
	}*/
}
