package katecam.hyuswim.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.dto.mypage.*;
import katecam.hyuswim.user.exception.UserNotFoundException;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.like.domain.PostLike;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.User;
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
  public MyProfileEditResponse selectMyProfileEdit(User loginUser) {
      return new MyProfileEditResponse("/profileImage/"+loginUser.getProfileImage(), loginUser.getNickname(), loginUser.getIntroduction());
  }

  @Transactional
    public void updateCommentNotification(User loginUser, Boolean enabled) {
      loginUser.isCommentNotificationEnabled(enabled);
  }

  @Transactional
    public void updateLikeNotification(User loginUser, Boolean enabled) {
      loginUser.isLikeNotificationEnabled(enabled);
  }

}
