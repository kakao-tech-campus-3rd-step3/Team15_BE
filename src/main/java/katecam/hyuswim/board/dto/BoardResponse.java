package katecam.hyuswim.board.dto;

import katecam.hyuswim.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String boardCategory;
    private String author;
    private Boolean isAnonymous;
    private Boolean isDeleted;
    private Boolean isLiked;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponse from(Board entity) {
        return BoardResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .boardCategory(entity.getBoardCategory().name())
                .author(entity.getUser().getUsername())
                .isAnonymous(entity.getIsAnonymous())
                .isDeleted(entity.getIsDeleted())
                .isLiked(false)
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}