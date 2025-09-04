package katecam.hyuswim.board.dto;

import katecam.hyuswim.board.domain.BoardCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequest {
    private String title;
    private String content;
    private Boolean isAnonymous;
    private BoardCategory boardCategory;
}
