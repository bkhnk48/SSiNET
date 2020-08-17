package network.layers;

import java.util.HashMap;
import java.util.Map;

import javatuples.Pair;
import network.elements.Packet;

public class DataLinkLayer extends Layer {
	public Packet packet;
	// cap nhat thong tin packet
	public DataLinkLayer(Packet p){
		this.packet = p;
	}
	
	Map<Pair<Integer, Integer>, Integer> flowSizesPerDuration = new HashMap<>();
	Map<Integer, Integer> outgoingTraffic = new HashMap();
	
	public void update(Packet p)
	{
		
	}
}
