package weightedloadexperiment.pairstrategies.interpod;

import java.util.List;

import common.RandomGenerator;
import custom.fattree.FatTreeGraph;
import custom.fattree.FatTreeRoutingAlgorithm;
import weightedloadexperiment.pairstrategies.InterPodIncoming;

public class MinimalCoreSwitches extends InterPodIncoming {

	private int [][] coreInPath;
	private int[] allCores;
	
	private int [] oversubscriptedCores;
	
	public MinimalCoreSwitches(FatTreeRoutingAlgorithm routing, FatTreeGraph G) {
		super(routing, G);
		
		
	}
	
	
	@Override
	public void setAllHosts(Integer[] allHosts)
	{
		super.setAllHosts(allHosts);
    	this.k =  (int)Math.cbrt(4*allHosts.length);
        
        int numOfHosts = allHosts.length;
        
		coreInPath = new int[numOfHosts][numOfHosts];
		for(int i = 0; i < numOfHosts; i++)
		{
			for(int j = 0; j < numOfHosts; j++)
			{
				int source = getHostIndex(i);
				int dest = getHostIndex(j);
				int core = getRealCoreSwitch(source, dest);
				coreInPath[i][j] = core;
			}
		}
		
		allCores = new int[k*k/4];
		int minCore = k*k*k/4 + k*k; 
		for(int i = 0; i < k*k/4; i++)
		{
			allCores[i] = i + minCore;
		}
	}
	
	@Override
    public void pairHosts() {
		int delta = RandomGenerator.nextInt(0, k*k/4);
		int numOfOversubscriptedCores = k*k/8;
		oversubscriptedCores = new int[numOfOversubscriptedCores];
		delta = delta % numOfOversubscriptedCores;
		
		for(int i = delta; i < numOfOversubscriptedCores; i++)
		{
			oversubscriptedCores[i - delta] = allCores[i % (k*k/4)];
		}
		
		delta = delta + RandomGenerator.nextInt(0, k*k/4);
		delta = delta % numOfOversubscriptedCores;
		List<Integer> dests = getDestinations();
		int previousDst = 0;
		
		for(int pod = 0; pod < k; pod++)
		{
			int indexOfFirstCore = delta;
			for(int offset = 0; offset < k*k/4; offset++)
			{
				int i = pod * k*k/4 + offset;
				int dst = getHostIndex(i);
				if(isCoreAvailable(dst, indexOfFirstCore))
				{
					dests.add(dst);
				}
			}
		}
	}
	
	//Nhan dau vao la chi so cua Host trong danh sach cac host (tu 0..15)
	//tra ve ket qua la ID cua host trong danh sach nodes: 0..3, 8..11, 16..19, 24..27 
	private int getHostIndex(int i)
	{
		int result = 0;
		int pod = i / (k*k/4);
		int delta = i % (k*k/4);
		result = pod * (k*k/4 + k) + delta;
		return result;
	}
	
	private boolean isCoreAvailable(int dst, int firstIndex)
	{
		boolean found = false;
		List<Integer> sources = getSources();
		List<Integer> dests = getDestinations();
		if(dests.contains(dst))
		{
			return false;
		}
		
		while(firstIndex < oversubscriptedCores.length && !found)
		{
			for(int i = 0; i < k*k*k/4; i++)
			{
				int src = getHostIndex(i);
				if(!sources.contains(src) && src != dst && (src / k != dst / k))
				{
					if(getRealCoreSwitch(src, dst) == oversubscriptedCores[firstIndex])
					{
						sources.add(src);
						found = true;
						return found;
					}
				}
			}
			if(!found)
			{
				firstIndex++;
			}
		}
		return found;
	}

}
