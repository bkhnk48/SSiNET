package network;

import common.Knuth;
import config.Constant;
import custom.fattree.FatTreeGraph;
import custom.fattree.FatTreeRoutingAlgorithm;
import graph.Coordination;
import graph.Graph;
import network.entities.Host;
import network.elements.EntranceBuffer;
import network.elements.ExitBuffer;
import network.entities.*;

import network.layers.NetworkLayer;
import network.layers.PhysicalLayer;
import simulator.DiscreteEventSimulator;

import java.util.*;

/**
 * Created by Dandoh on 6/27/17.
 */
public class Topology {
    private Graph graph;
    private List<Host> hosts;
    private List<Switch> switches;
    private Map<Integer, Host> hostById;
    private Map<Integer, Switch> switchById;
    private List<Integer> sourceNodes;
    private List<Integer> destinationNodes;

    //ThanhNT 14/10 new property
    public Map<Integer, String> cordOfNodes;
    //Endof ThanhNT 14/10 new property

    public Topology(FatTreeGraph graph, FatTreeRoutingAlgorithm routingAlgorithm) {
        this.graph = graph;
        // construct hosts, switches and links and routing algorithm
        hosts = new ArrayList<>();
        switches = new ArrayList<>();
        hostById = new HashMap<>();
        switchById = new HashMap<>();
        sourceNodes = new ArrayList<>();
        destinationNodes = new ArrayList<>();

        //ThanhNT 14/10 add new statements to init property
        cordOfNodes = new HashMap<>();
        //Endof ThanhNT 14/10 add new statements to init property

        NetworkLayer networkLayer = new NetworkLayer(routingAlgorithm);
        
     // link from switch to switch
        Coordination C = new Coordination(graph);
        for (Switch sw : switches) {
            for (int nextNodeID : graph.adj(sw.getId())) {
                if (graph.isSwitchVertex(nextNodeID)) {
                    Switch otherSwitch = switchById.get(nextNodeID);
                    // => ThanhNT set comment to THE following line
                    if (!otherSwitch.physicalLayer.links.containsKey(sw.getId()))
                    {
                        // create new link
                        double distance =  C.distanceBetween(sw.getId(), otherSwitch.getId());
                        //System.out.println("Chieu dai switch = " + distance + " from: " + sw.getId() + " to: " + otherSwitch.getId());
                        //double x = 5;

                        EntranceBuffer entranceBuffer;
                        ExitBuffer exitBuffer;
                        Link link = new Link(sw, otherSwitch, distance);
                        sw.physicalLayer.links.put(otherSwitch.getId(), link);
                        otherSwitch.physicalLayer.links.put(sw.getId(), link);

                        //exb and enb of switch
                        entranceBuffer = new EntranceBuffer(sw, otherSwitch, Constant.QUEUE_SIZE);
                        exitBuffer = new ExitBuffer(sw, otherSwitch, Constant.QUEUE_SIZE);
                        entranceBuffer.physicalLayer = sw.physicalLayer;
                        exitBuffer.physicalLayer = sw.physicalLayer;
                        sw.physicalLayer.entranceBuffers.put(otherSwitch.getId(), entranceBuffer);
                        sw.physicalLayer.exitBuffers.put(otherSwitch.getId(), exitBuffer);

                        //exb and enb of Otherswitch
                        entranceBuffer = new EntranceBuffer(otherSwitch, sw, Constant.QUEUE_SIZE);
                        exitBuffer = new ExitBuffer(otherSwitch, sw, Constant.QUEUE_SIZE);
                        entranceBuffer.physicalLayer = otherSwitch.physicalLayer;
                        exitBuffer.physicalLayer = otherSwitch.physicalLayer;
                        otherSwitch.physicalLayer.entranceBuffers.put(sw.getId(), entranceBuffer);
                        otherSwitch.physicalLayer.exitBuffers.put(sw.getId(), exitBuffer);

                        //ThanhNT 14/10 add new statements to insert coord of switch
                        cordOfNodes.put(sw.getId(), C.getCoordOfSwitch(sw.getId()));
                        cordOfNodes.put(otherSwitch.getId(), C.getCoordOfSwitch(otherSwitch.getId()));
                        //Endof ThanhNT 14/10 add new statements to insert coord of switch
                    }
                }
            }
        }

        // khoi tao host va dua vao list
        Integer[] hostIDList = graph.hosts().toArray(new Integer[0]);


        //Knuth.shuffle(hostIDList);

//        hostIDList = new Integer[]{ 17,24,18,11,2,3,19,8,26,0,27,1,10,16,9,25 };

        sourceNodes.addAll(Arrays.asList(hostIDList)//.subList(0, hostIDList.length / 2)
        											);
        //sourceNodes.add(0);
        
        for (int sourceNodeID : sourceNodes) {
            SourceNode sourceNode = new SourceNode(sourceNodeID);
            sourceNode.physicalLayer = new PhysicalLayer(sourceNode);
            sourceNode.networkLayer = networkLayer;
            hosts.add(sourceNode);
            hostById.put(sourceNodeID, sourceNode);

            //ThanhNT 14/10 add new statements to add new ID of HOST
            cordOfNodes.put(sourceNodeID, "");
            //Endof ThanhNT 14/10 add new statements to add new ID of HOST

        }

        destinationNodes.addAll(Arrays.asList(hostIDList)//.subList(hostIDList.length / 2, hostIDList.length)
        						);
        //destinationNodes.add(1);
        
        for (int destinationNodeID : destinationNodes) {
            DestinationNode destinationNode = new DestinationNode(destinationNodeID);
            hosts.add(destinationNode);
            hostById.put(destinationNodeID, destinationNode);

            //ThanhNT 14/10 add new statements to add new ID of HOST
            cordOfNodes.put(destinationNodeID, "");
            //Endof ThanhNT 14/10 add new statements to add new ID of HOST

            destinationNode.physicalLayer = new PhysicalLayer(destinationNode);
            destinationNode.networkLayer = networkLayer;
        }

        // khoi tao switch va them vao list
        for (int sid : graph.switches()) {
            Switch sw = new Switch(sid);
            switches.add(sw);
            switchById.put(sid, sw);

            //ThanhNT 14/10 add new statements to add new ID of switch
            cordOfNodes.put(sid, "");
            //Endof ThanhNT 14/10 add new statements to add new ID of switch

            sw.physicalLayer = new PhysicalLayer(sw, graph.getK());
            sw.networkLayer = networkLayer;
        }


        

        // todo them phan add link tu switch den host kieu nhu o tren
        // link from switch to host
        for (Host host : hosts) {
            // get switch
            int switchID = graph.adj(host.getId()).get(0);
            Switch sw = switchById.get(switchID);

            // create new link
            Link link = new Link(host, sw, Constant.HOST_TO_SWITCH_LENGTH);
            host.physicalLayer.links.put(host.getId(), link);// rieng link host luu id la id cua host
            sw.physicalLayer.links.put(host.getId(), link);

            //initiate property in Physical Layer
            if(host instanceof SourceNode){
                //exb of host
                ExitBuffer exitBuffer = new ExitBuffer(host, sw, Constant.QUEUE_SIZE);
                exitBuffer.physicalLayer = host.physicalLayer;
                host.physicalLayer.exitBuffers.put(sw.getId(), exitBuffer);

                //enb of switch to host
                EntranceBuffer entranceBuffer = new EntranceBuffer(sw, host, Constant.QUEUE_SIZE);
                entranceBuffer.physicalLayer = sw.physicalLayer;
                sw.physicalLayer.entranceBuffers.put(host.getId(), entranceBuffer);
            }

            // tao exitBuffer cho switch noi toi desNode
            //exb of switch to desNode
            if(host instanceof DestinationNode){
                ExitBuffer exitBuffer = new ExitBuffer(sw, host, Constant.QUEUE_SIZE);
                exitBuffer.physicalLayer = sw.physicalLayer;
                sw.physicalLayer.exitBuffers.put(host.getId(), exitBuffer);
            }

            //ThanhNT 14/10 add new statements to insert coord of HOST
            cordOfNodes.put(host.getId(), C.getCoordOfHost(sw.getId(), Constant.HOST_TO_SWITCH_LENGTH));
            //Endof ThanhNT 14/10 add new statements to insert coord of HOST
        }
    }

    public List<Integer> getSourceNodeIDs(){
        return sourceNodes;
    }
    public List<Integer> getDestinationNodeIDs(){
        return destinationNodes;
    }

    public Graph getGraph() {
        return graph;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public List<Switch> getSwitches() {
        return switches;
    }

    public Host getHostById(int id) {
        return hostById.get(id);
    }

    public void clear() {
        for (Host host : hosts) {
            host.clear();
        }

        for (Switch sw: switches) {
            sw.clear();
        }
    }
    
    public void setSimulator(DiscreteEventSimulator sim)
    {
    	for (Host host : hosts) {
            host.physicalLayer.simulator = sim;
        }

        for (Switch sw: switches) {
            sw.physicalLayer.simulator = sim;
        }
        sim.topology = this;
    }

    public boolean checkDeadlock(){
    	return false;
    }
}
