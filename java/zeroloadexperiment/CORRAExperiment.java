package zeroloadexperiment;

import common.StdOut;
import custom.corra.CORRAGraph;
import custom.corra.CORRARoutingAlgorithm;

public class CORRAExperiment {
    public static void main(String[] args) {
        long totalTime = System.currentTimeMillis();

        int delta = 3;
        int k = 1;
        int nTest = 1;

        int size = 1024;
        int xSize = 32;

        String type = "rsn";

        StdOut.printf("Experiment zeroloadexperiment with %d nodes, delta = %d\n", size, delta);

        for (int i = 0; i < nTest; i++) {
            StdOut.printf("Test %d\n", i + 1);
            String prefix = "./data/" + type + "/" + size + "/" + (i + 1) +
                    "/random_" + size + "_nodes_" + xSize + "_xSize_r2";
            String fileEdge = prefix + ".edges";
            String fileGeo = prefix + ".geo";
            StdOut.printf("Loading network...");
            CORRAGraph graph = new CORRAGraph(size, xSize, fileEdge, fileGeo, delta, k);
            StdOut.printf("Done!\n");

            StdOut.printf("Build routing algorithm...");
            // Choose type of routing algorithm
            CORRARoutingAlgorithm ra = new CORRARoutingAlgorithm(graph);
//            BigCORRARoutingAlgorithm ra = new BigCORRARoutingAlgorithm(graph);

            StdOut.printf("Done!\n");
            StdOut.printf("Calculating...\n");

            // Full pair analysis
            TopologyExperiment topo = new TopologyExperiment(graph, ra);

//            Part analysis
//            int nPair = 100;
//            TopologyExperiment zeroloadexperiment = new TopologyExperiment(graph, ra, nPair);

            StdOut.printf("Done!\n");

            StdOut.printf("Diameter: %d\n", topo.diameter());
            StdOut.printf("Average routing path length: %.3f\n", topo.averagePathLength());
            StdOut.printf("Average latency: %.5f\n", topo.getAvgLatency());
            StdOut.printf("Average RTS: %.5f\n", TopologyExperiment.getAvgRTS(ra));
            StdOut.printf("Path by Br1: %d\n", ra.nBr1);
            StdOut.printf("Path by Br2: %d\n", ra.nBr2);
            StdOut.printf("Path by STP: %d\n", ra.nSTP);
        }

        totalTime = System.currentTimeMillis() - totalTime;
        StdOut.printf("\nRuntime = %.3f\n", 1.0 * totalTime / 1000);
    }
}
