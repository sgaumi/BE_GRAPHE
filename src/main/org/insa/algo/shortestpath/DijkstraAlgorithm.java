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
       
        //Informations about the graph
        Graph graph = data.getGraph();
        
        //Create a list of labels associated to each nodes
        //Labels will be added when it would be used and the it's null
        Label[] nodeLabels = new Label[graph.getNodes().size()];
        
     // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[graph.getNodes().size()];
        
        //Create an heap
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        
        //Create of the first label
        Label first = new Label(data.getOrigin());
        first.setCost(0);
        nodeLabels[data.getOrigin().getId()] = first;
        heap.insert(first);
        
        //Define the destination
        Label destination = new Label(data.getDestination());
        nodeLabels[data.getDestination().getId()] = destination;
        heap.insert(destination);

        while(!destination.isMarked()) {
        	//System.out.println(destination);
        	//Sort the heap
        	heap.update();
        	//Choose the cheaper node
        	Label currentLabel = heap.deleteMin();
        	//Mark as processed node
        	currentLabel.setMarked();
        	notifyNodeMarked(currentLabel.getNode());

        	//System.out.println("using node"+currentLabel.getNode().getId());
        	//Browse each arc of the node
        	for(Arc a: currentLabel.getNode().getSuccessors()) {
        		//Check if the node exist
        		int nextNodeId = a.getDestination().getId();
        		
        		//Calculate the cost
        		double cost = currentLabel.getCost() + a.getMinimumTravelTime();
        		
        		//Check existence of an object with the destination node
        		boolean exist = false;

        		exist = (predecessorArcs[nextNodeId] != null);
        		
        		if(!exist && nextNodeId != data.getDestination().getId() && nextNodeId != data.getOrigin().getId()) { //Insert the label because it's inexistent
        			Node node = graph.getNodes().get(a.getDestination().getId());
        			Label l = new Label(node);
        			l.setCost(cost);
        			l.setFather(a);
        			nodeLabels[nextNodeId] = l;
        			heap.insert(l);
        			predecessorArcs[nextNodeId] = a;
        		}else { //Update the label
        			//System.out.println( nodeLabels[nextNodeId] == destination );
        			if(nodeLabels[nextNodeId].getCost() > cost) {
        				nodeLabels[nextNodeId].setCost(cost);
        				nodeLabels[nextNodeId].setFather(a);
        		        predecessorArcs[nextNodeId] = a;
        			}
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
            Arc arc = destination.getFather();

            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
                
        return solution;
    }

}
