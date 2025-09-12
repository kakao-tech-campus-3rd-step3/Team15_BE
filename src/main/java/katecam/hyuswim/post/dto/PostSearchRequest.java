package katecam.hyuswim.post.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import katecam.hyuswim.post.domain.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchRequest {
  private String keyword;
  private PostCategory category;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;
}
