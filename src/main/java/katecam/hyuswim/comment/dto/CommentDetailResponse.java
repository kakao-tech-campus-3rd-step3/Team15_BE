package katecam.hyuswim.comment.dto;

import katecam.hyuswim.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentDetailResponse {
    private Long id;
    private Long postId;
    private String author;
    private String content;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentDetailResponse from(Comment entity){
        return CommentDetailResponse.builder()
                .id(entity.getId())
                .postId(entity.getPost().getId())
                .author(entity.getUser().getNickname())
                .content(entity.getContent())
                .isAnonymous(entity.getIsAnonymous())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
