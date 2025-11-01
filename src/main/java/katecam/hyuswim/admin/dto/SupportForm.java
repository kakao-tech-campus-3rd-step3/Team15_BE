package katecam.hyuswim.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** 관리자 폼 바인딩 전용 immutable DTO (record) */
public record SupportForm(
        @NotBlank String name,      // 지원사업명
        @NotBlank String company,   // 제공기관
        String content,             // 내용(선택)
        String place,               // 장소(선택)
        LocalDate endDate,          // 마감일(선택)
        @NotNull String supportType // Enum 이름 또는 문자열
) {}
