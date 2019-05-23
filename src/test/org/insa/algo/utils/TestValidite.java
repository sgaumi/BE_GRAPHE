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


	@Before
	public void initAll() throws Exception {
		System.out.println("Chargement des cartes");
		
		//Initialisation des graphes
		String prefix = "/home/corentin/Téléchargements/";
		String[] mapList = {"carre.mapgr","carre-dense.mapgr","toulouse.mapgr","guadeloupe.mapgr"};
	//	String[] mapList = {"carre.mapgr"};
        for (String str: mapList) {
        	String mapName = prefix + str;
            // Create a graph reader.
            GraphReader reader = new BinaryGraphReader(
                    new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

            // TODO: Read the graph.
            graphList.add(reader.read());
        }
	}
	
	public boolean testPath(Graph graph,int origin,int destination,int arcInspectorNumber) {
		ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(arcInspectorNumber);
        ShortestPathData data = new ShortestPathData(graph, graph.getNodes().get(origin), graph.getNodes().get(destination), arcInspector);
        ShortestPathAlgorithm spa = new DijkstraAlgorithm(data);
        System.out.println("rightOrigin"+ (origin == graph.getNodes().get(origin).getId()));
        ShortestPathSolution sps = spa.run();
        System.out.println("pathSize"+sps.getPath().size());
        return sps.getPath().isValid() && sps.isFeasible();
	}

	@Test
	public void testValidite() {
		System.out.println("Carte 1: Carré");
		//Carte 1
		Graph graph = graphList.get(0);
		//origin = destination
		assertEquals(testPath(graph,5,5,0),true);
		
		////destination accessible
		assertEquals(testPath(graph,5,17,0),true);
		
		/*
	/////////////Carte 2
		System.out.println("Carte 2: Carré dense");
		
		graph = graphList.get(1);
		//origin = destination
		assertEquals(testPath(graph,63,63,0),true);
		
		//destination accessible
		assertEquals(testPath(graph,157890,312456,0),true);
		
		
		
		/////////////Carte 3 Toulouse
		System.out.println("Carte 3: Toulouse");
		
		graph = graphList.get(2);
		//origin = destination
		assertEquals(testPath(graph,1608,1608,0),true);
		
		//destination accessible
		assertEquals(testPath(graph,8890,1608,0),true);
		

		/////////////Carte 4 Guadeloupe
		System.out.println("Carte 4: La Désirade (Gaudeloupe)");
		
		graph = graphList.get(2);
		//origin = destination
		assertEquals(testPath(graph,13716,13716,0),true);
		
		//destination accessible
		assertEquals(testPath(graph,13716,33229,0),true);
		
		//destination inaccessible
		assertEquals(testPath(graph,13716,15808,0),false);
*/
	}
}
