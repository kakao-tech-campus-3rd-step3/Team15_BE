package katecam.hyuswim.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.admin.dto.BlockRequest;
import katecam.hyuswim.admin.dto.BlockResponse;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.UserStatus;
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

  public UserResponse getUserWithActivities(Long userId) {
    User u =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    var posts =
        postRepository.findAllByUser_IdAndIsDeletedFalse(userId).stream()
            .map(p -> new UserResponse.PostSummary(p.getId(), p.getTitle(), p.getCreatedAt()))
            .collect(Collectors.toList());

    var comments =
        commentRepository.findAllByUser_IdAndIsDeletedFalse(userId).stream()
            .map(c -> new UserResponse.CommentSummary(c.getId(), c.getContent(), c.getCreatedAt()))
            .collect(Collectors.toList());

    return new UserResponse(u.getId(), u.getEmail(), u.getStatus(), posts, comments);
  }

  @Transactional
  public BlockResponse blockUser(Long userId, BlockRequest request) {
    User u =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    if ("TEMPORARY".equalsIgnoreCase(request.getType())) {
      u.blockUntil(request.getUntil(), request.getReason());
      return new BlockResponse(
          u.getId(),
          UserStatus.BLOCKED,
          "TEMPORARY",
          request.getUntil(),
          request.getReason(),
          "사용자 " + userId + "번을 일시 차단했습니다.");
    } else {
      u.blockPermanently(request.getReason());
      return new BlockResponse(
          u.getId(),
          UserStatus.BLOCKED,
          "PERMANENT",
          null,
          request.getReason(),
          "사용자 " + userId + "번을 영구 차단했습니다.");
    }
  }

  @Transactional
  public BlockResponse unblockUser(Long userId) {
    User u =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    u.unblock();
    return new BlockResponse(
        u.getId(), UserStatus.ACTIVE, null, null, null, "사용자 " + userId + "번의 차단을 해제했습니다.");
  }

  @Transactional
  public BlockResponse banUser(Long userId) {
    User u =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    u.ban("관리자에 의한 영구 차단");
    return new BlockResponse(
        u.getId(),
        UserStatus.BANNED,
        null,
        null,
        "관리자에 의한 영구 차단",
        "사용자 " + userId + "번을 영구 차단(ban)했습니다.");
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }
}
