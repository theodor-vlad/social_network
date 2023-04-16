package vtheo.code_challenges;

import vtheo.code_challenges.social_network.*;

public class Main {
    public static void main(String[] args) {
        SocialNetwork<Integer> sn = new SocialNetwork<>();
        sn.addFriendshipBetween(1, 2);
        sn.addFriendshipBetween(1, 3);
        sn.addFriendshipBetween(2, 4);
        sn.addFriendshipBetween(2, 5);
        sn.addFriendshipBetween(4, 6);
        sn.addFriendshipBetween(5, 6);
        try {
            // The above definition of the social network would yield the following underlying graph:
            //        1
            //       / \
            //      2   3
            //     / \
            //    4   5
            //     \ /
            //      6
            //
            // ...so computeMinimumLengthBetween(1, 6) will return 3, achievable either through:
            //  * 1 -> 2 -> 4 -> 6, OR
            //  * 1 -> 2 -> 5 -> 6
            //
            // Given that the modelled graph is undirected (edges in the graph ar bidirectional, which translates to
            // friendships being symmetrical - e.g. if A is friends with B, B is friends with A), the order of the
            // parameters is irrelevant. As such, computeMinimumLengthBetween(6, 1) would also return 3.
            int first = 1;
            int second = 6;
            System.out.println("[MAIN] computeMinimumLengthBetween(" + first + ", " + second + ") = " + sn.computeMinimumLengthBetween(first, second));
            System.out.println("[MAIN] computeMinimumLengthBetween(" + second + ", " + first + ") = " + sn.computeMinimumLengthBetween(second, first));
        } catch (SocialNetworkException e) {
            System.out.println("[SOCIAL NETWORK EXCEPTION] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[CATCH ALL] " + e.getMessage());
        }
    }
}