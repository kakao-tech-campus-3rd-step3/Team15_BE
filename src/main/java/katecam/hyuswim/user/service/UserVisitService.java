package katecam.hyuswim.user.service;

import java.time.LocalDate;
import katecam.hyuswim.user.domain.UserVisit;
import katecam.hyuswim.user.repository.UserRepository;
import katecam.hyuswim.user.repository.UserVisitRepository;
import katecam.hyuswim.user.event.UserVisitedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserVisitService {

    private final UserRepository userRepository;
    private final UserVisitRepository userVisitRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public void touch(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        var today = LocalDate.now();

        boolean firstVisitToday = !userVisitRepository.existsByUser_IdAndVisitDate(userId, today);
        if (firstVisitToday) {
            userVisitRepository.saveAndFlush(new UserVisit(user, today));
            eventPublisher.publishEvent(new UserVisitedEvent(userId));
        }
    }
}


