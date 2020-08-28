package custom.fattree;

//import javafx.util.Pair;
//import kotlin.Triple;
//import kotlin.TuplesKt;
import javatuples.* ;
import network.elements.Packet;
import network.entities.Host;
import network.entities.Switch;
import routing.RoutingAlgorithm;
import routing.RoutingPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infrastructure.entity.Node;

/**
 * Created by Dandoh on 5/24/17.
 */
public class FatTreeRoutingAlgorithm implements RoutingAlgorithm, Cloneable {
    public FatTreeGraph G;
    public Map<Pair<Integer, Integer>, RoutingPath> precomputedPaths = new HashMap<>();
    public Map<Integer, Map<Integer, Integer>> suffixTables = new HashMap<>();
    
    public Map<Integer, Map<Integer, Integer>> getSuffixTables() {
		return suffixTables;
	}

	public void setSuffixTables(Map<Integer, Map<Integer, Integer>> suffixTables) {
		this.suffixTables = suffixTables;
	}

	private Map<Integer,
            Map<Triplet<Integer, Integer, Integer>, Integer>> prefixTables = new HashMap<>();
	
    public void setCorePrefixTables(Map<Integer, Map<Pair<Integer, Integer>, Integer>> corePrefixTables) {
		this.corePrefixTables = corePrefixTables;
	}

	public Map<Integer, Map<Triplet<Integer, Integer, Integer>, Integer>> getPrefixTables() {
		return prefixTables;
	}

	public void setPrefixTables(Map<Integer, Map<Triplet<Integer, Integer, Integer>, Integer>> prefixTables) {
		this.prefixTables = prefixTables;
	}

	private Map<Integer,
        Map<Pair<Integer, Integer>, Integer>> corePrefixTables = new HashMap<>();

    

	public FatTreeRoutingAlgorithm(FatTreeGraph G, boolean precomputed) {
        this.G = G;
        buildTables();
        if (precomputed) {
            List<Integer> hosts = G.hosts();
            for (int i = 0; i < hosts.size() - 1; i++) {
                for (int j = i + 1; j < hosts.size(); j++) {
                    int source = hosts.get(i);
                    int destination = hosts.get(j);
                    path(source, destination);
                }
            }
        }
    }

    private void buildTables() {
        // TODO - build prefix - suffix routing table
        int k = G.getK();
        int numEachPod = k * k / 4 + k;

        // edge switches
        for (int p = 0; p < k; p++) {
            int offset = numEachPod * p;
            for (int e = 0; e < k / 2; e++) {
                int edgeSwitch = offset + k * k / 4 + e;
                // create suffix table
                HashMap<Integer, Integer> suffixTable = new HashMap<>();
                for (int suffix = 2; suffix <= k / 2 + 1; suffix++) {
                    int agg = offset + k * k / 4 + (e + suffix - 2) % (k / 2) + (k / 2);
                    suffixTable.put(suffix, agg);
                }
                suffixTables.put(edgeSwitch, suffixTable);
            }
        }

        // agg switches
        for (int p = 0; p < k; p++) {
            int offset = numEachPod * p;
            for (int a = 0; a < k / 2; a++) {
                int aggSwitch = offset + k * k / 4 + k / 2 + a;

                // create suffix table
                Map<Integer, Integer> suffixTable = new HashMap<>();
                for (int suffix = 2; suffix <= k / 2 + 1; suffix++) {
                    int core = a * k / 2 + (suffix + a - 2) % (k / 2) + numEachPod * k;
                    suffixTable.put(suffix, core);
                }
                // inject to the behavior
                suffixTables.put(aggSwitch, suffixTable);

                // create prefix table
                Map<javatuples.Triplet<Integer, Integer, Integer>, Integer> prefixTable
                        = new HashMap<>();
                /*javatuples.Triplet<Integer, String, String> triplet 
                            = new javatuples.Triplet<Integer, String, String>(Integer.valueOf(1),  
                                                "GeeksforGeeks", "A computer portal");*/ 
                  
                                  for (int e = 0; e < k / 2; e++) {
                    int edgeSwitch = offset + k * k / 4 + e;
                    prefixTable.put(new javatuples.Triplet<>(10, p, e), edgeSwitch);
                }
                prefixTables.put(aggSwitch, prefixTable);

            }
        }


        // core switches
        for (int c = 0; c < k * k / 4; c++) {
            int core = k * k * k / 4 + k * k + c;

            // build core prefix
            HashMap<Pair<Integer, Integer>, Integer> corePrefixTable =
                    new HashMap<>();
            for (int p = 0; p < k; p++) {
                int offset = numEachPod * p;
                int agg = (c / (k / 2)) + k / 2 + k * k / 4 + offset;
                corePrefixTable.put(new Pair<>(10, p), agg);
            }
            corePrefixTables.put(core, corePrefixTable);
        }

        System.out.println();
    }

