package katecam.hyuswim.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import katecam.hyuswim.user.domain.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.admin.dto.BlockRequest;
import katecam.hyuswim.admin.dto.BlockResponse;
import katecam.hyuswim.admin.dto.UserDetailResponse;
import katecam.hyuswim.admin.dto.UserListResponse;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserBlockHistory;
import katecam.hyuswim.user.domain.UserStatus;
import katecam.hyuswim.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class AdminUserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public AdminUserService(
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public UserDetailResponse getUserWithActivities(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        var posts = postRepository.findAllByUser_IdAndIsDeletedFalse(userId).stream()
                .map(p -> new UserDetailResponse.PostSummary(p.getId(), p.getTitle(), p.getCreatedAt()))
                .collect(Collectors.toList());

        var comments = commentRepository.findAllByUserIdAndIsDeletedFalse(userId).stream()
                .map(c -> new UserDetailResponse.CommentSummary(c.getId(), c.getContent(), c.getCreatedAt()))
                .collect(Collectors.toList());

        return new UserDetailResponse(u.getId(), u.getStatus(), posts, comments);
    }

    @Transactional
    public BlockResponse blockUser(Long userId, BlockRequest request) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        UserBlockHistory history = new UserBlockHistory(u, UserStatus.BLOCKED, request.getUntil(), request.getReason());
        u.getBlockHistories().add(history);

        u.setStatus(UserStatus.BLOCKED);

        return new BlockResponse(
                u.getId(),
                UserStatus.BLOCKED,
                request.getUntil(),
                request.getReason(),
                "사용자 " + userId + "번을 " + request.getUntil() + "까지 차단했습니다."
        );
    }

    @Transactional
    public BlockResponse unblockUser(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        UserBlockHistory history = new UserBlockHistory(u, UserStatus.ACTIVE, null, null);
        u.getBlockHistories().add(history);

        u.setStatus(UserStatus.ACTIVE);

        return new BlockResponse(
                u.getId(),
                UserStatus.ACTIVE,
                null,
                null,
                "사용자 " + userId + "번의 차단을 해제했습니다."
        );
    }

    @Transactional
    public BlockResponse banUser(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        String reason = "관리자에 의한 영구 차단";
        UserBlockHistory history = new UserBlockHistory(u, UserStatus.BANNED, null, reason);
        u.getBlockHistories().add(history);

        u.setStatus(UserStatus.BANNED);

        return new BlockResponse(
                u.getId(),
                UserStatus.BANNED,
                null,
                reason,
                "사용자 " + userId + "번을 영구 차단했습니다."
        );
    }

    public List<UserListResponse> findAll() {
        return userRepository.findByRoleNot(UserRole.ADMIN).stream()
                .map(u -> new UserListResponse(u.getId(), u.getNickname(), u.getStatus()))
                .collect(Collectors.toList());
    }

    public UserDetailResponse getUserDetail(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. ID: " + userId));

        // 게시글 목록 (삭제되지 않은 것만)
        List<UserDetailResponse.PostSummary> posts = postRepository.findAllByUser_IdAndIsDeletedFalse(userId).stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt())) // 최신순 정렬
                .map(p -> new UserDetailResponse.PostSummary(
                        p.getId(),
                        p.getTitle(),
                        p.getCreatedAt()
                ))
                .toList();

        // 댓글 목록 (삭제되지 않은 것만)
        List<UserDetailResponse.CommentSummary> comments = commentRepository.findAllByUserIdAndIsDeletedFalse(userId).stream()
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt())) // 최신순 정렬
                .map(c -> new UserDetailResponse.CommentSummary(
                        c.getId(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .toList();

        return new UserDetailResponse(user.getId(), user.getStatus(), posts, comments);
    }
}
