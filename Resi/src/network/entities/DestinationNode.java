package network.entities;

import config.Constant;
import network.elements.Packet;

public class DestinationNode extends Host {
    protected int receivedPacketInNode;
    protected double lastRx = 0; // thoi gian goi tin cuoi cung den host
    protected double firstTx = -1; //la thoi gian goi tin dau tien den host

    public DestinationNode(int id){
        super(id);
        this.receivedPacketInNode = 0;
    }

    public int getReceivedPacketInNode() {
        return receivedPacketInNode;
    }

    public double getLastRx() {
        return lastRx;
    }

    public double getFirstTx() {
        return firstTx;
    }

    public void receivePacket(Packet packet){
        double currentTime = this.physicalLayer.simulator.getTime();
        this.physicalLayer.simulator.numReceived++;
        if(this.receivedPacketInNode == 0) {
//            this.firstTx = packet.getStartTime();
            this.firstTx = currentTime;
            //System.out.println("Thoi gian goi tin dau tien den voi host " + self.id + " la: " + this.firstTx);
        }
        this.receivedPacketInNode ++;
        this.lastRx = currentTime;
        this.physicalLayer.simulator.receivedPacketPerUnit[(int)(currentTime / Constant.EXPERIMENT_INTERVAL + 1)]++;
        packet.setEndTime(currentTime);

        this.physicalLayer.simulator.totalPacketTime += packet.timeTravel();
        this.physicalLayer.simulator.totalHop += packet.nHop; //todo xem nHop la gi

    }
}
