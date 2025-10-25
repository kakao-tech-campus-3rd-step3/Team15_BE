package katecam.hyuswim.user.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import katecam.hyuswim.user.domain.UserVisit;

public interface UserVisitRepository extends JpaRepository<UserVisit, Long> {
    boolean existsByUser_IdAndVisitDate(Long userId, LocalDate date);

    @Query("select count(v) from UserVisit v where v.user.id = :userId")
    long countDaysByUserId(Long userId);
}

