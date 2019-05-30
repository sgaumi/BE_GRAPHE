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
		double cout_total;
		cout_total=this.getCost();
		cout_total=cout_total+cost_to_dest;
		return cout_total;
	}
	
	public void setCost(double cost, boolean added) {
		double result=cost;
		if (added==true) {
			result = cost-cost_to_dest;
		}
		this.cout=result;
	}
}