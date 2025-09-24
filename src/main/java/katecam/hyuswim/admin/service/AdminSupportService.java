package katecam.hyuswim.admin.service;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.dto.SupportDetailResponse;
import katecam.hyuswim.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSupportService {

    private final SupportRepository supportRepository;

    // 신규 등록
    public SupportDetailResponse createSupport(Support support) {
        Support saved = supportRepository.save(support);
        return SupportDetailResponse.from(saved);
    }

    // 수정
    public SupportDetailResponse updateSupport(Long id, Support updateRequest) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        support.update(
                updateRequest.getName(),
                updateRequest.getCompany(),
                updateRequest.getContent(),
                updateRequest.getPlace(),
                updateRequest.getEndPoint(),
                updateRequest.getSupportType()
        );

        Support updated = supportRepository.save(support);
        return SupportDetailResponse.from(updated);
    }

    // 삭제
    public void deleteSupport(Long id) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
        supportRepository.delete(support);
    }
}
