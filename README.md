## Context

This repository stores the implementation for the **Social Network** code challenge.

### Challenge

Let's build a social network.

In this social network, each user has friends.

A chain of friends between two users, user A and user B, is a sequence of users starting with A and ending with B, such that for each user in the chain, ua, the subsequent user, ua + 1, are friends.

Given a social network and two users, user A and user B, please write a function that computes the length of the shortest chain of friends between A and B.

### Discussion

#### How did you represent the social network? Why did you choose this representation?

From the statement of the challenge, the most intuitive way to model a social network where friendships are bilateral (if A is friends with/follows B, then B is friends with/follows A) is through an undirected graph. In the code, I chose to represent this by mapping each user (also further referred to in the documentation as a **person**/**node**) to their set of friends.

Firstly, I chose to enable users of the `SocialNetwork` class to specify a generic type for the nodes of the graph (they can be integers, strings, complex objects, etc.), because different social networks may require different underlying implementations, so implementing the class as a template seemed like the most natural approach. For example, during a coding challenge, you would implement a graph using `int` as the underlying class; when trying to teach a class of students the concept of a graph, you would use strings to represent the names of the persons in the social network; when coding a real social platform, you would use more complex, user-defined objects.

Secondly, the most common representation in problems which involve graphs is to create an adjacency matrix or adjacency list, where you would check the adjacency of two nodes A and B either by checking that `1 == adjacency_matrix[A][B]` (former) or if B is in the adjacency list of A (latter). Since I could no longer guarantee that the underlying type of a node is an integer, I chose to go with the second approach, and initially mapped each node to their list of friends. The chosen map implementation is `HashMap`, since the order of insertion does not matter.

Finally, I thought about human errors that can occur when populating the network, the most notable case being recording a friendship relation between A and B twice. To get rid of this and not make any explicit checks for friendship deduplication, I instead chose to map each node to a set of their friends (rather than a list). This choice also covers the addition of a future requirement that may demand user deletion from the network as well (as is often the case in social media platforms), and removing a user would require a lower time complexity with a `HashSet` than with a `LinkedList` (constant time vs. linear time). Again, the order of insertion does not matter, so the chosen implementation for the `Set` interface is `HashSet`.

#### What algorithm did you use to compute the shortest chain of friends? What alternatives did you consider? Why did you choose this algorithm over the alternatives?

It would first be best to define the function which computes the minimum distance between two persons. The function that was derived from the challenge statement has the signature `computeMinimumLengthBetween(final NodeType firstPerson, final NodeType secondPerson)` - so it is called `computeMinimumLengthBetween` and takes two persons as parameters - and returns a `long` as follows:
* `0`, if `firstPerson` and `secondPerson` coincide;
* `-1`, if `secondPerson` is not reachable from `firstPerson` or vice-versa;
* a value greater than 0 representing the minimum length of a friendship chain between `firstPerson` and `secondPerson`, otherwise.

The function also throws a custom exception in case either `firstPerson` or `secondPerson` was not inserted in the social network beforehand.

I chose to traverse the underlying graph in a Breadth-First manner by inserting either A or B in a queue as the starting point of the algorithm, then until the queue is empty proceed to visit any unvisited nodes in the current node's adjacency list (so the chosen algorithm is graph BFS), and record the number of steps made until the current node was first reached along the way, as well as keeping track of visited notes in the graph by use of a set.

I considered two other algorithms for solving the problem: 
1. graph DFS;
2. Dijkstra's algorithm for the shortest path in a graph.

For DFS, I quickly dismissed the idea, since first reaching a node with DFS does not guarantee that the distance travelled to that node is minimal, unlike BFS.

Dijkstra's algorithm could've worked just as fine as BFS, but it wasn't necessary, since it's more targeted towards weighted graphs, and in our case the edges of the graph (i.e. the friendship relations) do not have costs attributed to them (or you could say they have uniform costs - each friendship has a cost of 1), so again BFS would suffice to compute the minimum distance between two nodes.

#### Please enumerate the test cases you considered and explain their relevance.

The test suite consists of 8 unit tests:

1. If a social network is not populated after creation, then the underlying graph should be empty. This is useful because it ensures no other entity meddles with the contents of the underlying representation of the network.

2. If a social network is populated after creation, then the underlying graph should reflect that (the number of nodes and the friendships should exist). This again ensures no external influence over the underlying network representation other than the user themselves, and also ensures the correctness of the functions which add nodes and friendships to the network.

3. The minimum path length in a connected graph (where any node B can be reached from any other node A, `A != B`) should be greater than 0. This is important as it tests the default use-case for the function that needs to be implemented as part of the coding challenge (which is part of the `SocialNetwork` class, and is called `computeMinimumLengthBetween`).

4. The minimum path length between disjoint connected components in a graph (i.e. between two friends pertaining to disjoint friend groups) should be `-1`. This ensures that if B is not reachable from A, the `computeMinimumLengthBetween` function call rightfully returns `-1` to indicate that no chain of friendships exists to link A to B and vice-versa.

5. The minimum path length between a node and itself should be 0. This is important since, if the graph is structured like a cycle, the path from A to themselves should not be the length of the graph, but rather should conform to the value specified in the function definition.

6. If the user tries to compute the minimum length between nodes that do not exist in the social network, an exception should be thrown. This is important in order to ensure the functional correctness of the implemented method as per the definition, and asserts that nodes must be inserted in the network before being considered for minimum path length computations.

7. If the user tries to add a node to the social network twice, it will actually only be inserted once. This ensures the functional correctness of the `addPerson` method.

8. Trying to add a friendship relation to the social network in different scenarios (e.g. adding a friendship between two new persons, adding a friendship between an existing person and a new person and vice-versa, and adding a friendship between two existing persons) does not duplicate nodes/edges in the underlying graph. This ensures the functional correctness of the `addFriendshipBetween` method.

**Note**: No mocking was done in the unit tests, since the class that implements the `computeMinimumLengthBetween` function does not have relevant dependencies that can be mocked. Only input-output assertions are made in the tests.