    /**
     * Time complexity: O(1)
     */
    @Override
    public int next(int source, int current, int destination) {
        if (G.isHostVertex(current)) {
            return G.adj(current).get(0);
        } else if (G.adj(current).contains(destination)) {
            return destination;
        } else {
            int type = G.switchType(current);
            if (type == FatTreeGraph.CORE) {
                Address address = G.getAddress(destination);
                Pair<Integer, Integer> prefix
                        = new Pair<>(address._1, address._2);
                Map<Pair<Integer, Integer>, Integer> corePrefixTable =
                        corePrefixTables.get(current);

                return corePrefixTable.get(prefix);
            } else if (type == FatTreeGraph.AGG) {
                Address address = G.getAddress(destination);

                Triplet<Integer, Integer, Integer> prefix
                        = new Triplet<>(address._1, address._2, address._3);
                int suffix = address._4;

                Map<Triplet<Integer, Integer, Integer>, Integer> prefixTable =
                        prefixTables.get(current);
                Map<Integer, Integer> suffixTable = suffixTables.get(current);

                if (prefixTable.containsKey(prefix)) {
                    return prefixTable.get(prefix);
                } else {
                    return suffixTable.get(suffix);
                }
            } else { // Edge switch
                Address address = G.getAddress(destination);
                int suffix = address._4;

                Map<Integer, Integer> suffixTable = suffixTables.get(current);
                return suffixTable.get(suffix);
            }

        }
    }

    @Override
    public RoutingPath path(int source, int destination) {
    	return null;
    }
    
    public int next(Packet packet, Node node) 
    {
    	return next(packet.getSource(), node.getId(), packet.getDestination());
    }
    
    public RoutingAlgorithm build(Node node) throws CloneNotSupportedException
    {
    	RoutingAlgorithm ra = (RoutingAlgorithm) this.clone();
    	if(node instanceof Host)
    	{
    		((FatTreeRoutingAlgorithm)ra).setCorePrefixTables(null);
    		((FatTreeRoutingAlgorithm)ra).setPrefixTables(null);
    		((FatTreeRoutingAlgorithm)ra).setSuffixTables(null);
    	}
    	if(node instanceof Switch)
    	{
    		int id = ((Switch)node).getId();
    		int type = G.switchType(id);
    		if(type == FatTreeGraph.AGG)
    		{
    			((FatTreeRoutingAlgorithm)ra).corePrefixTables = null;
    		}
    		if(type == FatTreeGraph.EDGE)
    		{
    			((FatTreeRoutingAlgorithm)ra).prefixTables = null;
    			((FatTreeRoutingAlgorithm)ra).corePrefixTables = null;
    		}
    		if(type == FatTreeGraph.CORE)
    		{
    			((FatTreeRoutingAlgorithm)ra).prefixTables = null;
    			((FatTreeRoutingAlgorithm)ra).suffixTables = null;
    		}
    	}
		return ra;
    }
    
    public void update(Packet p, Node node)
    {
    	
    }

}
