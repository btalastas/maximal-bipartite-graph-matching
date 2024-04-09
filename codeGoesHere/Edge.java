class Edge {
	public int startNodeId;
	public int endNodeId;
	
	public Edge(int startNodeId, int endNodeId) {
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
	}
	
	public String toString() {
		return startNodeId + "-->" + endNodeId;
	}
}