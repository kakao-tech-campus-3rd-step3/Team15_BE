package katecam.hyuswim.admin.dto;

import katecam.hyuswim.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPostListResponse {

    private Long id;
    private String title;
    private String writer;
    private Boolean deleted;
    private LocalDateTime createdAt;

    public static AdminPostListResponse from(Post post) {
        return AdminPostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .writer(post.getUser().getNickname())
                .deleted(post.getIsDeleted())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
