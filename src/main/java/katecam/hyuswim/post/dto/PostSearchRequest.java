package katecam.hyuswim.post.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import katecam.hyuswim.post.domain.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchRequest {
  private final String keyword;
  private final PostCategory category;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private final LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private final LocalDate endDate;
}
