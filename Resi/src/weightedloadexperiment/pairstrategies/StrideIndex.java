package weightedloadexperiment.pairstrategies;

import java.util.List;

public class StrideIndex extends PairGenerator {
    private int stride;
    private int modulo ;
    
    public StrideIndex(int stride)
    {
        super();
        this.stride = stride;
        
    }
    
    public StrideIndex(Integer[] allHosts, int stride)
    {
        super(allHosts);
        this.stride = stride;
        modulo = allHosts.length;
    }

    public void pairHosts()
    {
        List<Integer> sources = getSources();
        List<Integer> destinations = getDestinations();

        Integer[] hosts = getAllHosts();
        for(int i = 0; i < hosts.length; i++)
        {
            int x = hosts[i];
            sources.add(x);
            destinations.add(hosts[(i + stride) % modulo]);
        }

        setSources(sources);
        setDestinations(destinations);
    }
    
    @Override
    public void setAllHosts(Integer[] allHosts)
    {
    	super.setAllHosts(allHosts);
    	this.modulo = allHosts.length;
    }
}
