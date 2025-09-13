package katecam.hyuswim.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.like.domain.PostLike;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.mypage.MyCommentResponse;
import katecam.hyuswim.user.dto.mypage.MyLikedPostResponse;
import katecam.hyuswim.user.dto.mypage.MyOverviewResponse;
import katecam.hyuswim.user.dto.mypage.MyPostListReponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final PostLikeRepository postLikeRepository;

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
}
