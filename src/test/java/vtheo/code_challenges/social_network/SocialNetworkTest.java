package vtheo.code_challenges.social_network;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SocialNetworkTest {

    /**
     * Use-case: after social network creation, if no insertions have been made,
     *           the network should be empty.
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: Integer
     * <p>
     * Expectations (E):
     *    #1: after TA#1, we should have no people in the network
     *    #2: after TA#1, the key set returned by the underlying network representation
     *        should be empty
     */
    @Test
    void testNetworkIsEmptyAfterNoInsertions() {
        // TA#1
        SocialNetwork<Integer> sn = new SocialNetwork<>();

        // E#1
        assertEquals(0, sn.getFriends().size());

        // E#2
        assertEquals(Collections.emptySet(), sn.getFriends().keySet());
    }

    /**
     * Use-case: after social network creation, if several insertions have been made,
     *           the network should not be empty.
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: String
     *    #2: add friendship between John and Mary
     *    #3: add friendship between Jane and Bill
     *    #4: add Bob to the network
     * <p>
     * Expectations (E):
     *    #1: after TA#4, we should have 5 people in the network
     *    #2: after TA#4, the key set returned by the underlying network representation
     *        should contain Mary, Jane, John, Bob and Bill
     */
    @Test
    void testNetworkNotEmptyAfterSeveralInsertions() {
        // TA#1-4
        SocialNetwork<String> sn = new SocialNetwork<>();
        sn.addFriendshipBetween("John", "Mary");
        sn.addFriendshipBetween("Jane", "Bill");
        sn.addPerson("Bob");

        // E#1
        assertEquals(5, sn.getFriends().size());

        // E#2
        Set<String> expectedKeySet = new HashSet<String>(Arrays.asList("Mary", "Jane", "John", "Bob", "Bill"));
        assertEquals(expectedKeySet, sn.getFriends().keySet());
    }

    /**
     * Use-case: after creating and populating a social network which represents a connected graph,
     *           the minimum distance between two nodes x and y (x != y) is > 0. Given that the
     *           social network models an undirected graph, the order of x and y in calculating
     *           the distance is arbitrary.
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: Integer
     *    #2: add friendship between 1 and 2
     *    #3: add friendship between 1 and 3
     *    #4: add friendship between 2 and 4
     *    #5: add friendship between 2 and 5
     *    #6: add friendship between 4 and 6
     *    #7: add friendship between 5 and 6
     * <p>
     * Expectations (E):
     *    #1: after TA#7, the minimum distance between 1 and 6 is 3
     *    #2: after TA#7, the minimum distance between 6 and 1 is also 3
     */
    @Test
    void testComputedLengthDefault() throws SocialNetworkException {
        // TA#1-7
        SocialNetwork<Integer> sn = new SocialNetwork<>();
        sn.addFriendshipBetween(1, 2);
        sn.addFriendshipBetween(1, 3);
        sn.addFriendshipBetween(2, 4);
        sn.addFriendshipBetween(2, 5);
        sn.addFriendshipBetween(4, 6);
        sn.addFriendshipBetween(5, 6);

        // E#1
        assertEquals(3, sn.computeMinimumLengthBetween(1, 6));

        // E#2
        assertEquals(3, sn.computeMinimumLengthBetween(6, 1));
    }

    /**
     * Use-case: after creating and populating a social network which represents a graph which
     *           contains at least two disjoint connected components (i.e. two groups of friends
     *           who do not intersect through a common friend), the distance between any node x
     *           and y pertaining to different connected components should be -1 (meaning that
     *           you can't get from x to y - there's no friendship chain between them).
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: Integer
     *    #2: add 1 to the network
     *    #3: add 7 to the network
     * <p>
     * Expectations (E):
     *    #1: after TA#3, the minimum distance between 1 and 7 is -1
     */
    @Test
    void testComputedLengthForUnreachablePerson() throws SocialNetworkException {
        // TA#1-3
        SocialNetwork<Integer> sn = new SocialNetwork<>();
        sn.addPerson(1);
        sn.addPerson(7);

        // E#1
        assertEquals(-1, sn.computeMinimumLengthBetween(1, 7));
    }

    /**
     * Use-case: the distance between a node in the social network and itself should be 0.
     *
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: String
     *    #2: add John to the network
     * <p>
     * Expectations (E):
     *    #1: after TA#2, the minimum distance between John and himself should be 0
     */
    @Test
    void testComputedLengthForSamePerson() throws SocialNetworkException {
        // TA#1-2
        SocialNetwork<String> sn = new SocialNetwork<>();
        String person = "John";
        sn.addPerson(person);

        // E#1: the computed path should be 0 if the initial and target nodes are the same
        assertEquals(0, sn.computeMinimumLengthBetween(person, person));
    }

    /**
     * Use-case: a call to compute the minimum distance between two values which don't exist as nodes
     *           in the social network should always throw a {@link SocialNetworkException} exception.
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: String
     *    #2: add friendship between Jack and Jill
     *    #3: compute the distance between Bob and Bill
     * <p>
     * Expectations (E):
     *    #1: after TA#3, an exception of type {@link SocialNetworkException} should be thrown
     *    #2: after E#1, the exception message should coincide with the one thrown by the function
     *        call to {@link SocialNetwork#computeMinimumLengthBetween(Object, Object)}
     */
    @Test
    void testComputedLengthThrowsException() {
        // TA#1-3, E#1
        Exception e = assertThrows(SocialNetworkException.class, () -> {
            SocialNetwork<String> sn = new SocialNetwork<>();
            sn.addFriendshipBetween("Jack", "Jill");
            sn.computeMinimumLengthBetween("Bob", "Bill");
        });

        // E#2
        assertEquals(SocialNetwork.EXCEPTION_INVALID_PERSON, e.getMessage());
    }

    /**
     * Use-case: trying to add the same node to the network twice should only lead to the node
     *           having one instance in the network.
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: Integer
     *    #2: add 5 to the network
     *    #3: add 5 to the network again
     * <p>
     * Expectations (E):
     *    #1: after TA#2, the size of the network should be 1
     *    #2: after TA#2, the only member in the network should be 5
     *    #3: after TA#3, the size of the network should still be 1
     *    #4: after TA#3, the only member in the network should still be 5
     */
    @Test
    void testAddPerson() {
        // TA#1-2, E#1-2
        SocialNetwork<Integer> sn = new SocialNetwork<>();
        sn.addPerson(5);
        assertEquals(1, sn.getFriends().size());
        assertEquals(new HashSet<>(Arrays.asList(5)), sn.getFriends().keySet());

        // TA#3, E#3-4
        sn.addPerson(5);
        assertEquals(1, sn.getFriends().size());
        assertEquals(new HashSet<>(Arrays.asList(5)), sn.getFriends().keySet());
    }

    /**
     * Use-case: adding friendships between different combinations of nodes in the network
     *           (e.g. adding a friendship between two new persons, adding a friendship
     *           between an existing person and a new person and vice-versa, and
     *           adding a friendship between two existing persons)
     * <p>
     * Test actions (TA):
     *    #1: create a social network with template type: String
     *    #2: add friendship between John and Mary
     *    #3: add friendship between John and Bob
     *    #4: add friendship between Jane and Mary
     *    #5: add friendship between John and Jane
     * <p>
     * Expectations (E):
     *    #1: after TA#2, the size of the network should be 2
     *    #2: after TA#2, the only members in the network should be John and Mary
     *    #3: after TA#3, the size of the network should be 3
     *    #4: after TA#3, the only members in the network should be John, Bob and Mary
     *    #5: after TA#4, the size of the network should be 4
     *    #6: after TA#4, the only members in the network should be John, Bob and Mary and Jane
     *    #7: after TA#5, the size of the network should still be 4
     *    #8: after TA#5, the only members in the network should still be John, Bob and Mary and Jane
     */
    @Test
    void testAddFriendship() {
        // T#1-2, E#1-2
        SocialNetwork<String> sn = new SocialNetwork<>();
        sn.addFriendshipBetween("John", "Mary");
        Set<String> expectedKeySet = new HashSet<String>(Arrays.asList("John", "Mary"));
        assertEquals(2, sn.getFriends().size());
        assertEquals(expectedKeySet, sn.getFriends().keySet());

        // T#3, E#3-4
        sn.addFriendshipBetween("John", "Bob");
        expectedKeySet.add("Bob");
        assertEquals(3, sn.getFriends().size());
        assertEquals(expectedKeySet, sn.getFriends().keySet());

        // T#4, E#5-6
        sn.addFriendshipBetween("Jane", "Mary");
        expectedKeySet.add("Jane");
        assertEquals(4, sn.getFriends().size());
        assertEquals(expectedKeySet, sn.getFriends().keySet());

        // T#5, E#7-8
        sn.addFriendshipBetween("John", "Jane");
        assertEquals(4, sn.getFriends().size());
        assertEquals(expectedKeySet, sn.getFriends().keySet());
    }
}
