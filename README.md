# maximal-bipartite-graph-matching

## ***Description***

Java program to solve the maximal bipartite graph matching problem. A bipartite graph is a graph where there are two sets of nodes. Nodes from set A can only be connected with an edge to nodes in set B. This problem wants to match as many nodes from set A to set B without reusing any nodes from either set.

Some examples of this type of problems are:
    - Matching job applicants to job openings.
    - Matching animal's up for adoption at a shelter to people.
    - Matching passengers to ride-share drivers.

For the examples listed above, we want the maximum possible connections from set A to set B. To solve this problem we can turn this graph into a network flow graph. A network flow graph includes two seperate nodes called the source and sink. There is a flow coming out of the source and into the sink. We attach the source node to all the nodes in set A, and attach the nodes in set B to the sink node. By implementing the Ford-Fulkerson max flow algorithm, we can find which nodes from set A that match to nodes in set B if there is a flow between them.

## ***How to run***

Navigate to a directory and run the following command in your terminal

```sh
git clone https://github.com/btalastas/maximal-bipartite-graph-matching.git
```

Next navigate to the `codeGoesHere` directory
```sh
cd maximal-bipartite-graph-matching/codeGoesHere
```

Compile the java files with the following command

```sh
javac *.java
```

Finally, to run the java program run the following command with any of the example txt files

```sh
java Convert ../example.txt
```

## ***Input/Output***

The txt files follow the following format

`[Node from set A]>[Comma seperated list of nodes in set 2]`

Each row signifies a new node in set A, and the right side represents nodes in set B.

The example.txt file shows the following.

```txt
Person 1>Software Developer,Software Engineer
Person 2>Software Engineer
Person 3>Software Developer,Network Engineer,Interface Developer
Person 4>Software Engineer,Technical Manager
Person 5>Technical Manager
```

Running the program with this txt file outputs the following.

```sh
***************************************************
Max Flow: 4
Matches:
        Person 1-->Software Developer
        Person 2-->Software Engineer
        Person 3-->Network Engineer
        Person 4-->Technical Manager
***************************************************
```

## ***Acknowledgements***

Professor Katherine Russell

