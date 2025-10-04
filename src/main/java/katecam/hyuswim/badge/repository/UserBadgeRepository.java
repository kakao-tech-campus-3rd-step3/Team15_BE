package katecam.hyuswim.badge.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import katecam.hyuswim.badge.domain.UserBadge;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    @Query("select ub.badge.id from UserBadge ub where ub.user.id = :userId")
    Set<Long> findOwnedBadgeIdsByUserId(Long userId);

    @Query("select ub from UserBadge ub where ub.user.id = :userId")
    List<UserBadge> findAllByUserId(Long userId);
}
