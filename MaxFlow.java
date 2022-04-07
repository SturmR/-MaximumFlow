import java.util.*;
import java.io.*;

public class MaxFlow {
	
	//Stores non-a bag capacities summed up in one integer, to simplify the graph
	static int b=0;
	static int c=0;
	static int d=0;
	static int e=0;
	static int bd=0;
	static int be=0;
	static int cd=0;
	static int ce=0;

	static int totalA=0;
	
	//a-bag array lists, containing their capacities
	static ArrayList<Integer> a = new ArrayList<Integer>();
	static ArrayList<Integer> ab = new ArrayList<Integer>();
	static ArrayList<Integer> ac = new ArrayList<Integer>();
	static ArrayList<Integer> ad = new ArrayList<Integer>();
	static ArrayList<Integer> ae = new ArrayList<Integer>();
	static ArrayList<Integer> abd = new ArrayList<Integer>();
	static ArrayList<Integer> abe = new ArrayList<Integer>();
	static ArrayList<Integer> acd = new ArrayList<Integer>();
	static ArrayList<Integer> ace = new ArrayList<Integer>();
	
	//transport array lists, containing their capacities
	static ArrayList<Integer> greenTrains = new ArrayList<Integer>();
	static ArrayList<Integer> redTrains = new ArrayList<Integer>();
	static ArrayList<Integer> greenReindeer = new ArrayList<Integer>();
	static ArrayList<Integer> redReindeer = new ArrayList<Integer>();
	
	static int totalGifts=0;
	
	static ArrayList<Edge>[] graph;
	//depth array keeps the levels of the nodes, as in distance from source
	static int[] depth;
	//next array is for pruning dead ends
	static int[] next;
	
	//creates a graph in form of array of array lists of edges
	public static ArrayList<Edge>[] graph(int n) {
		ArrayList<Edge>[] grap = new ArrayList[n];
		for (int i=0; i<n; i++) {
			grap[i] = new ArrayList<Edge>();
		}
		return grap;
	}
	
	//adds an edge, as well as its reverse edge
	public static void addEdge(ArrayList<Edge>[] graph, int s, int t, int cap) {
		Edge e1 = new Edge(s, t, 0, cap);
		Edge e2 = new Edge(t, s, 0, 0);
		graph[s].add(e1);
		graph[t].add(e2);
		e1.rev = e2;
		e2.rev = e1;
	}
	
	//the boolean output states whether we are able to reach the sink or not, also assigns levels to each node which indicates its distance from the source node
	public static boolean bfs() {
		Arrays.fill(depth, -1);
		depth[0] = 0;
		Queue<Integer> q = new ArrayDeque<>(graph.length);
		q.offer(0);
		while(!q.isEmpty()) {
			int node = q.poll();
			for (int i=0; i<graph[node].size(); i++) {
				Edge e = graph[node].get(i);
				if (depth[e.end] < 0 && e.cap - e.flow > 0) {
					depth[e.end] = depth[e.start] + 1;
					q.offer(e.end);
				}
			}
		}
		return depth[graph.length-1] != -1;
	}
	
	//returns the maximum available flow from each augmenting path that it finds
	public static int dfs(int node, int flow) {
		if (node == graph.length-1)
			return flow;
		for (;next[node] < graph[node].size(); next[node]++) {
			Edge e = graph[node].get(next[node]);
			if(depth[e.end] == depth[node] + 1 && e.cap - e.flow > 0) {
				int bottleNeck = dfs(e.end, Math.min(flow , e.cap-e.flow));
				if (bottleNeck > 0) {
					e.flow += bottleNeck;
					e.rev.flow -= bottleNeck;
					return bottleNeck;
				}
			}
		}
		
		return 0;
	}
			
			
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		
		int numGreenTrains = in.nextInt();
		for (int i=0; i<numGreenTrains; i++) {
			int cap = in.nextInt();
			greenTrains.add(cap);
		}

		int numRedTrains = in.nextInt();
		for (int i=0; i<numRedTrains; i++) {
			int cap = in.nextInt();
			redTrains.add(cap);
		}
		
		int numGreenReindeer = in.nextInt();
		for (int i=0; i<numGreenReindeer; i++) {
			int cap = in.nextInt();
			greenReindeer.add(cap);
		}
		
		int numRedReindeer = in.nextInt();
		for (int i=0; i<numRedReindeer; i++) {
			int cap = in.nextInt();
			redReindeer.add(cap);
		}
		
		//BAGS
		int numBags = in.nextInt();
		for (int i=0; i<numBags; i++) {
			String type = in.next();
			int cap = in.nextInt();
			totalGifts += cap;
			if (type.equals("b")) {
				b+=cap;
			} else if (type.equals("c")) {
				c+=cap;
			}else if (type.equals("d")) {
				d+=cap;
			}else if (type.equals("e")) {
				e+=cap;
			}else if (type.equals("bd")) {
				bd+=cap;
			}else if (type.equals("be")) {
				be+=cap;
			}else if (type.equals("cd")) {
				cd+=cap;
			}else if (type.equals("ce")) {
				ce+=cap;
			}else if (type.equals("a")) {
				a.add(cap);
				totalA++;
			}else if (type.equals("ab")) {
				ab.add(cap);
				totalA++;
			}else if (type.equals("ac")) {
				ac.add(cap);
				totalA++;
			}else if (type.equals("ad")) {
				ad.add(cap);
				totalA++;
			}else if (type.equals("ae")) {
				ae.add(cap);
				totalA++;
			}else if (type.equals("abd")) {
				abd.add(cap);
				totalA++;
			}else if (type.equals("abe")) {
				abe.add(cap);
				totalA++;
			}else if (type.equals("acd")) {
				acd.add(cap);
				totalA++;
			}else if (type.equals("ace")) {
				ace.add(cap);
				totalA++;
			}
			
		}
		
		
		int totalNodes = numGreenTrains + numGreenReindeer + numRedReindeer + numRedTrains + totalA + 10;//10 = 1 source, 1 sink, 8 non-a bags merged
		
