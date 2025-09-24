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
public class SupportResponse {

    private Long id;
    private String name;
    private String company;
    private SupportType supportType;
    private String supportTypeName;
    private LocalDate endDate;

    public static SupportResponse from(Support support) {
        return SupportResponse.builder()
                .id(support.getId())
                .name(support.getName())
                .company(support.getCompany())
                .supportType(support.getSupportType())
                .supportTypeName(support.getSupportType().getDisplayName())
                .endDate(support.getEndDate())
                .build();
    }
}
