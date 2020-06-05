package network.states.exb;

import infrastructure.state.State;
import network.elements.ExitBuffer;

public class X10 extends State {
	//�	State X10: EXB is full and unable to transfer packet (due to the next ENB is full).
    public X10(ExitBuffer exitBuffer){
        this.element = exitBuffer;
        countStateEXB++;
    }

    @Override
    public void act(){

    }
}
