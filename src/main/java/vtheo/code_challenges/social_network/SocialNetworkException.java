package vtheo.code_challenges.social_network;

/**
 * Class which implements exceptions thrown by the {@link SocialNetwork} module (currently only one such custom exception).
 */
public class SocialNetworkException extends Exception {
    public SocialNetworkException(String message) {
        super(message);
    }
}
