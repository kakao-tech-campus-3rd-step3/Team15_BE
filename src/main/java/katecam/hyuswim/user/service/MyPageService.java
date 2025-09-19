package katecam.hyuswim.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.user.dto.mypage.*;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
  private final UserRepository userRepository;

  @Transactional
  public MyOverviewResponse selectMyOverview(User loginUser) {
    Long userId = loginUser.getId();
    int postCount = loginUser.getPosts().size();
    int commentCount = loginUser.getComments().size();
    int likeCount = selectMyLikesCount(userId);
    int missionCount = loginUser.getMissionProgresses().size();

    return new MyOverviewResponse(postCount, commentCount, likeCount, missionCount);
  }

  @Transactional
  public List<MyPostListReponse> selectMyPostList(User loginUser) {
    List<Post> posts = loginUser.getPosts();
    List<MyPostListReponse> myPostListReponseList = new ArrayList<>();
    for (Post post : posts) {
      myPostListReponseList.add(MyPostListReponse.from(post));
    }
    return myPostListReponseList;
  }

  @Transactional
  public List<MyCommentResponse> selectMyCommentList(User loginUser) {
    List<Comment> comments = loginUser.getComments();
    List<MyCommentResponse> myCommentResponseList = new ArrayList<>();
    for (Comment comment : comments) {
      myCommentResponseList.add(MyCommentResponse.from(comment));
    }
    return myCommentResponseList;
  }

  @Transactional
  public List<MyLikedPostResponse> selectMyLikedPostList(User loginUser) {
    List<PostLike> postLikes = postLikeRepository.findByUserId(loginUser.getId());
    List<MyLikedPostResponse> myLikedPostResponseList = new ArrayList<>();
    for (PostLike postLike : postLikes) {
      Post post = postLike.getPost();
      myLikedPostResponseList.add(
          new MyLikedPostResponse(
              postLike.getId(),
              post.getId(),
              post.getTitle(),
              post.getContent(),
              post.getPostLikes().size(),
              post.getViewCount(),
              post.getCreatedAt()));
    }
    return myLikedPostResponseList;
  }

  @Transactional
  public int selectMyLikesCount(Long userId) {
    return postLikeRepository.countByUserId(userId);
  }

  @Transactional
  public void updateUserProfile(User loginUser, ProfileUpdate profileUpdate) {
    loginUser.updateProfile(profileUpdate.getNickname(), profileUpdate.getIntroduction());
  }

  @Transactional
    public void updateCommentNotification(User loginUser, Boolean enabled) {
      loginUser.updateCommentNotificationEnabled(enabled);
  }

  @Transactional
    public void updateLikeNotification(User loginUser, Boolean enabled) {
      loginUser.updateLikeNotificationEnabled(enabled);
  }

  @Transactional
    public MyProfileReponse selectMyProfile(User loginUser) {
      List<Post> posts = loginUser.getPosts();
      List<Comment> comments = loginUser.getComments();
      List<MissionProgress> missionProgresses = loginUser.getMissionProgresses();

      MyProfileReponse.UserInfo userInfo = selectUserInfo(loginUser);

      MyProfileReponse.UserStats userStats = selectUserStats(posts,comments, missionProgresses);
      List<MyProfileReponse.BadgeInfo> badgeInfos = selectBadgeInfo(loginUser);
      List<MyProfileReponse.PostSummary> postSummaries = buildRecentPosts(posts);
      List<MyProfileReponse.CommentInfo> commentInfos = buildRecentComments(comments);
      List<MyProfileReponse.LikedPostInfo> likedPostInfos = buildRecentLikedPosts(loginUser);
      MyProfileReponse.AccountInfo accountInfo = MyProfileReponse.AccountInfo.builder()
              .email(loginUser.getEmail())
              .passwordLastChanged(loginUser.getPasswordLastChanged())
              .newCommentNotification(loginUser.getCommentNotificationEnabled())
              .likeNoticeNotification(loginUser.getLikeNotificationEnabled())
              .build();
    return new MyProfileReponse(userInfo, userStats, badgeInfos, postSummaries, commentInfos, likedPostInfos, accountInfo);


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
        return loginUser.getBadges().stream()
                .map(badge -> MyProfileReponse.BadgeInfo.builder()
                        .name(badge.getName().toString())
                        .iconUrl(badge.getIconUrl())
                        .build())
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




}
