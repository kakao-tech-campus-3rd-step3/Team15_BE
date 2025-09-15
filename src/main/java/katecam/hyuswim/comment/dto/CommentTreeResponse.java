package katecam.hyuswim.comment.dto;

import katecam.hyuswim.comment.domain.AuthorTag;
import katecam.hyuswim.comment.domain.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CommentTreeResponse {
    private final Long id;
    private final Long postId;
    private final String content;
    private final String author;
    private final String handle;
    private final AuthorTag authorTag;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentTreeResponse> children;

    public static CommentTreeResponse from(Comment comment){
        return new CommentTreeResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getUser().getHandle(),
                comment.getAuthorTag(),
                comment.getIsDeleted(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getChildren().stream()
                        .map(katecam.hyuswim.comment.dto.CommentTreeResponse::from)
                        .toList()

        );
    }
}
