class Flow {
	private int[][] graph;
	private int source;
	private int sink;

	private int[][] flowGraph;
	private int[][] residualGraph;
	private int maxFlow;

	private boolean printOn = false;

	public Flow(int[][] graph, int source, int sink, boolean printOn) {
		this.printOn = printOn;

		if (printOn)
			System.out.println("Flow from " + source + " to " + sink + " in graph: ");
		if (printOn)
			printGraph(graph);

		this.graph = graph;
		this.source = source;
		this.sink = sink;

		if (!inputCheck()) {
			if (printOn)
				System.out.println("\nIllegal Argument to Flow()");
			throw new IllegalArgumentException();
		}

		if (printOn)
			System.out.println("\nFlow calculation using Ford Fulkerson: ");
		computeMaxFlowFordFulkerson();
	}

	// accepts a weighted graph where positive numbers indicate an edge
	// and 0 indicates there is no edge
	public Flow(int[][] graph, int source, int sink) {
		this(graph, source, sink, false);
	}

	public int[][] getFlowGraph() {
		return flowGraph;
	}

	public int getMaxFlow() {
		return maxFlow;
	}

	public void computeMaxFlowFordFulkerson() {
		// create a flow graph (no flow to start)
		flowGraph = createFlowGraph();
		if (printOn)
			System.out.println("\nFlow Graph:");
		if (printOn)
			printGraph(flowGraph);

		// create a residual graph (starts as a copy of the graph)
		residualGraph = createResidual();
		if (printOn)
			System.out.println("\nResidual Graph:");
		if (printOn)
			printGraph(residualGraph);

		// get a path through the residual graph
		Node pathStart = getPath();

		// while we have a valid path
		while (pathStart != null) {
			if (printOn)
				System.out.print("\nPath: ");
			Node curr = pathStart;
			while (curr.next != null) {
				if (printOn)
					System.out.print(curr.value.startNodeId + "->");
				curr = curr.next;
			}
			if (printOn)
				System.out.print(curr.value.startNodeId + "->");
			if (printOn)
				System.out.println(curr.value.endNodeId);

			// get the minimum cost of the path
			int minCost = getMinCost(pathStart);
			if (printOn)
				System.out.println("Min Cost: " + minCost);

			// update the flow
			updateFlow(pathStart, minCost);
			if (printOn)
				System.out.println("\nFlow Graph:");
			if (printOn)
				printGraph(flowGraph);

			// update the flow
			updateResidual(pathStart, minCost);
			if (printOn)
				System.out.println("\nResidual Graph:");
			if (printOn)
				printGraph(residualGraph);

			// get a new path through the residual graph
			pathStart = getPath();
		}

		// get max flow from flow graph
		maxFlow = computeMaxFlow();
		if (printOn)
			System.out.println("\nMax Flow: " + maxFlow);
	}

	// error checking input
	public boolean inputCheck() {
		if (graph == null) {
			return false;
		}

		if (source < 0 || source >= graph.length) {
			return false;
		}

		if (sink < 0 || sink >= graph.length || sink == source) {
			return false;
		}

		for (int i = 0; i < graph.length; i++) {
			if (graph[i].length != graph.length) {
				return false;
			}

			for (int j = 0; j < graph[i].length; j++) {
				if (graph[i][j] < 0) {
					return false;
				}

				if (graph[i][j] > 0 && graph[j][i] > 0) {
					return false;
				}
			}
		}

		return true;
	}

	// no flow to start
	public int[][] createFlowGraph() {
		return new int[graph.length][graph.length];
	}

