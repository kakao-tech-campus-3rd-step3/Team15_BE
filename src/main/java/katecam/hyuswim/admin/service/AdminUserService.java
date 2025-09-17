package katecam.hyuswim.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.admin.dto.BlockRequest;
import katecam.hyuswim.admin.dto.BlockResponse;
import katecam.hyuswim.admin.dto.UserDetailResponse;
import katecam.hyuswim.admin.dto.UserListResponse;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;
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

        return new UserDetailResponse(u.getId(), u.getEmail(), u.getStatus(), posts, comments);
    }

    @Transactional
    public BlockResponse blockUser(Long userId, BlockRequest request) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        u.blockUntil(request.getUntil(), request.getReason());

        return new BlockResponse(
                u.getId(),
                UserStatus.BLOCKED,
                request.getUntil(), // until 있으면 임시 차단
                request.getReason(),
                "사용자 " + userId + "번을 " + request.getUntil() + "까지 차단했습니다."
        );
    }

    @Transactional
    public BlockResponse unblockUser(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        u.unblock();
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

        u.ban("관리자에 의한 영구 차단");
        return new BlockResponse(
                u.getId(),
                UserStatus.BANNED,
                null, // until 없음 → 영구 차단
                "관리자에 의한 영구 차단",
                "사용자 " + userId + "번을 영구 차단했습니다."
        );
    }

    public List<UserListResponse> findAll() {
        return userRepository.findAll().stream()
                .filter(u -> !"admin@domain.com".equalsIgnoreCase(u.getEmail()))
                .map(u -> new UserListResponse(u.getId(), u.getEmail(), u.getStatus()))
                .collect(Collectors.toList());
    }
}
