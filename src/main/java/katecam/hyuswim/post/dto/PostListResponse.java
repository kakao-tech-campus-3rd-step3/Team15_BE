package katecam.hyuswim.post.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.post.domain.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {

    private Long id;
    private String postCategory;
    private String postCategoryName;
    private String title;
    private String content;
    private String author;
    private Long likeCount;
    private Long viewCount;
    private Long commentCount;
    private boolean isLiked;
    private LocalDateTime createdAt;

    public PostListResponse(Long id,
                            PostCategory postCategory,
                            String title,
                            String content,
                            String author,
                            Long likeCount,
                            Long commentCount,
                            Long viewCount,
                            LocalDateTime createdAt) {
        this.id = id;
        this.postCategory = postCategory.name();
        this.postCategoryName = postCategory.getDisplayName();
        this.title = title;
        this.content = content;
        this.author = author;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.isLiked = false;
        this.createdAt = createdAt;
    }

    public PostListResponse withLiked(boolean isLiked) {
        this.isLiked = isLiked;
        return this;
    }
}

