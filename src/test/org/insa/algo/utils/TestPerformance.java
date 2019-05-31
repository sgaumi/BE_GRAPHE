package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
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


public class TestPerformance {
	//Ce dont on aura besoin pour le test
	
	//Chargement des cartes
	//private ArrayList<Graph> graphList = new ArrayList<Graph>();
//	String[] mapList = {"carre.mapgr","carre-dense.mapgr","toulouse.mapgr","guadeloupe.mapgr"};
	ArrayList<Integer> NodeList = new ArrayList<Integer>();
	
	
	@Before
	public void initAll() throws Exception {
		System.out.println("Lancement");

	}
	
	@Test
	public void testStart() {
		System.out.println("Toulouse");
		launch("guadeloupe.mapgr",200,"astar","distance");
	}
	public boolean launch(String str,int nb_test,String algorithm,String type) {

		String prefix = "/home/corentin/Téléchargements/";
		String mapName = prefix + str;
		
		Graph graph = null;
		GraphReader reader;
		
		try {
			reader = new BinaryGraphReader(
			        new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
			graph = reader.read();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Choose the random points
		Random r = new Random(15);
		for (int i =0; i< nb_test*2;i++) {
			NodeList.add(r.nextInt(graph.getNodes().size()));
		}
		
		//Initialize the file
		Date d = new Date();
		PrintWriter writer = null;
		String filename = "/home/corentin/"+str.split(".mapg")[0]+"_"+type+"_"+nb_test+"_data_"+algorithm;
		try {
			writer = new PrintWriter(filename+".csv", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(str.split(".mapg")[0]); //Name of the map
		writer.println((type == "distance")?0:1); //Print if it's based on time or distance
		writer.println("origine destination cout temps explores marques tas");
		int arcInspectorNumber = (type == "distance")? 0:2;
		ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(arcInspectorNumber);
       
		//Make a test and write the result
		for(int i=0;i < nb_test; i++) {
			int origin = NodeList.get(2*i);
			int destination = NodeList.get(2*i + 1);
			

			//Code
			ShortestPathData data = new ShortestPathData(graph, graph.getNodes().get(origin), graph.getNodes().get(destination), arcInspector);
			DijkstraAlgorithm spa;
	        switch (algorithm) {
			        case "dijkstra":
			        	spa = new DijkstraAlgorithm(data);
			        	break;
			        case "astar":
			        	spa = new AStarAlgorithm(data);
			        	break;
			        default:
			        	System.out.println("Unknown type of algorithm");
			        	return false;
	        }      
	        ShortestPathSolution sps = spa.run();
	        	
	        writer.print(origin+" "+destination+" ");
			
			//Cost
			if(sps.isFeasible()) {
				writer.print((type == "distance")?sps.getPath().getLength():sps.getPath().getMinimumTravelTime());
			}else {
				writer.print("");
			}
			
			writer.print(" "+sps.getSolvingTime().getNano());
			writer.print(" "+spa.getReachedNodes());
			writer.print(" "+spa.getMarkedNodes());
			writer.print(" "+spa.getMaxHeapSize());
			///writer.print();
			writer.print("\n");			
		}
		
		//Save the file
		writer.close();
		return true;
		
	}

}
