package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        
        final int nbNodes = graph.size();
        
        //Initialization
        Label[] nodeLabels = new Label[nbNodes];
        for(int i = 0; i < nbNodes; i++) {
        	nodeLabels[i] = new Label(i);
        }
        
        // Initialize the first node
        Node currentNode = data.getOrigin();
        nodeLabels[currentNode.getId()].setCost(0);
        nodeLabels[currentNode.getId()].setFather(null);
        nodeLabels[currentNode.getId()].setMarked();
        notifyNodeMarked(data.getOrigin());
        
        
        Node nextNode = null;
        // Continue loop while the destination isn't reached
        while(!nodeLabels[data.getDestination().getId()].isMarked()) {	
        	for(Arc a: currentNode.getSuccessors()) {
        		//Calculate the cost
        		int id = a.getDestination().getId();
        		double cost = nodeLabels[currentNode.getId()].getCost() + a.getMinimumTravelTime();
        		System.out.println(cost);
        		
        		//Test if it's going to be better to choose this path
        		if( nodeLabels[id].getCost() > cost) {
        			// Notify that the node is reached if it's the first time it's processed
        			if(Double.isInfinite(nodeLabels[id].getCost())) {
        				notifyNodeReached(a.getDestination());
        			}
        			//Set the code and the father
        			nodeLabels[id].setCost(cost);
        			nodeLabels[id].setFather(a);
        			
        			System.out.println(id+": cout"+nodeLabels[id].getCost());
        		}
        	}
        	
        	//Choose the node with the lower cost
        	double minLabelCost = Double.POSITIVE_INFINITY;
        	int indexNextNode = -1;
        	for(int i = 0; i < nbNodes;i++) {
        		if(nodeLabels[i].getCost() < minLabelCost && !nodeLabels[i].isMarked() ) {
        			minLabelCost = nodeLabels[i].getCost();
        			indexNextNode = i;
        		}
        	}
        	if (indexNextNode != -1) {
            	nextNode = data.getGraph().getNodes().get(indexNextNode);
            	nodeLabels[indexNextNode].setMarked();
            	notifyNodeMarked(nextNode);
            	
            	currentNode = nextNode;
          	}else {
          		System.out.println("Problème de gestion du noeud suivant");
        		return new ShortestPathSolution(data, Status.INFEASIBLE);
        	}
        	
        }
        
        //Give the solution
        if (nodeLabels[data.getDestination().getId()].getFather() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
        	 // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            
         // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = nodeLabels[data.getDestination().getId()].getFather();
            while (arc != null) {
                arcs.add(arc);
                arc = nodeLabels[arc.getOrigin().getId()].getFather();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }

        return solution;
    }

}
