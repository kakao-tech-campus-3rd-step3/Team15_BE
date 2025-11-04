package katecam.hyuswim.post.dto;

import java.time.LocalDateTime;
import katecam.hyuswim.post.domain.Post;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponse {
    private Long id;
    private String postCategory;
    private String postCategoryName;
    private String title;
    private String content;
    private String author;
    private String handle;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Boolean isAuthor;
    private Boolean isAnonymous;
    private Boolean isLiked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDetailResponse from(Post post, long likeCount, long commentCount, long viewCount) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .postCategory(post.getPostCategory().name())
                .postCategoryName(post.getPostCategory().getDisplayName())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getDisplayName())
                .handle(post.getUser().getHandle())
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .isAuthor(false)
                .isAnonymous(post.getIsAnonymous())
                .isLiked(false)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostDetailResponse from(Post post, boolean isAuthor, boolean isLiked, long likeCount, long commentCount, long viewCount) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .postCategory(post.getPostCategory().name())
                .postCategoryName(post.getPostCategory().getDisplayName())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getDisplayName())
                .handle(post.getUser().getHandle())
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .isAuthor(isAuthor)
                .isAnonymous(post.getIsAnonymous())
                .isLiked(isLiked)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
