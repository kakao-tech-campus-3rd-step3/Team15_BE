package katecam.hyuswim.board.service;

import katecam.hyuswim.board.domain.Board;
import katecam.hyuswim.board.repository.BoardRepository;
import katecam.hyuswim.board.dto.BoardRequest;
import katecam.hyuswim.board.dto.BoardResponse;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public BoardResponse createBoard(BoardRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Board board = new Board(
                request.getTitle(),
                request.getContent(),
                request.getBoardCategory(),
                user,
                request.getIsAnonymous()
        );

        Board saved = boardRepository.save(board);
        return BoardResponse.from(saved);
    }
}

