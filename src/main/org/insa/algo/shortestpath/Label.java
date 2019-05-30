package org.insa.algo.shortestpath;


import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label implements Comparable<Label> {
	
	@SuppressWarnings("unused")
	private Node sommet_courant;
	
	private boolean marque = false;
	
	protected double cout = Double.POSITIVE_INFINITY;
	
	private Arc pere;
	
	public Label(Node node) {
		this.sommet_courant = node;
	}
	
	public boolean isMarked() {
		return this.marque;
	}
	
	public void setMarked() {
		this.marque = true;
	}
	
	public void setFather(Arc a) {
		this.pere = a;
	}
	
	public void setCost(double cost) {
		this.cout = cost;
	}

	public double getCost() {
		return this.cout;
	}
	
	//à redéfinir dans LabelStar pour A*
	public double getTotalCost() {
		return this.cout;
	}

	public Arc getFather() {
		// TODO Auto-generated method stub
		return this.pere;
	}
	
    public int compareTo(Label other) {
        return Double.compare(this.getTotalCost(), other.getTotalCost());
    }

	public Node getNode() {
		// TODO Auto-generated method stub
		return this.sommet_courant;
	}
	
	public String toString() {
		String str = "Noeud"+this.getNode().getId()+" : "+this.cout+" hasFather= "+(this.getFather() != null)+" ";
		if(this.getFather() != null) str += "(Nœud"+this.getFather().getOrigin().getId()+")";
		return str;
	}
}
