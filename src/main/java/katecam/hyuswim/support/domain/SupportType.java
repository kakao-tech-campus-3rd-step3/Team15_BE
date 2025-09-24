package katecam.hyuswim.support.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SupportType {
    EDUCATION("교육"),
    STARTUP("창업"),
    EMPLOYMENT("취업"),
    WELFARE("복지"),
    CULTURE("문화");

    private final String displayName;
}