	// residual graph starts as a copy of the original graph
	public int[][] createResidual() {
		int[][] residualGraph = new int[graph.length][graph.length];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				residualGraph[i][j] = graph[i][j];
			}
		}
		return residualGraph;
	}

	// BFS from source to sink
	public Node getPath() {
		// queue of nodes encountered
		int[] queue = new int[residualGraph.length];

		// parents list / in-queue list (serves both purposes, if they are
		// the source, or if they have a parent, they must be in the queue)
		int[] parents = new int[residualGraph.length];
		for (int i = 0; i < parents.length; i++) {
			parents[i] = -1;
		}

		// put the source node in the queue
		int qSize = 1;
		queue[0] = source;

		// do a breadth first traversal
		for (int i = 0; i < qSize; i++) {
			int queueNode = queue[i];
			for (int j = 0; j < residualGraph.length; j++) {
				if (j != source && parents[j] == -1 && residualGraph[queueNode][j] > 0) {
					queue[qSize] = j;
					parents[j] = queueNode;
					qSize++;
				}
			}
		}

		if (printOn)
			System.out.print("\nQueue:   ");
		for (int i = 0; i < queue.length; i++) {
			if (printOn)
				System.out.print(queue[i] + " ");
		}
		if (printOn)
			System.out.println();

		if (printOn)
			System.out.print("Parents: ");
		for (int i = 0; i < parents.length; i++) {
			if (printOn)
				System.out.print(parents[i] + " ");
		}
		if (printOn)
			System.out.println();

		// no path found :(
		if (parents[sink] == -1) {
			return null;
		}

		// path found! create list of edges starting at the destination
		// and following the parent pointers
		int currNode = sink;
		int currParent = parents[sink];
		Node path = new Node(new Edge(currParent, currNode));

		while (currParent != source) {
			// go up to the parent
			currNode = currParent;

			// get the new parent
			currParent = parents[currNode];

			// prepend to the path
			path = new Node(new Edge(currParent, currNode), path);
		}

		return path;
	}

	public int getMinCost(Node path) {
		int min = Integer.MAX_VALUE;

		while (path != null) {
			int cost = residualGraph[path.value.startNodeId][path.value.endNodeId];
			min = Math.min(min, cost);
			path = path.next;
		}

		return min;
	}

	public void updateFlow(Node path, int minCost) {
		// go to each edge in the path
		while (path != null) {
			int startId = path.value.startNodeId;
			int endId = path.value.endNodeId;

			// if it's a "real" edge, we're increasing the flow
			if (graph[startId][endId] != 0) {
				flowGraph[startId][endId] += minCost;
			}
			// if it's a backwards flowing edge, we're decreasing the flow
			// in the original direction
			else {
				flowGraph[endId][startId] -= minCost;
			}

			// next edge on the path
			path = path.next;
		}
	}

	public void updateResidual(Node path, int minCost) {
		// go to each edge in the path
		while (path != null) {
			int startId = path.value.startNodeId;
			int endId = path.value.endNodeId;

			// decrease along the path chosen
			residualGraph[startId][endId] -= minCost;

			// increase in the opposite direction
			residualGraph[endId][startId] += minCost;

			// next edge on the path
			path = path.next;
		}
	}

	public int computeMaxFlow() {
		int flowTotal = 0;
		for (int i = 0; i < flowGraph.length; i++) {
			flowTotal += flowGraph[i][sink];
		}
		return flowTotal;
	}

	public static void printGraph(int[][] graph) {
		for (int row = 0; row < graph.length; row++) {
			System.out.print("{");
			for (int col = 0; col < graph[row].length; col++) {
				if (graph[row][col] == Integer.MAX_VALUE) {
					System.out.printf(" inf ", graph[row][col]);
				} else {
					System.out.printf("%4d ", graph[row][col]);
				}
			}
			System.out.println("}");
		}
	}

	public static void main(String[] args) {
		int[][] graphAdjMatrix = null;
		Flow f;

		graphAdjMatrix = new int[][] { { 0, 1 }, { 0, 0 } };
		f = new Flow(graphAdjMatrix, 0, 1, true);
		System.out.println();
		System.out.println();

		/*
		 * graphAdjMatrix = new int[][] {{0, 1, 1}, {0, 0, 1}, {0, 0, 0}};
		 * f = new Flow(graphAdjMatrix, 0, 1, true);
		 * System.out.println();
		 * System.out.println();
		 * 
		 * graphAdjMatrix = new int[][] {{0, 1, 1}, {0, 0, 1}, {0, 0, 0}};
		 * f = new Flow(graphAdjMatrix, 0, 2, true);
		 * System.out.println();
		 * System.out.println();
		 * 
		 * graphAdjMatrix = new int[][] {{0, 1, 1}, {0, 0, 1}, {0, 0, 0}};
		 * f = new Flow(graphAdjMatrix, 1, 2, true);
		 * System.out.println();
		 * System.out.println();
		 * 
		 * graphAdjMatrix = new int[][] {{0, 1, 2, 0}, {0, 0, 0, 2}, {0, 1, 0, 1}, {0,
		 * 0, 0, 0}};
		 * f = new Flow(graphAdjMatrix, 0, 3, true);
		 * System.out.println();
		 * System.out.println();
		 * 
		 * graphAdjMatrix = new int[][] {{0, 1, 2, 0}, {0, 0, 0, 2}, {0, 1, 0, 1}, {0,
		 * 0, 0, 0}};
		 * f = new Flow(graphAdjMatrix, 2, 3, true);
		 * System.out.println();
		 * System.out.println();
		 * 
		 * graphAdjMatrix = new int[][] {{0, 100, 100, 0}, {0, 0, 0, 101}, {0, 1, 0,
		 * 99}, {0, 0, 0, 0}};
		 * f = new Flow(graphAdjMatrix, 0, 3, true);
		 * System.out.println();
		 * System.out.println();
		 */
	}
}