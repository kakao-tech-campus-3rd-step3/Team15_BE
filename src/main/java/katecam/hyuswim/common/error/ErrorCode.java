package katecam.hyuswim.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  LOGIN_FAILED(HttpStatus.FORBIDDEN, "아이디 또는 비밀번호가 잘못되었습니다"),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
  POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "작성자만 게시글을 수정/삭제할 수 있습니다."),
  COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "작성자만 댓글을 수정/삭제할 수 있습니다."),
  LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요가 존재하지 않습니다."),
  REPORT_REASON_REQUIRED(HttpStatus.BAD_REQUEST, "기타 사유 선택 시 내용은 필수입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다."),
  MISSING_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT토큰이 없거나 유효하지 않습니다");


  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
