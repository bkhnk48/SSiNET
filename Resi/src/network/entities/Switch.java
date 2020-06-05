package network.entities;

import custom.fattree.FatTreeRoutingAlgorithm;
import infrastructure.entity.Node;
import network.elements.EntranceBuffer;
import network.elements.Packet;
import routing.RoutingAlgorithm;

/**
 * Created by Dandoh on 6/27/17.
 */
public class Switch extends Node {
	public int numPorts = 0;

	public Switch(int id)
	{
		super(id);
	}

}