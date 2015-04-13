package baggageroutingsystem;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Vertex implements Comparable<Vertex> {
	public final String name;
	public List<Edge> adjacencies = new ArrayList<Edge>();
	public double minDistance = Double.POSITIVE_INFINITY;
	public Vertex previous;

	public Vertex(String argName) {
		name = argName;
	}

	public String toString() {
		return name;
	}

	public int compareTo(Vertex other) {
		return Double.compare(minDistance, other.minDistance);
	}
}

class Edge {
	public final Vertex target;
	public final double weight;

	public Edge(Vertex argTarget, double argWeight) {
		target = argTarget;
		weight = argWeight;
	}
}

public class BaggageSystem {

	public static void main(String[] args) {
		Map<String, String> departureMap = getDepartureMap();
		calculateShortestPath(departureMap);

	}

	public static void computePaths(Vertex source) {
		source.minDistance = 0.;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();
			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				Vertex v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	public static List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);

		Collections.reverse(path);
		return path;
	}

	
	private static void calculateShortestPath(Map<String, String> departureMap) {
		File bagsToBeRoutedSystem = new File(
				"C:\\Users\\Toshiba\\Documents\\Routing System\\Bags.txt");
		try {
			FileInputStream inputStream = new FileInputStream(
					bagsToBeRoutedSystem);
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			String line = dataInputStream.readLine();
			while (line != null) {
				if (null != line && !line.isEmpty()
						&& line.split(" ").length == 3) {
					Map<String, Vertex> vertexMap = getInputConveyorSystem();
					String bagsID = line.split(" ")[0];
					String startingNode = line.split(" ")[1];
					String destinationNode = departureMap
							.get(line.split(" ")[2]);
					if ("ARRIVAL".equals(line.split(" ")[2])) {
						destinationNode = "BaggageClaim";
					}
					computePaths(vertexMap.get(startingNode));
					List<Vertex> path = getShortestPathTo(vertexMap
							.get(destinationNode));
					System.out.println(bagsID + " " + path + " : "
							+ vertexMap.get(destinationNode).minDistance);
					vertexMap.clear();
				}
				line = dataInputStream.readLine();
			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Map<String, String> getDepartureMap() {
		File departuresSystem = new File(
				"C:\\Users\\Toshiba\\Documents\\Routing System\\Departures.txt");
		Map<String, String> departureMap = new HashMap<String, String>();
		try {
			FileInputStream inputStream = new FileInputStream(departuresSystem);
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			String line = dataInputStream.readLine();
			while (line != null) {
				if (null != line && !line.isEmpty()
						&& line.split(" ").length == 4) {
					String flightName = line.split(" ")[0];
					String nodeName = line.split(" ")[1];
					departureMap.put(flightName, nodeName);
				}
				line = dataInputStream.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return departureMap;
	}

	private static Map<String, Vertex> getInputConveyorSystem() {
		File conveyorSystem = new File(
				"C:\\Users\\Toshiba\\Documents\\Routing System\\Conveyor System.txt");
		Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
		try {
			FileInputStream inputStream = new FileInputStream(conveyorSystem);
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			String line = dataInputStream.readLine();
			while (line != null) {
				Vertex vertex;
				Vertex edgeVertex;
				if (null != line && !line.isEmpty()
						&& line.split(" ").length == 3) {
					String vertexName = line.split(" ")[0];
					String edgeName = line.split(" ")[1];
					String edgeWeight = line.split(" ")[2];
					if (vertexMap.containsKey(vertexName)) {
						vertex = vertexMap.get(vertexName);
					} else {
						vertex = new Vertex(vertexName);
						vertexMap.put(vertexName, vertex);
					}
					if (vertexMap.containsKey(edgeName)) {
						edgeVertex = vertexMap.get(edgeName);
					} else {
						edgeVertex = new Vertex(edgeName);
						vertexMap.put(edgeName, edgeVertex);
					}
					Edge edge1 = new Edge(edgeVertex, new Double(edgeWeight));
					Edge edge2 = new Edge(vertex, new Double(edgeWeight));
					vertex.adjacencies.add(edge1);
					edgeVertex.adjacencies.add(edge2);
				}
				line = dataInputStream.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vertexMap;
	}
}
