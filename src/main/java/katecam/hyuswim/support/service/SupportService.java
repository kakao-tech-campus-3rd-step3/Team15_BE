package katecam.hyuswim.support.service;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.domain.SupportType;
import katecam.hyuswim.support.dto.SupportResponse;
import katecam.hyuswim.support.dto.SupportDetailResponse;
import katecam.hyuswim.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportRepository supportRepository;

    // 전체 조회
    public List<SupportResponse> getAllSupports() {
        return supportRepository.findAll()
                .stream()
                .map(SupportResponse::from)
                .toList();
    }

    // 카테고리별 조회
    public List<SupportResponse> getSupportsByType(SupportType type) {
        return supportRepository.findBySupportType(type)
                .stream()
                .map(SupportResponse::from)
                .toList();
    }

    // 상세 조회
    public SupportDetailResponse getSupportDetail(Long id) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
        return SupportDetailResponse.from(support);
    }

    // 진행 중인 개수 조회
    public long getActiveSupportCount() {
        return supportRepository.countByEndDateAfter(LocalDate.now());
    }


}

