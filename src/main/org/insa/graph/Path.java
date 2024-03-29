package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        
        Arc fastestarc=null;
        
        if (nodes.size()!=0) {
        	fastestarc=nodes.get(0).getSuccessors().get(0);
        }
        
        
        boolean test=true;
        
        
        if (nodes.size()!=0 && nodes.size()!=1) {
        	Node node_prec=nodes.get(0);
	        for (Node n : nodes) {
	        	if (n!=node_prec) {
	        		for(Arc a: node_prec.getSuccessors()) {
	        			if(a.getDestination()==n) {
		        			if (test) {fastestarc=a;test=false;}
		        			else {
		        				if(a.getMinimumTravelTime()<fastestarc.getMinimumTravelTime())fastestarc=a;
		        			}
	        			}
	        		}
	        		if (test) {
	        			throw(new IllegalArgumentException());
	        		}
	        		arcs.add(fastestarc);
	        		test=true;
	        	}
	        	node_prec=n;
	        }
        }
    	if (nodes.size()==1) {
    		return new Path(graph, nodes.get(0));
    	}
    	if (nodes.size()==0) {
    		return new Path(graph);
    	}
  
        
        
        return new Path(graph, arcs);
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
       // System.out.println("\n/////////\nsize: "+nodes.size());
        //Initialisation
        Node node_prec = null;
        int i = 0;

        
        if(nodes.size() == 0) {
        	return new Path(graph);
        }
        //Cas d'un chemin d'un nœud
        if(nodes.size() == 1) {
        	return new Path(graph,nodes.get(0));
        }
        
        //Cas d'un chemin de plus d'un nœud
        for(Node n: nodes) {
        	//System.out.print("passage");
        	//Ignorer la première étape
        	if(i == 0) {
        		node_prec = nodes.get(i);
        	}else {
	        	// Préparer la recherche d'un arc
	        	Arc shortestArc = null;
	        	for(Arc a: node_prec.getSuccessors()) {
	        		
	        		//Passer à l'arc suivant s'il n'a pas la bonne destination
	        		if(a.getDestination() != n) {
	        			continue;
	        		}
	        		if(shortestArc == null || a.getLength() < shortestArc.getLength()) {
	            		shortestArc = a;
	        		}
	        		//System.out.println("2eboucle");
	        	}
	        	//Préparer le tour suivant
	        	node_prec = nodes.get(i);
	        	
	        	if(shortestArc != null && shortestArc.getOrigin() != null) {
	            	arcs.add(shortestArc);
	            	System.out.println("Ajout!\n");
	        	}else {
	        		throw(new IllegalArgumentException());
	        	}
        	}
        	i++;
        }
        //System.out.println("\n/////////\nsize: "+arcs.size());
        return new Path(graph, arcs);
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise..
     */
    public boolean isValid() {
    	boolean test=false;
        
        int real_way=0;
        Node node_prec=this.getOrigin();
        Node node_mtn;
        for (Arc a: this.arcs) {
        	node_mtn=a.getOrigin();
        	if (node_mtn!=node_prec) {
        		real_way++;
        	}
        	node_prec=a.getDestination();
        }
        
        if (real_way==0 || this.isEmpty() || this.size()==1) {
        	test=true;
        }
        
        return test;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     *
     */
    public float getLength() {
    	float longueur = 0;
        for(Arc a: this.arcs) {
        	longueur += a.getLength();
        }
        return longueur;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     */
    public double getTravelTime(double speed) {
    	double temps = 0;
        for(Arc a: this.arcs) {
        	temps += a.getTravelTime(speed);
        }
        return temps;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     */
    public double getMinimumTravelTime() {
       	double temps = 0;
        for(Arc a: this.arcs) {
        	temps += a.getMinimumTravelTime();
        }
        return temps;
    }

}
