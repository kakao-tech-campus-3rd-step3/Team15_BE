package katecam.hyuswim.board.repository;

import katecam.hyuswim.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}