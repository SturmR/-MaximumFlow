
public class Edge {
	
	public int start;
	public int end;
	public int flow;
	public int cap;
	public Edge rev;
	
	public Edge(int start, int end, int flow, int cap) {
		this.start = start;
		this.end = end;
		this.flow = flow;
		this.cap = cap;
	}
	
}
