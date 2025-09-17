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
public class AdminPostDetailResponse {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private Boolean deleted;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AdminPostDetailResponse from(Post post) {
        return AdminPostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getUser().getNickname())
                .deleted(post.getIsDeleted())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
