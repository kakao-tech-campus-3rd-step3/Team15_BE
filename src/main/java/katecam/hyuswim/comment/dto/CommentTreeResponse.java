package katecam.hyuswim.comment.dto;

import katecam.hyuswim.comment.domain.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CommentTreeResponse {
    private final Long id;
    private final String content;
    private final String author;
    private final boolean isDeleted;
    private final List<CommentTreeResponse> children;

    public static CommentTreeResponse from(Comment comment){
        return new CommentTreeResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getIsDeleted(),
                comment.getChildren().stream()
                        .map(katecam.hyuswim.comment.dto.CommentTreeResponse::from)
                        .toList()

        );
    }
}
