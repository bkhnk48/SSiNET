package infrastructure.state;

import infrastructure.element.Element;
import infrastructure.event.Event;

public abstract class State {
	//public Event ancestorEvent;
	public Element element;
	public void act() {}
	public void getNextState(Element e) {}

}
