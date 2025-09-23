package katecam.hyuswim.support.dto;

import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.domain.SupportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class SupportDetailResponse {

    private Long id;
    private String name;
    private String company;
    private SupportType supportType;
    private String content;
    private String place;
    private LocalDate endPoint;

    public static SupportDetailResponse from(Support support) {
        return SupportDetailResponse.builder()
                .id(support.getId())
                .name(support.getName())
                .company(support.getCompany())
                .supportType(support.getSupportType())
                .content(support.getContent())
                .place(support.getPlace())
                .endPoint(support.getEndPoint())
                .build();
    }
}
