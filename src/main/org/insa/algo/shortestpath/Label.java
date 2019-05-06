package org.insa.algo.shortestpath;


import org.insa.graph.Arc;

public class Label {
	
	@SuppressWarnings("unused")
	private int sommet_courant;
	
	private boolean marque = false;
	
	private double cout = Double.POSITIVE_INFINITY;
	
	private Arc pere;
	
	public Label(int n) {
		this.sommet_courant = n;
	}
	
	public void setMarked() {
		this.marque = true;
	}
	
	public boolean isMarked() {
		return this.marque;
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

	public Arc getFather() {
		// TODO Auto-generated method stub
		return this.pere;
	}
	
    public int compareTo(Label other) {
        return Double.compare(this.cout, other.cout);
    }
}
