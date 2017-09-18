/* implement a core
functionality of the Link State routing algorithm
*/
/*lingchi chen
  jackchen4work@gmail.com
*/

import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class Dijkstra {

	public static void main(String[] args) {
		//get input data from system input
		Scanner in = new Scanner(System.in);
		System.out.println("Input number of node and data table");
		int n = Integer.parseInt(in.nextLine());
		int[][] array_table = new int[n][n];  //new array table
		ArrayList<Vertex> vertices = new ArrayList();


		for (int i=0; i<n;i++){
			Vertex vertex = new Vertex(i);
			vertices.add(vertex);
			String row = in.nextLine();
			String[] temp_list = row.split(" "); //put element into new list
			for (int c=0;c<n;c++){
				array_table[i][c] = Integer.parseInt(temp_list[c]);
			}
		}
		in.close();

		for(int i=0; i<n;i++){
			Vertex vertex= vertices.get(i);
			for (int c=0;c<n;c++){
				if (array_table[i][c]!=-1 && array_table[i][c]!=0){
					Edge e = new Edge(vertices.get(c),array_table[i][c]);
					vertex.adjacent.add(e);
				}
			}
		}

		String out = "";
		for (Vertex start: vertices){
			System.out.println("Start node: " + (start.node+1));
			System.out.println("-------------------------------------------------------------------");
			System.out.println("Node       |        NextHop        |            Cost    |");
			out = doDijkstras(vertices, start);
			System.out.println(out);
		}
	}
	//set max distance between each node no more than 1000000
	public static final int MAXDISTANCE = 1000000;
	static public class Vertex implements Comparable<Vertex>{
		public int node;
		public ArrayList<Edge> adjacent = new ArrayList();
		public int min_distance = MAXDISTANCE;
		public Vertex front;

		public Vertex(int n){
			node = n;
		}

		public int compareTo(Vertex vertex){
			return Double.compare(min_distance,vertex.min_distance);
		}
	}

	//edge
	static public class Edge{
		public Vertex end_node;
		public int weight;
		//initialize the vertex and edge
		public Edge(Vertex vertex, int w){
			end_node = vertex;
			weight = w;
		}
	}
	//function to calculate cost from one node to neighbor node
	public static void PathCal(Vertex start){
		start.min_distance = 0;
		PriorityQueue<Vertex> vert = new PriorityQueue<Vertex>();
		vert.add(start);
		Vertex vertex = vert.poll();
		while(vertex!=null){
			for (Edge e:vertex.adjacent){
				Vertex next = e.end_node;
				int dist = vertex.min_distance + e.weight;
				if (dist < next.min_distance){
					vert.remove(next);
					next.min_distance = dist;
					next.front = vertex;
					vert.add(next);
				}
			}
			vertex = vert.poll();
		}
	}

	public static ArrayList<Vertex> displayPathTo(Vertex lastNode){
		ArrayList<Vertex> backPath = new ArrayList();
		ArrayList<Vertex> fwdPath = new ArrayList();

		while (lastNode!=null){
			backPath.add(lastNode);
			lastNode = lastNode.front;
		}
		int num = backPath.size()-1;
		for (int i=0; i<=num; i++){
			fwdPath.add(i, backPath.get(num-i));
		}
		return fwdPath;
	}

	public static void reset(ArrayList<Vertex> v){
		for(Vertex node:v){
			node.min_distance = MAXDISTANCE;
			node.front = null;
		}
	}

	public static String doDijkstras (ArrayList<Vertex> vertices, Vertex start){
		ArrayList<Vertex> vert = (ArrayList<Vertex>) vertices.clone();
		String output = "";

		reset(vertices);
		PathCal(start);

		for (Vertex end: vert){
			if (start.node == end.node){
				output = output + (start.node+1) + "          |            " + "self " + "      |            " + 0 + "       |\n";

			}
			else{
				ArrayList<Vertex> path = new ArrayList();
				path = displayPathTo(end);
				if (path.size()>1){
					output = output + (end.node+1) + "          |            " + (path.get(1).node+1) + "          |            " + end.min_distance + "       |\n";

				}
				else{
					output = output + (end.node+1) + "          |            "
							+ "          |            " + "-1(absence of directed edge)" + "          |            " + "NA" + "       |\n";
					System.out.println("\n-------------------------------------------------------------------");
				}
			}
		}
		return output;
	}

}
