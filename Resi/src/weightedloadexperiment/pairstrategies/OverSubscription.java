package weightedloadexperiment.pairstrategies;

import java.util.List;

import network.Topology;
import network.entities.Link;
import network.entities.Switch;

public abstract class OverSubscription extends PairGenerator {

	private final int OVERSUBSCRIPTION_BANDWIDTH = 106670000;
	private final int NORMAL_BANDWIDTH = 96*1000*1000;
	public int modulo ;
	
	public OverSubscription() {
		
	}

	public OverSubscription(Integer[] allHosts) {
		super(allHosts);
		
	}
	
	@Override
	public void setUpBandwidth(Topology network)
    {
		Integer[] allHosts = getAllHosts();
        
        List<Switch> switches = network.getSwitches();
        
        int k = (int)Math.cbrt(4*allHosts.length);
        int maxIndexOfCore = allHosts.length + 5*k*k/4 - 1;
    	int minIndexOfCore = maxIndexOfCore - k + 1;
    	
        for(Switch sw : switches)
        {
        	for(Link link : sw.physicalLayer.links.values())
        	{
        		if(isOversubscriptedLink(link, maxIndexOfCore, minIndexOfCore))
        		{
        			link.setBandwidth(OVERSUBSCRIPTION_BANDWIDTH);
        		}
        		else {
        			link.setBandwidth(NORMAL_BANDWIDTH);
        		}
        	}
            
        }
    }
	
	@Override
    public void setAllHosts(Integer[] allHosts)
    {
    	super.setAllHosts(allHosts);
    	this.modulo = allHosts.length;
    }

}
