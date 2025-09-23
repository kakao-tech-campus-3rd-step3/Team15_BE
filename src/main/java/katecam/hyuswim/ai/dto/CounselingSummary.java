package katecam.hyuswim.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CounselingSummary {
    private String sessionId;
    private String summary;        // 상담 내용 요약
    private String recommendation; // 추천 활동/다음 단계
}
