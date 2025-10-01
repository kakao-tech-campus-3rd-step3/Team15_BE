package katecam.hyuswim.common.error;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {

    //User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    REJOIN_NOT_ALLOWED(HttpStatus.FORBIDDEN, "영구 정지된 사용자는 재가입할 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다"),
    WITHDRAWAL_FAILED(HttpStatus.BAD_REQUEST, "회원탈퇴에 실패했습니다."),

    //Email
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"이메일 발송에 실패했습니다."),
    EMAIL_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증번호가 만료되었거나 존재하지 않습니다."),
    EMAIL_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다."),


    //Auth
    GOOGLE_TOKEN_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "구글 토큰 요청에 실패했습니다."),
    GOOGLE_ID_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "구글 ID 토큰이 유효하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),

    //Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    POST_DELETED(HttpStatus.GONE, "삭제된 게시글입니다."),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "작성자만 게시글을 수정/삭제할 수 있습니다."),

    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "작성자만 댓글을 수정/삭제할 수 있습니다."),

    //Like
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요가 존재하지 않습니다."),

    //Report
    INVALID_REPORT_TYPE(HttpStatus.BAD_REQUEST, "허용되지 않은 신고 타입입니다."),
    REPORT_REASON_REQUIRED(HttpStatus.BAD_REQUEST, "기타 사유 선택 시 내용은 필수입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 신고 내역입니다."),

    //Counseling
    JSON_SERIALIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메시지 직렬화에 실패했습니다."),
    JSON_DESERIALIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메시지 역직렬화에 실패했습니다."),
    REDIS_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 작업 처리 중 오류가 발생했습니다."),
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상담 세션입니다."),

    // Support
    SUPPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지원 사업입니다."),
    INVALID_SUPPORT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 지원 사업 카테고리입니다."),

    //Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}

