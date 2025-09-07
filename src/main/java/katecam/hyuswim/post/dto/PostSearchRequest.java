package katecam.hyuswim.post.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import katecam.hyuswim.post.domain.PostCategory;
import lombok.Getter;

@Getter
public class PostSearchRequest {
  private PostCategory category;
  private String keyword;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;
}
