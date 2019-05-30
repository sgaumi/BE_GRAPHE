

package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    ShortestPathData data = getInputData();
    ShortestPathSolution solution = null;
    Node dest = data.getDestination();
   
    //Informations about the graph
    Graph graph = data.getGraph();

    
    protected Label[] createLabelTab() {
    	Label[] Lbl=new Label[graph.getNodes().size()];
    	Lbl[data.getOrigin().getId()]=new Label(data.getOrigin());
    	return Lbl;
    }
    
    protected Label createLabel(Node node) {
    	return new Label(node);
    }
    
    @Override
    protected ShortestPathSolution doRun() {
        
        
        
        //Create a list of labels associated to each nodes
        //Labels will be added when it would be used and the it's null
        //Label[] nodeLabels = new Label[graph.getNodes().size()];				//ICI
    	Label[] nodeLabels = createLabelTab();
        
     // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[graph.getNodes().size()];
        
        //Create an heap
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        
        //Create of the first label
        //Label first = new Label(data.getOrigin());																			//ICI
        Label first=nodeLabels[data.getOrigin().getId()];
        first.setCost(0);
        //nodeLabels[data.getOrigin().getId()] = first;
        heap.insert(first);


        if(data.getOrigin() == null) {
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph));
        }
        else if(data.getOrigin().equals(data.getDestination())){
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, data.getOrigin()));
        }
        else {
        	boolean reached = false;
	        while(!heap.isEmpty() && !reached) {
	        	if (nodeLabels[data.getDestination().getId()] != null && nodeLabels[data.getDestination().getId()].isMarked()) {
	        		reached = true;
	        	}
	        	//Sort the heap
	        	heap.update();
	        	//Choose the cheaper node
	        	Label currentLabel = heap.deleteMin();
	        	//Mark as processed node
	        	currentLabel.setMarked();
	        	notifyNodeMarked(currentLabel.getNode());
	        	

	        	//Browse each arc of the node
	        	for(Arc a: currentLabel.getNode().getSuccessors()) {
	        		//Check if the node exist
	        		int nextNodeId = a.getDestination().getId();
	        		
	        		//Calculate the cost
	        		double cost = currentLabel.getCost() + a.getMinimumTravelTime();
	        		
	        		//Check existence of an object with the destination node
	        		boolean exist = false;
	
	        		exist = (predecessorArcs[nextNodeId] != null);
	        		
	        		if(!exist && nextNodeId != data.getOrigin().getId()) { //Insert the label because it's inexistent
	        			Node node = graph.getNodes().get(a.getDestination().getId());
	        			//Label l = new Label(node);																			//ICI
	        			Label l = createLabel(node);
	        			l.setCost(cost);
	        			l.setFather(a);
	        			nodeLabels[nextNodeId] = l;
	        			notifyNodeReached(node);
	        			heap.insert(l);
	        			predecessorArcs[nextNodeId] = a;
	        		}else { //Update the label
	        			if(nodeLabels[nextNodeId].getCost() > cost) {
	        				nodeLabels[nextNodeId].setCost(cost);
	        				nodeLabels[nextNodeId].setFather(a);
	        		        predecessorArcs[nextNodeId] = a;
	        			}
	        		}
	        	}
	        }
	        Label destination = nodeLabels[data.getDestination().getId()];
	        if(destination != null) System.out.println("dst marked="+destination.isMarked());
        
	        //Give the solution
	        if (destination == null || !destination.isMarked()) {
	            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
	        }
	        else {
	        	System.out.print("Djk algo dest father="+destination.getFather()+" <== ");
	        	System.out.println(destination.getFather().getDestination() == destination.getNode());
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
        }
                
        return solution;
    }

}
