package katecam.hyuswim.post.dto;

import katecam.hyuswim.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String postCategory;
    private String author;
    private Boolean isAnonymous;
    private Boolean isDeleted;
    private Boolean isLiked;
    private Long viewCount;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post entity) {
        return PostResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .postCategory(entity.getPostCategory().name())
                .author(entity.getUser().getUsername())
                .isAnonymous(entity.getIsAnonymous())
                .isDeleted(entity.getIsDeleted())
                .isLiked(false)
                .viewCount(entity.getViewCount())
                .likeCount((long) entity.getLikes().size())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}