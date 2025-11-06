package katecam.hyuswim.user.service;

import java.util.*;
import java.util.stream.Collectors;

import katecam.hyuswim.auth.domain.UserAuth;
import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.auth.dto.EmailVerifyRequest;
import katecam.hyuswim.auth.repository.UserAuthRepository;
import katecam.hyuswim.auth.service.AuthEmailService;
import katecam.hyuswim.badge.domain.Badge;
import katecam.hyuswim.badge.domain.UserBadge;
import katecam.hyuswim.badge.repository.BadgeRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.dto.mypage.*;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.like.domain.PostLike;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

  private final PostLikeRepository postLikeRepository;
  private final UserAuthRepository userAuthRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthEmailService authEmailService;
  private final BadgeRepository badgeRepository;



    @Transactional
  public MyOverviewResponse selectMyOverview(User loginUser) {
    Long userId = loginUser.getId();
    int postCount = loginUser.getPosts().size();
    int commentCount = loginUser.getComments().size();
    int likeCount = selectMyLikesCount(userId);

    return new MyOverviewResponse(postCount, commentCount, likeCount);
  }

  @Transactional
  public List<MyPostListResponse> selectMyPostList(User loginUser) {
      User user = userRepository.findById(loginUser.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    List<Post> posts = user.getPosts();
    List<MyPostListResponse> myPostListReponseList = new ArrayList<>();
    for (Post post : posts) {
      myPostListReponseList.add(MyPostListResponse.from(post));
    }
    return myPostListReponseList;
  }

  @Transactional
  public List<MyCommentResponse> selectMyCommentList(User loginUser) {
      User user = userRepository.findById(loginUser.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    List<Comment> comments = user.getComments();
    List<MyCommentResponse> myCommentResponseList = new ArrayList<>();
    for (Comment comment : comments) {
      myCommentResponseList.add(MyCommentResponse.from(comment));
    }
    return myCommentResponseList;
  }

  @Transactional
  public List<MyLikedPostResponse> selectMyLikedPostList(User loginUser) {
      User user = userRepository.findById(loginUser.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    List<PostLike> postLikes = postLikeRepository.findByUserId(user.getId());
    List<MyLikedPostResponse> myLikedPostResponseList = new ArrayList<>();
    for (PostLike postLike : postLikes) {
      myLikedPostResponseList.add(MyLikedPostResponse.from(postLike));
    }
    return myLikedPostResponseList;
  }

  @Transactional
  public int selectMyLikesCount(Long userId) {
    return postLikeRepository.countByUserIdAndIsDeletedFalse(userId);
  }

  @Transactional
  public void updateUserProfile(User loginUser, ProfileUpdate profileUpdate) {
    loginUser.updateProfile(profileUpdate.getNickname(), profileUpdate.getIntroduction());
  }

  @Transactional
    public void updateCommentNotification(User loginUser, Boolean enabled) {
      User user = userRepository.findById(loginUser.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
      user.updateCommentNotificationEnabled(enabled);
  }

  @Transactional
    public void updateLikeNotification(User loginUser, Boolean enabled) {
      User user = userRepository.findById(loginUser.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
      user.updateLikeNotificationEnabled(enabled);
  }

  @Transactional
    public MyProfileReponse selectMyProfile(User loginUser) {
      User user = userRepository.findById(loginUser.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      List<Post> posts = user.getPosts();
      List<Comment> comments = user.getComments();
      List<MissionProgress> missionProgresses = user.getMissionProgresses();

      MyProfileReponse.UserInfo userInfo = selectUserInfo(user);
      MyProfileReponse.UserStats userStats = selectUserStats(posts,comments, missionProgresses);
      List<MyProfileReponse.BadgeInfo> badgeInfos = selectBadgeInfo(user);
      List<MyProfileReponse.PostSummary> postSummaries = buildRecentPosts(posts);
      List<MyProfileReponse.CommentInfo> commentInfos = buildRecentComments(comments);
      List<MyProfileReponse.LikedPostInfo> likedPostInfos = buildRecentLikedPosts(user);

      UserAuth localAuth = userAuthRepository.findByUserAndProvider(user, AuthProvider.LOCAL)
              .orElse(null);

      MyProfileReponse.AccountInfo accountInfo = MyProfileReponse.AccountInfo.builder()
              .email(localAuth != null ? localAuth.getEmail() : null)
              .passwordLastChanged(localAuth != null ? localAuth.getPasswordLastChanged() : null)
              .newCommentNotification(user.getCommentNotificationEnabled())
              .likeNoticeNotification(user.getLikeNotificationEnabled())
              .build();

      return new MyProfileReponse(
              userInfo,
              userStats,
              badgeInfos,
              postSummaries,
              commentInfos,
              likedPostInfos,
              accountInfo
      );


  }

  @Transactional
    public MyProfileReponse.UserInfo selectUserInfo(User loginUser) {
      return MyProfileReponse.UserInfo.builder()
              .nickname(loginUser.getNickname())
              .joinDate(loginUser.getCreatedAt())
              .lastActiveDate(loginUser.getLastActiveDate())
              .score(loginUser.getScore())
              .level(loginUser.getLevel())
              .build();
  }


    @Transactional
    public MyProfileReponse.UserStats selectUserStats(List<Post> posts, List<Comment> comments, List<MissionProgress> missionProgresses) {

        return MyProfileReponse.UserStats.builder()
                .totalPosts(posts.size())
                .totalComments(comments.size())
                .totalLikes(posts.stream()
                        .mapToInt(post -> post.getPostLikes().size())
                        .sum())
                .totalMissionClear((int) missionProgresses.stream()
                        .filter(progress -> progress.getIsCompleted())
                        .count())
                .build();
    }

    private List<MyProfileReponse.BadgeInfo> selectBadgeInfo(User loginUser) {
        return loginUser.getUserBadges().stream()
                .map(userBadge -> {
                    var badge = userBadge.getBadge();
                    return MyProfileReponse.BadgeInfo.builder()
                            .name(badge.getName())
                            .iconUrl(badge.getIconUrl())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<MyProfileReponse.PostSummary> buildRecentPosts(List<Post> posts) {
        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(3) // 최대 3개
                .map(post -> MyProfileReponse.PostSummary.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .likeCount(post.getPostLikes().size())
                        .commentCount(post.getComments().size())
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MyProfileReponse.CommentInfo> buildRecentComments(List<Comment> comments) {
        return comments.stream()
                .filter(comment -> !comment.getIsDeleted())
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                .limit(3) // 최대 3개
                .map(comment -> MyProfileReponse.CommentInfo.builder()
                        .commentId(comment.getId())
                        .content(comment.getContent())
                        .postId(comment.getPost().getId())
                        .postTitle(comment.getPost().getTitle())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MyProfileReponse.LikedPostInfo> buildRecentLikedPosts(User loginUser) {
        return postLikeRepository.findByUserId(loginUser.getId()).stream()
                .sorted((l1, l2) -> l2.getCreatedAt().compareTo(l1.getCreatedAt()))
                .limit(3) // 최대 3개
                .map(like -> MyProfileReponse.LikedPostInfo.builder()
                        .likeId(like.getId())
                        .postId(like.getPost().getId())
                        .title(like.getPost().getTitle())
                        .likeCount(like.getPost().getPostLikes().size())
                        .commentCount(like.getPost().getComments().size())
                        .createdAt(like.getPost().getCreatedAt())
                        .likedAt(like.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public MyProfileEditResponse selectMyProfileEdit(User loginUser) {
      return new MyProfileEditResponse(loginUser.getNickname(), loginUser.getIntroduction());
    }

    @Transactional
    public void updatePassword(User user, PasswordUpdateRequest passwordUpdateRequest) {

        UserAuth userAuth = userAuthRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(passwordUpdateRequest.getCurrentPassword(), userAuth.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if(!passwordUpdateRequest.getNewPassword().equals(passwordUpdateRequest.getConfirmPassword())){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String encodedPassword = passwordEncoder.encode(passwordUpdateRequest.getNewPassword());
        userAuth.updatePassword(encodedPassword);
    }

    @Transactional
    public Map<String, String> selectEmail(User user) {
        UserAuth userAuth = userAuthRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Map<String, String> map = new HashMap<>();
        map.put("email",userAuth.getEmail());
        return map;
    }

    @Transactional
    public void verifyEmailCode(EmailVerifyRequest emailVerifyRequest) {
        authEmailService.verifyCode(emailVerifyRequest.getEmail(), emailVerifyRequest.getCode());
    }

    @Transactional
    public void updateEmail(User user, String email) {
        UserAuth userAuth = userAuthRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userAuth.updateEmail(email);
    }

    @Transactional
    public BadgeCollectionResponse selectBadgeCollection(User loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<UserBadge> userBadges = user.getUserBadges();
        Set<Badge> earnedBadgeSet = userBadges.stream()
                .map(UserBadge::getBadge)
                .collect(Collectors.toSet());

        List<BadgeCollectionResponse.EarnedBadge> earnedBadges = userBadges.stream()
                .map(ub -> new BadgeCollectionResponse.EarnedBadge(
                        ub.getBadge().getName(),
                        ub.getBadge().getKind(),
                        ub.getBadge().getIconUrl(),
                        ub.getEarnedAt()
                ))
                .collect(Collectors.toList());

        List<Badge> allBadgeEntities = badgeRepository.findAll();

        List<BadgeCollectionResponse.BadgeInfo> unearnedBadges = allBadgeEntities.stream()
                .filter(badge -> !earnedBadgeSet.contains(badge))
                .map(b -> new BadgeCollectionResponse.BadgeInfo(b.getName(), b.getKind(), b.getIconUrl()))
                .collect(Collectors.toList());

        List<BadgeCollectionResponse.BadgeInfo> allBadges = allBadgeEntities.stream()
                .map(b -> new BadgeCollectionResponse.BadgeInfo(b.getName(), b.getKind(), b.getIconUrl()))
                .collect(Collectors.toList());

        return new BadgeCollectionResponse(earnedBadges, unearnedBadges, allBadges);
    }




}
