package network.states.exb;

import config.Constant;
import events.CLeavingEXBEvent;
import events.FLeavingSwitchEvent;
import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.ExitBuffer;
import network.elements.Packet;
import network.entities.SourceNode;
import network.entities.Switch;

public class X11 extends State {
	//�	State X11: EXB is full and able to transfer packet.

    public X11(ExitBuffer exitBuffer){
        this.element = exitBuffer;
    }

    @Override
    public void act(){
        ExitBuffer exitBuffer = (ExitBuffer)this.element;
        Packet packet = exitBuffer.getPeekPacket();
        if(packet != null){
            if(!(exitBuffer.hasEventOfPacket(packet))){
                //todo xem neu can viet ham set trang thai co packet ve dung P2 hoac P5
                if(exitBuffer.getNode() instanceof SourceNode){
                    long time = exitBuffer.physicalLayer.simulator.time();
                    Event event = new CLeavingEXBEvent(time, time, exitBuffer, packet);
                    exitBuffer.insertEvents(event); //chen them su kien moi vao
                }
                else if(exitBuffer.getNode() instanceof Switch){
                    long time = exitBuffer.physicalLayer.simulator.time();
                    Event event = new FLeavingSwitchEvent(time, time + Constant.SWITCH_CYCLE, exitBuffer, packet);
                    exitBuffer.insertEvents(event); //chen them su kien moi vao
                }
            }
        }

    }
}
