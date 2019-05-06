package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.PriorityQueue;
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
        
        BinaryHeap heap = new BinaryHeap();
        Label destination = null;
        System.out.println("Initialisation...");
        for(int i = 0; i < nbNodes; i++) {
        	Label l = new Label(data.getGraph().getNodes().get(i));
        	nodeLabels[i] = l;
        	// Initialize the first node
        	if( i == data.getOrigin().getId() ) {
        		l.setCost(0);
        	}
        	if(i == data.getDestination().getId()){
        		destination = l;
        	}
        	
        	heap.insert((Comparable) l);
        }

        
        Node currentNode = null;
        Node nextNode = null;
        
        Label next;
        // Continue loop while the destination isn't reached
        while(!destination.isMarked()) {
        	
        	//Choose the node with the lower cost
        	next = (Label) heap.deleteMin();
        	next.setMarked();
        	//Define it as process node
        	currentNode = next.getNode();
        	notifyNodeMarked(currentNode);

        	if(next == destination) {
				System.out.println("dst: cout = " + next.getCost() +" hasFather="+(destination.getFather() != null));
			}
        	
        	for(Arc a: currentNode.getSuccessors()) {
        		//Calculate the cost
        		
        		System.out.println(next.getCost());
        		int id = a.getDestination().getId();
        		double cost = nodeLabels[currentNode.getId()].getCost() + a.getMinimumTravelTime();
        		
        		System.out.println(cost +" > "+nodeLabels[id].getCost());
        		//Test if it's going to be better to choose this path
        		if( nodeLabels[id].getCost() > cost) {
        			// Notify that the node is reached if it's the first time it's processed
        			if(Double.isInfinite(nodeLabels[id].getCost())) {
        				notifyNodeReached(a.getDestination());
        			}
        			//Set the code and the father
        			nodeLabels[id].setCost(cost);
        			//heap.arraySet(id,nodeLabels[id]);
        			nodeLabels[id].setFather(a);       			
        		}
        	}
        	
        }
        
        //Give the solution
        if (destination.getFather() == null) {
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

      //  solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        return solution;
    }

}
