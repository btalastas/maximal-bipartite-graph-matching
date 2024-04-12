import java.util.*;
import java.io.*;

class Convert {
	// DO NOT EDIT THIS CLASS IF YOU WANT CREDIT!
	public static class Answer {
		int maxFlow = -1;
		TreeMap<String, String> matches = new TreeMap<>();
	}

	// DO NOT EDIT THE METHOD _SIGNATURE_ IF YOU WANT CREDIT!
	// That includes "throws" (don't add "throws" to the method signature)
	// Yes, you may write helper methods.
	public static Answer youCodeGoesInThisMethod(File inputFile) {
		// YOUR_CODE_HERE
		Answer ans = new Answer();
		int[][] graph = createAdjMatrix(inputFile);
		Flow f = new Flow(graph, 0, graph[0].length - 1);
		int[][] flowGraph = f.getFlowGraph();

		int maxFlow = f.getMaxFlow();
		ans.maxFlow = maxFlow;
		// Finding person who got job
		for (int i = 1; i <= nameMap.size(); i++) {
			if (flowGraph[0][i] == 1) {
				String name = nameMap.get(i);
				String jobName = null;

				for (int j = 1; j < flowGraph[0].length; j++) {
					if (flowGraph[i][j] == 1) {
						jobName = jobIndexMap.get(j);
						break;
					}
				}

				ans.matches.put(name, jobName);
			}
		}

		return ans;
	}

	/**
	 * Mapping job name to index value and vice versa
	 */
	private static Map<String, Integer> jobTitleMap = new HashMap<>();
	private static Map<Integer, String> jobIndexMap = new HashMap<>();

	/**
	 * Mapping persons name to index value
	 */
	private static Map<Integer, String> nameMap = new HashMap<>();

	/**
	 * Mapping persons to what job they applied to.
	 */
	private static Map<String, String[]> applicationMap = new HashMap<>();

	/**
	 * Creates an adjacency matrix given the input file.
	 * Turns bipartite graph problem into a max flow problem.
	 * 
	 * @param inputFile txt file. Follows the format of [Node from set 1]>[Comma
	 *                  seperated list of nodes in set 2]
	 * 
	 * @return 2D int matrix.
	 */
	private static int[][] createAdjMatrix(File inputFile) {
		int[][] adjMatrix = null;

		Scanner input;
		try {
			ArrayList<String[]> jobs = new ArrayList<>();
			input = new Scanner(inputFile);
			int index = 1;
			int personIndex = 1;
			int jobIndex = 1;
			while (input.hasNextLine()) {

				String line = input.nextLine();

				if (line.isEmpty()) {
					continue;
				}

				String[] applicant = line.split(">");

				if (nameMap.get(index) == null) {
					nameMap.put(index++, applicant[0]);
					personIndex++;
				}
				String[] positions = applicant[1].split(",");
				jobs.add(applicant[1].split(","));

				if (applicationMap.get(applicant[0]) == null) {
					applicationMap.put(applicant[0], positions);
				}
			}
			Iterator<String[]> iterator = jobs.iterator();
			while (iterator.hasNext()) {
				String[] job = iterator.next();

				for (int i = 0; i < job.length; i++) {
					if (jobTitleMap.get(job[i]) == null) {
						jobIndexMap.put(index, job[i]);
						jobTitleMap.put(job[i], index++);
						jobIndex++;
					}
				}
			}
			input.close();
			adjMatrix = fillAdjMatrix(personIndex - 1, jobIndex - 1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return adjMatrix;
	}

	/**
	 * Fills in the adjacency matrix created from inputFile.
	 * Goes from source node, amount of people, amount of jobs for rows and columns.
	 * 1 indicates there is an edge between nodes.
	 * 
	 * @param persons amount of people.
	 * @param jobs    amount of jobs.
	 * @return adjacency matrix filled to indicate edges.
	 */
	private static int[][] fillAdjMatrix(int persons, int jobs) {
		int[][] adjMatrix = new int[persons + jobs + 2][jobs + persons + 2];

		for (int i = 1; i <= persons; i++) {
			String person = nameMap.get(i);
			adjMatrix[0][i] = 1;
			String[] jobPositions = applicationMap.get(person);
			for (int j = 0; j < jobPositions.length; j++) {
				int jobIndex = jobTitleMap.get(jobPositions[j]);
				adjMatrix[i][jobIndex] = 1;
				adjMatrix[jobIndex][persons + jobs + 1] = 1;
			}

		}

		return adjMatrix;

	}

	// DO NOT EDIT THE METHOD _SIGNATURE_ IF YOU WANT CREDIT!
	// That includes "throws" (don't add "throws" to the method signature)
	// Technically you can change this method, but there shouldn't be a
	// reason to. It demonstrates the *expected behavior* of your
	// youCodeGoesInThisMethod() method.
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java Convert filename");
			return;
		}

		File inputFile = new File(args[0]);
		Answer ans = youCodeGoesInThisMethod(inputFile);

		if (ans != null) {
			// prints out the answer...
			System.out.println("***************************************************");
			System.out.println("Max Flow: " + ans.maxFlow);
			System.out.println("Matches:");
			for (String setOneNode : ans.matches.keySet()) {
				System.out.println("\t" + setOneNode + "-->" + ans.matches.get(setOneNode));
			}
			System.out.println("***************************************************");
		}
	}
}