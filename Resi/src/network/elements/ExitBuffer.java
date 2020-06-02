package network.elements;

import config.Constant;
import infrastructure.element.LimitedBuffer;
import infrastructure.entity.Node;
import network.states.exb.X00;
import network.states.exb.X01;
import network.states.exb.X10;
import network.states.exb.X11;

import java.util.ArrayList;

public class ExitBuffer extends LimitedBuffer {
	protected ArrayList<EntranceBuffer> requestList;


	public ExitBuffer(Node node, Node connectNode, int size)
	{
		this.node = node;
		this.size = size;
		this.connectNode = connectNode;
		this.requestList = new ArrayList<>();
		this.setState( new X01(this));
	}

	public ArrayList<EntranceBuffer> getRequestList() {
		return requestList;
	}
	public void addToRequestList(EntranceBuffer entranceBuffer){
		requestList.add(entranceBuffer);
	}

	public void removeFromRequestList(EntranceBuffer entranceBuffer){
		if(requestList.contains(entranceBuffer)){
			requestList.remove(entranceBuffer);
		}else
			System.out.println("ERROR: ExitBuffer: " + this.toString() + " does not contain request id: " + id);
	}
	public boolean isRequestListEmpty(){
		return requestList.isEmpty();
	}

	@Override
	public void checkStateChange(){ }
}
