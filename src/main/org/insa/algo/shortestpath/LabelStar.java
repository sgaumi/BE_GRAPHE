package org.insa.algo.shortestpath;

import org.insa.graph.Arc;
import org.insa.graph.Node;
import org.insa.graph.Point;

public class LabelStar extends Label implements Comparable<Label>{
	
	private double cost_to_dest;
	
	public LabelStar(Node node, Node destination) {
		super(node);
		this.cost_to_dest=Point.distance(node.getPoint(), destination.getPoint());
	}
	
	public double getTotalCost() {
		return (this.cout)+(this.cost_to_dest);
	}
	
	
}