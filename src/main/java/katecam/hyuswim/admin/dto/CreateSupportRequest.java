package katecam.hyuswim.admin.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/** 관리자: 지원사업 생성 요청 DTO (immutable, setter 없음) */
public record CreateSupportRequest(
        @NotBlank String name,
        @NotBlank String company,
        String content,
        String place,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        String status,
        String supportType,
        String applicationUrl,
        String imageUrl
) {}
