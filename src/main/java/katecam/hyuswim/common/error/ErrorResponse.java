package katecam.hyuswim.common.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
  private int status;
  private String code;
  private String message;

  public static ErrorResponse from(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .status(errorCode.getStatus().value())
        .code(errorCode.name())
        .message(errorCode.getMessage())
        .build();
  }
}
