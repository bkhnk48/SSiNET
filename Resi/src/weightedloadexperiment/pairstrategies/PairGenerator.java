package weightedloadexperiment.pairstrategies;

import java.util.ArrayList;
import java.util.List;

import network.Topology;
import network.entities.Switch;

public abstract class PairGenerator {
	
	 private final int BANDWIDTH = 106670000;
    private Integer[] allHosts;

    public Integer[] getAllHosts() {
        return allHosts;
    }

    public void setAllHosts(Integer[] allHosts)
    {
    	this.allHosts = allHosts;
    }
    public List<Integer> getSources() {
        return sources;
    }

    public List<Integer> getDestinations() {
        return destinations;
    }

    public void setSources(List<Integer> sources) {
        this.sources = sources;
    }

    public void setDestinations(List<Integer> destinations) {
        this.destinations = destinations;
    }

    private List<Integer> sources;
    private List<Integer> destinations;
    
    public PairGenerator()
    {
    	sources = new ArrayList<Integer>();
        destinations = new ArrayList<Integer>();
    }

    public PairGenerator(Integer[] allHosts)
    {
        this.allHosts = allHosts;
        sources = new ArrayList<Integer>();
        destinations = new ArrayList<Integer>();
    }

    public abstract void pairHosts();

    public void checkValid(){}

    public void setUpBandwidth(Topology network)
    {
        int k = allHosts.length;
        k = (int)(Math.cbrt(k*4));
        int minCoreIndex = k*k*k/4 + k*k;
        int maxCoreIndex = minCoreIndex + (k*k/4 - 1);
        List<Switch> switches = network.getSwitches();
        for(Switch sw : switches)
        {
            /*Map<Integer, IntegratedPort> ports = sw.ports;
            for(IntegratedPort p : ports.values())
            {
                Link link = p.getLink();
                if(link.getBandwidth() != BANDWIDTH) {
                    int[] pair = link.getPairs();
                    if ((pair[0] >= minCoreIndex && pair[0] <= maxCoreIndex)
                            ||
                            (pair[1] >= minCoreIndex && pair[1] <= maxCoreIndex)
                    ) {
                        /*System.out.println("Bandwidth of (" + pair[0] + ", " + pair[1] + ") changed from " + link.getBandwidth() +
                                " to " + BANDWIDTH
                        );*/
                        /*link.setBandwidth(BANDWIDTH);
                    }
                }
            }*/
        }
    }
}
