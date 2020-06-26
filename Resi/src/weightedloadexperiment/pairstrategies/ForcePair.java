package weightedloadexperiment.pairstrategies;

import java.util.List;

import custom.fattree.FatTreeGraph;
import custom.fattree.FatTreeRoutingAlgorithm;

public class ForcePair extends InterPodIncoming {

	private int modulo = 0;
	public ForcePair(FatTreeRoutingAlgorithm routing, FatTreeGraph G) {
		super(routing, G);
		this.modulo = 0;
	}
	
	public ForcePair(FatTreeRoutingAlgorithm routing, FatTreeGraph G, int modulo) {
		super(routing, G);
		this.modulo = modulo;
	}
	
	public void pairHosts()
	{
		List<Integer> sources = getSources();
        List<Integer> destinations = getDestinations();
        
        sources.add((1 + modulo)% 4);     destinations.add(11);
        sources.add((2 + modulo)% 4);     destinations.add(16);
        sources.add((3 + modulo)% 4);     destinations.add(25);
        sources.add((0 + modulo)% 4);     destinations.add(18);
        
        sources.add((9 + modulo)% 4);     destinations.add(19);
        sources.add(10);     destinations.add(24);
        sources.add(11);     destinations.add(26);
        sources.add(18);     destinations.add(27);
        
        sources.add(8);     destinations.add(0);
        sources.add(17);     destinations.add(1);
        sources.add(26);     destinations.add(2);
        sources.add(19);     destinations.add(3);
        
        sources.add(16);     destinations.add(8);
        sources.add(25);     destinations.add(9);
        sources.add(27);     destinations.add(10);
        sources.add(24);     destinations.add(17);
        
		this.setSources(sources);
        this.setDestinations(destinations);
	}

}
