package katecam.hyuswim.post.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import katecam.hyuswim.post.dto.PostStatsResponse;
import katecam.hyuswim.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostStatService {

  private final PostRepository postRepository;

  public PostStatsResponse getPostStats() {
    LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
    LocalDateTime endOfToday = startOfToday.plusDays(1).minusNanos(1);

    LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
    LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).minusNanos(1);

    long totalCount = postRepository.countByIsDeletedFalse();
    long todayCount =
        postRepository.countByIsDeletedFalseAndCreatedAtBetween(startOfToday, endOfToday);
    long weekCount =
        postRepository.countByIsDeletedFalseAndCreatedAtBetween(startOfWeek, endOfWeek);

    return new PostStatsResponse(totalCount, weekCount, todayCount);
  }
}
