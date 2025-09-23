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

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportRepository supportRepository;

    public List<SupportResponse> getAllSupports() {
        return supportRepository.findAll()
                .stream()
                .map(SupportResponse::from)
                .toList();
    }

    public List<SupportResponse> getSupportsByType(SupportType type) {
        return supportRepository.findBySupportType(type)
                .stream()
                .map(SupportResponse::from)
                .toList();
    }

    public SupportDetailResponse getSupportDetail(Long id) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
        return SupportDetailResponse.from(support);
    }
}