		graph = graph(totalNodes);
		depth = new int[totalNodes];
		next = new int[totalNodes];
		
		//graph, in order: source, 8 non-a bags, abd - abe - acd - ace - ae - ad - ac - ab - a - green trains - green reindeer - red trains - red reindeer, sink
		
		addEdge(graph, 0, 1, b);
		addEdge(graph, 0, 2, c);
		addEdge(graph, 0, 3, d);
		addEdge(graph, 0, 4, e);
		addEdge(graph, 0, 5, bd);
		addEdge(graph, 0, 6, be);
		addEdge(graph, 0, 7, cd);
		addEdge(graph, 0, 8, ce);
		
		int base1 = 9+abd.size();
		int base2 = base1+abe.size();
		int base3 = base2+acd.size();
		int base4 = base3+ace.size();
		int base5 = base4+ae.size();
		int base6 = base5+ad.size();
		int base7 = base6+ac.size();
		int base8 = base7+ab.size();
		int base9 = base8+a.size();
		int base10 = base9+numGreenTrains;
		int base11 = base10+numRedTrains;
		int base12 = base11+numGreenReindeer;
		int base13 = base12+numRedReindeer;
		
		
		for (int i=0; i<abd.size(); i++) {
			addEdge(graph, 0, 9+i, abd.get(i));
			for (int j=0; j<numGreenTrains; j++) 
				addEdge(graph, 9+i, base9+j, 1);
		}
		
		for (int i=0; i<abe.size(); i++) {
			addEdge(graph, 0, base1+i, abe.get(i));
			for (int j=0; j<numGreenReindeer; j++) 
				addEdge(graph, base1+i, base11+j, 1);
		}
		
		for (int i=0; i<acd.size(); i++) {
			addEdge(graph, 0, base2+i, acd.get(i));
			for (int j=0; j<numRedTrains; j++)
				addEdge(graph, base2+i, base10+j, 1);
		}
		
		for (int i=0; i<ace.size(); i++) {
			addEdge(graph, 0, base3+i, ace.get(i));
			for (int j=0; j<numRedReindeer; j++)
				addEdge(graph, base3+i, base12+j, 1);
		}
		
		for (int i=0; i<ae.size(); i++) {
			addEdge(graph, 0, base4+i, ae.get(i));
			for (int j=0; j<numGreenReindeer+numRedReindeer; j++)
				addEdge(graph, base4+i, base11+j, 1);
		}
		
		for (int i=0; i<ad.size(); i++) {
			addEdge(graph, 0, base5+i, ad.get(i));
			for (int j=0; j<numGreenTrains+numRedTrains; j++)
				addEdge(graph, base5+i, base9+j, 1);
		}
		
		for (int i=0; i<ac.size(); i++) {
			addEdge(graph, 0, base6+i, ac.get(i));
			for (int j=0; j<numRedReindeer; j++)
				addEdge(graph, base6+i, base12+j, 1);
			for (int j=0; j<numRedTrains; j++)
				addEdge(graph, base6+i, base10+j, 1);
		}
		
		for (int i=0; i<ab.size(); i++) {
			addEdge(graph, 0, base7+i, ab.get(i));
			for (int j=0; j<numGreenTrains; j++)
				addEdge(graph, base7+i, base9+j, 1);
			for (int j=0; j<numGreenReindeer; j++)
				addEdge(graph, base7+i, base11+j, 1);
		}
		
		for (int i=0; i<a.size(); i++) {
			addEdge(graph, 0, base8+i, a.get(i));
			for (int j=0; j<numGreenTrains+numGreenReindeer+numRedTrains+numRedReindeer; j++)
			addEdge(graph, base8+i, base9+j, 1);
		}
		
		for (int i=0; i<numGreenTrains; i++) {
			addEdge(graph, 1, base9+i, b);
			addEdge(graph, 3, base9+i, d);
			addEdge(graph, 5, base9+i, bd);
			addEdge(graph, base9+i, base13, greenTrains.get(i));
		}
			
		for (int i=0; i<numGreenReindeer; i++) {
			addEdge(graph, 1, base11+i, b);
			addEdge(graph, 4, base11+i, e);
			addEdge(graph, 6, base11+i, be);
			addEdge(graph, base11+i, base13, greenReindeer.get(i));
		}
		
		for (int i=0; i<numRedTrains; i++) {
			addEdge(graph, 2, base10+i, c);
			addEdge(graph, 3, base10+i, d);
			addEdge(graph, 7, base10+i, cd);
			addEdge(graph, base10+i, base13, redTrains.get(i));
		}
			
		for (int i=0; i<numRedReindeer; i++) {
			addEdge(graph, 2, base12+i, c);
			addEdge(graph, 4, base12+i, e);
			addEdge(graph, 8, base12+i, ce);
			addEdge(graph, base12+i, base13, redReindeer.get(i));
		}
			
		
		int totalFlow = 0;
		while (bfs()) {
			Arrays.fill(next, 0);
			while (true) {
				int extraFlow = dfs(0, Integer.MAX_VALUE);
				if (extraFlow == 0)
					break;
				totalFlow += extraFlow;
			}
		}
		
		out.println(totalGifts-totalFlow);
	}

}
