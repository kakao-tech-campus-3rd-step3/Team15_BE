package katecam.hyuswim.user;

import katecam.hyuswim.common.ApiResponse;
import katecam.hyuswim.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponse<String> handleUserNotFound(UserNotFoundException e) {
        return ApiResponse.error(e.getMessage());
    }
}
