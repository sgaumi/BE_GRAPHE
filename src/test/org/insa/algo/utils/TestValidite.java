package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.components.BasicDrawing;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


public class TestValidite {
	//Ce dont on aura besoin pour le test
	
	//Chargement des cartes
	private ArrayList<Graph> graphList = new ArrayList<Graph>();
	String[] mapList = {"carre.mapgr","carre-dense.mapgr","toulouse.mapgr","guadeloupe.mapgr"};
	int[] Nodelist = {5,5,5,5,5,17,63,63,63,63,157890,312456,1608,1608,1608,1608,8890,1608,13716,13716,13716,33229,13716,15808};
	
	
	@Before
	public void initAll() throws Exception {
		System.out.println("Chargement des cartes");
		
		//Initialisation des graphes
		String prefix = "/home/corentin/Téléchargements/";

        for (String str: mapList) {
        	String mapName = prefix + str;
            // Create a graph reader.
            GraphReader reader = new BinaryGraphReader(
                    new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

            // TODO: Read the graph.
            graphList.add(reader.read());
        }
	}
	
	public boolean testPath(Graph graph,int origin,int destination,int arcInspectorNumber,String algorithm) {
		//Création d'un ShortestPathData
		ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(arcInspectorNumber);
        ShortestPathData data = new ShortestPathData(graph, graph.getNodes().get(origin), graph.getNodes().get(destination), arcInspector);
        //Définition de l'algorithme utilisé
        ShortestPathAlgorithm spa;
        switch (algorithm) {
		        case "dijkstra":
		        	spa = new DijkstraAlgorithm(data);
		        	break;
		        case "bellman":
		        	spa = new BellmanFordAlgorithm(data);
		        	break;
		        case "a*":
		        	spa = new AStarAlgorithm(data);
		        	break;
		        default:
		        	System.out.println("Unknown type of algorithm");
		        	return false;
        }      
        //Exploitation de la solution
        ShortestPathSolution sps = spa.run();
        return (sps.isFeasible())? sps.getPath().isValid() : false;
	}
	
	public float testLength(Graph graph,int origin,int destination,int arcInspectorNumber,String algorithm) {
		ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(arcInspectorNumber);
        ShortestPathData data = new ShortestPathData(graph, graph.getNodes().get(origin), graph.getNodes().get(destination), arcInspector);

        ShortestPathAlgorithm spa;
        switch (algorithm) {
		        case "dijkstra":
		        	spa = new DijkstraAlgorithm(data);
		        	break;
		        case "bellman":
		        	spa = new BellmanFordAlgorithm(data);
		        	break;
		        case "a*":
		        	spa = new AStarAlgorithm(data);
		        	break;
		        default:
		        	System.out.println("Unknown type of algorithm");
		        	return -1;
        }
        System.out.println(algorithm);
        ShortestPathSolution sps = spa.run();

        return (sps.isFeasible()) ? sps.getPath().getLength(): 0;
	}
	
	@Test
	public void testValidite() {
		int step = 0;
		int mapId = 0;
		
		for(Graph graph: graphList) {
			System.out.println("Carte n°"+mapId+": "+mapList[mapId]);
			for (int i = 0; i<3; i++) {
				int src = Nodelist[(step*2)];
				int dst = Nodelist[(step*2)+1];
				assertEquals(testPath(graph,src,dst,0,"dijkstra"),true);
				assertEquals(testPath(graph,src,dst,0,"a*"),true);
				step++;
			}
			mapId++;
		}

	}
	
	//@Test
	public void testOptimalite() {
		int step = 0;
		int mapId = 0;
		
		for(Graph graph: graphList) {
			System.out.println("Carte n°"+mapId+": "+mapList[mapId]);
			for (int i = 0; i<3; i++) {
				int src = Nodelist[(step*2)];
				int dst = Nodelist[(step*2)+1];
				float bellmanLength = testLength(graph,src,dst,0,"bellman");
				assertEquals(Double.compare(testLength(graph,src,dst,0,"dijkstra"),bellmanLength),0);
				assertEquals(Double.compare(testLength(graph,src,dst,0,"a*"),bellmanLength),0);
				step++;
			}
			mapId++;
		}
	}
}
