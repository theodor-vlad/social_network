package vtheo.code_challenges.social_network;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Class responsible for modelling and managing a social network.
 * @param <NodeType> generic type for a person tracked in the social network (long, String, complex object, etc.)
 */
public class SocialNetwork<NodeType> {

    /**
     * Underlying social network graph which maps each person to their set of friends. A set was chosen instead
     *      of a list mostly in order to avoid friend duplication, but also after considering how the
     *      implementation for the social network might evolve (for example, a requirement might be
     *      added which would specify that friends could also be removed, and doing so would
     *      involve only checking if the friend exists in the set followed by a removal
     *      operation if positive, rather than traversing the entire list and removing
     *      the friend - so constant time (HashSet) vs. linear time (LinkedList)).
     */
    private final Map<NodeType, Set<NodeType>> friends;

    /**
     * Package private string, visible for testing; used to throw a custom error in case of invalid specified person(s)
     *      for method {@link #computeMinimumLengthBetween(Object, Object)}.
     */
    static final String EXCEPTION_INVALID_PERSON = "At least one of the given persons does not exist in the social network!";

    /**
     * Default constructor.
     */
    public SocialNetwork() {
        this.friends = new HashMap<>();
    }

    /**
     * Getter for underlying graph structure, left with default package-level access modifier on purpose,
     *      as this information should only be exposed in tests.
     * @return
     */
    Map<NodeType, Set<NodeType>> getFriends() {
        return this.friends;
    }

    /**
     * Adds a person to the social network.
     * @param person the person to be added to the social network
     */
    public void addPerson(final NodeType person) {
        friends.putIfAbsent(person, new HashSet<>());
    }

    /**
     * Adds a symmetrical friendship relation between two persons.
     * @param firstPerson  the first person adhering to the friendship
     * @param secondPerson the second friend adhering to the friendship
     */
    public void addFriendshipBetween(final NodeType firstPerson, final NodeType secondPerson) {

        // make sure firstPerson has been added to the network beforehand, then add secondPerson to their friends
        addPerson(firstPerson);
        friends.get(firstPerson).add(secondPerson);

        // same for secondPerson; we consider friendships to be symmetrical
        addPerson(secondPerson);
        friends.get(secondPerson).add(firstPerson);
    }

    /**
     * Validates if a person exists in the social network or not (while it may seem that defining a function
     *      just to check if a person exists in the network is not necessary, the need for implementing
     *      validation criteria other than adherence to the network may arise in the future, so
     *      having a common place to validate all of that criteria may be useful).
     * @param person the person to be validated
     * @return true if the person exists in the social network; false otherwise
     */
    private boolean validatePerson(final NodeType person) {
        return friends.containsKey(person);
    }

    /**
     * Computes the minimum length of a friendship chain between two persons in the social network.
     * @param firstPerson  the first person adhering to the friendship
     * @param secondPerson the second friend adhering to the friendship
     * @return the length of a minimal friendship chain between the two persons, or -1 if either
     *         person is not reachable from the other (meaning that they're either not friends
     *         directly, or they have no mutual friends)
     */
    public long computeMinimumLengthBetween(final NodeType firstPerson, final NodeType secondPerson) throws SocialNetworkException {

        // argument validation
        if (!validatePerson(firstPerson) || !validatePerson(secondPerson)) {
            throw new SocialNetworkException(EXCEPTION_INVALID_PERSON);
        }

        // keeps track of whether a person was already visited or not
        Set<NodeType> visited = new HashSet<>();

        // used in order to traverse the social network in a Breadth-First manner
        Queue<BFSWrapper> q = new ArrayDeque<>();

        // returning value for the function, initialized with -1 in case the two persons can't be connected
        long steps = -1;

        // add the starting point for our algorithm
        q.add(new BFSWrapper(firstPerson, 0));

        // BFS traversal of the social network graph; since no costs are attached to the individual friendships
        //      (edges of the graph), BFS assures that the number of steps it takes to reach a target node from
        //      a starting node is minimal
        while (!q.isEmpty()) {

            // safety checks
            BFSWrapper currentContainer = q.poll();
            if (null == currentContainer) {
                break;
            }

            NodeType currentPerson = currentContainer.getPerson();
            long currentSteps = currentContainer.getStepsTaken();

            // we've reached our target -> return the number of steps taken until this point
            if (secondPerson == currentPerson) {
                steps = currentSteps;
                break;
            }

            // for each friend of mine...
            for (final NodeType friend : friends.get(currentPerson)) {

                // ... if they've not already been visited...
                if (visited.contains(friend)) {
                    continue;
                }

                // ... queue them up for a visit, while also increasing the number
                // of steps it took to reach them (one more than the number of steps
                // required to reach me)
                visited.add(friend);
                q.add(new BFSWrapper(friend, 1 + currentSteps));
            }
        }

        return steps;
    }

    /**
     * Wrapper around the number of friendships required to reach a target person in the network from a starting node.
     * Used in function {@link #computeMinimumLengthBetween(Object, Object)}.
     */
    private class BFSWrapper {
        /**
         * The target person.
         */
        private final NodeType person;
        /**
         * The number of direct friendships it takes to link the target person to the starting person, which is
         *      specified in the queue data structure used in {@link #computeMinimumLengthBetween(Object, Object)}.
         */
        private final long stepsTaken;

        public BFSWrapper(NodeType person, long stepsTaken) {
            this.person = person;
            this.stepsTaken = stepsTaken;
        }

        public NodeType getPerson() {
            return person;
        }

        public long getStepsTaken() {
            return stepsTaken;
        }
    }
}

