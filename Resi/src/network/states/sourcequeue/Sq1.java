package network.states.sourcequeue;

import infrastructure.event.Event;
import events.AGenerationEvent;
import network.elements.SourceQueue;
import infrastructure.state.State;

public class Sq1 extends State {
	//�	State Sq1: source queue is empty.
	public Sq1(SourceQueue e)
	{
		this.element = e;
	}
	
	/**
	 * Phuong thuc act dung de goi khi ma mot phan tu thay doi trang thai
	 * O day, phan tu Source queue khi o trang thai Sq1 thi
	 * no se kiem tra xem danh sach cac su kien (sap xay ra) co
	 * su kien sinh goi tin tiep theo chua?
	 * Neu chua se tao ra su kien nay. Thoi diem xay ra su kien nay la
	 * tuong lai (mot Constant.HOST_DELAY nua)
	 */
	@Override
	public void act()
	{
		SourceQueue sourceQueue = (SourceQueue) element;
		if(notYetAddGenerationEvent(sourceQueue))//Kiem tra xem Source Queue da co event tao goi tin moi chua?
		{
			long time = (long)sourceQueue.getNextPacketTime();
			Event event = new AGenerationEvent(time, time, element);
			sourceQueue.insertEvents(event);//ma nguon cu dung pthuc add la khong dung
		}
	}
	
	public boolean notYetAddGenerationEvent(SourceQueue sourceQueue)
	//Kiem tra xem Source Queue da co event tao goi tin moi chua khi ma da co P moi duoc tao ra(source het delay)
	{
		long nextPacketTime = (long)sourceQueue.getNextPacketTime();
		for(int i = 0; i < sourceQueue.allEvents.size(); i++)
		{
			if(sourceQueue.allEvents.get(i) instanceof AGenerationEvent)
			{
				// dau == vi neu cho them lon hon thi evevt A xay ra nhung se ko co goi tin duoc tao moi, ly do xem trong ham SourceQueue.generatePacket()
//				  neu thoi gian tao packet tiep theo bang thoi gian bat dau event
				if(sourceQueue.allEvents.get(i).getStartTime() == nextPacketTime)
				{
					return false;
				}
			}
		}
		return true;
	}
}
