package weightedloadexperiment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.sun.java.swing.plaf.windows.TMSchema.State;

import common.StdOut;
import config.Constant;
import custom.fattree.FatTreeGraph;
import custom.fattree.FatTreeRoutingAlgorithm;
import infrastructure.event.Event;
import network.Topology;
import network.entities.DestinationNode;
import network.entities.Host;
import network.entities.SourceNode;
import simulator.DiscreteEventSimulator;


public class ThroughputExperiment {
	private Topology topology;

	public ThroughputExperiment(Topology network) {
        this.topology = network;
    }

    public double[][] calThroughput(Map<Integer, Integer> trafficPattern, boolean verbose) {
    	
    	long start = System.currentTimeMillis();
        System.out.println("Start:");
        
        DiscreteEventSimulator simulator = new DiscreteEventSimulator(true, Constant.MAX_TIME, verbose);
        topology.clear(); // clear all the data, queue, ... in switches, hosts
        topology.setSimulator(simulator);
        
        simulator.initializeCollectionOfEvents();

        int count = 0;
        for (Integer source : trafficPattern.keySet()) {
            Integer destination = trafficPattern.get(source);
            count++;
            ((SourceNode) topology.getHostById(source)).generatePacket(destination);
        }
        simulator.start();

        double interval = 1e7;
        int nPoint = (int) (simulator.getTimeLimit() / interval + 1);
        double[][] points = new double[2][nPoint];
        for (int i = 0; i < nPoint; i++) {
            // convert to ms
            points[0][i] = i * interval;
            points[1][i] = simulator.receivedPacketPerUnit[i];
        }

        double throughput = 0;
        List<Double> scores = new ArrayList<Double>();
        for (int i = 0; i < nPoint; i++) {
            points[1][i] = 100 * points[1][i] * Constant.PACKET_SIZE /
                    (trafficPattern.size() * Constant.LINK_BANDWIDTH * interval / 1e9);
        }
        for (int i = 0; i < nPoint; i++) {
        	scores.add(points[1][i]);
        }
        throughput = points[1][nPoint - 1];

        StdOut.printf("Throughput : %.2f\n", throughput);

        double rawThroughput = throughput * Constant.LINK_BANDWIDTH / 100 / 1e9;
        //StdOut.printf("RAW Throughput : %.2f GBit/s\n", rawThroughput);

        double alternativeRawThroughput = simulator.numReceived * Constant.PACKET_SIZE / (trafficPattern.size());
        //StdOut.printf("b1: %f\n", alternativeRawThroughput);
        alternativeRawThroughput = alternativeRawThroughput / (nPoint * interval);
        
        
        long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
        
        System.out.println("\nTime for the method addCurrentEventsFromDevices is: "
                            + formatter.format(simulator.timeOfAddCurrentEventsFromDevices/1000d) + " secs");
        
        System.out.println("\nTime for the method selectNextCurrentTime is: "
                + formatter.format(simulator.timeOfSelectNextCurrentTime/1000d) + " secs");

        GraphPanel.createAndShowGui(scores);

        if(false)
        {
	        // Export to filfp
	        try {
	            String fileName = "./src/results/throughput.txt";
	            File file = new File(fileName);
	            // creates the file
	            file.createNewFile();
	
	            FileWriter writer = new FileWriter(file);
	
	            // Writes the content to the file
	            writer.flush();
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }

        return points;
    }


    public static void main(String[] args) {

        //for(int timeOfRun = 0; timeOfRun < 100-3; timeOfRun++)
        {
            FatTreeGraph G = new FatTreeGraph(4);
            FatTreeRoutingAlgorithm ra = new FatTreeRoutingAlgorithm(G, false);

            Topology topology = new Topology(G, ra);
            

            ThroughputExperiment experiment = new ThroughputExperiment(topology);

            Map<Integer, Integer> traffic = new HashMap<>();

            List<Integer> sourceNodeIDs = topology.getSourceNodeIDs();
            List<Integer> destinationNodeIDs = topology.getDestinationNodeIDs();

            int sizeOfFlow = //1;
                    sourceNodeIDs.size();

            for (int i = 0; i < sizeOfFlow; i++) {
                traffic.put(sourceNodeIDs.get(i), destinationNodeIDs.get(i));
                
            }
            
            

            experiment.calThroughput(traffic, false);

            //ThanhNT
            int rxPacket = 0;
            double thp = 0, privateThp = 0;
            for (int i = 0; i < topology.getHosts().size(); i++) {
                Host host = topology.getHosts().get(i);
                if(host instanceof DestinationNode) {
                    DestinationNode destinationNode = (DestinationNode)host;
                    if (destinationNode.getReceivedPacketInNode() != 0) {
                        /*System.out.println("DesNode " + destinationNode.getId() + " receives: "
                                + destinationNode.getReceivedPacketInNode() + " packets "
                                + "from " + destinationNode.getFirstTx() + " to " + (destinationNode.getLastRx())
                        );*/
                        rxPacket += destinationNode.getReceivedPacketInNode();
                        privateThp = destinationNode.getReceivedPacketInNode()
                                * Constant.PACKET_SIZE / (destinationNode.getLastRx() - destinationNode.getFirstTx());
                        thp += privateThp;
                        //System.out.println("\t Private Throughput = " + privateThp);
                    }
                }
            }
            
            
            System.out.println("\n# of calling packet state constructors (might be 1.000.000): "
            		+ infrastructure.state.State.countPacket);
            System.out.println("\n# of calling ENB state constructors: "
            		+ infrastructure.state.State.countStateENB);
            
            System.out.println("\n# of calling EXB state constructors (might be 800.000): "
            		+ infrastructure.state.State.countStateEXB);

            System.out.println("\n# of initialization"
            		+ " of sub events (might be 1.000.000): "
            		+ Event.countSubEvent);

            //System.out.println("Rx Packet: " + rxPacket + " " + (thp / traffic.size()));
            
            
            //Endof ThanhNT
        }
    }

}
