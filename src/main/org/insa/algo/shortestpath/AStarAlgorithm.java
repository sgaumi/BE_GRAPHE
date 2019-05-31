
package org.insa.algo.shortestpath;

import org.insa.graph.Node;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected Label[] createLabelTab() {
    	LabelStar[] Lbl=new LabelStar[graph.getNodes().size()];
    	Lbl[data.getOrigin().getId()]=new LabelStar(data.getOrigin(),dest);
    	return Lbl;
    }
    
    protected Label createLabel(Node node) {
    	return new LabelStar(node,dest);
    }

}


