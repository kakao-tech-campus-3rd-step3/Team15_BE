package katecam.hyuswim.board.controller;

import katecam.hyuswim.board.dto.BoardRequest;
import katecam.hyuswim.board.dto.BoardResponse;
import katecam.hyuswim.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public BoardResponse createBoard(@RequestBody BoardRequest request,
                                     @RequestParam Long userId) {
        return boardService.createBoard(request, userId);
    }
}
