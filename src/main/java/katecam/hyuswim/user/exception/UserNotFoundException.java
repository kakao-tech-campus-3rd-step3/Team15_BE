package katecam.hyuswim.user.exception;

import javax.naming.AuthenticationException;

public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException(String message) {
        super(message);
    }
}
