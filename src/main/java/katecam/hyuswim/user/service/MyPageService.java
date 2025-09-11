package katecam.hyuswim.user.service;

import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.MyCommentResponse;
import katecam.hyuswim.user.dto.MyOverviewResponse;
import katecam.hyuswim.user.dto.MyPostListReponse;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public MyOverviewResponse selectMyOverview(User loginUser) {
        String email = loginUser.getEmail();
        int postCount = loginUser.getPosts().size();
        int commentCount = loginUser.getComments().size();
        int likeCount = selectMyLikesCount(email);
        int missionCount = loginUser.getMissionProgresses().size();

        return new MyOverviewResponse(postCount,commentCount,likeCount,missionCount,email);
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
    public int selectMyLikesCount(String email) {
        return postLikeRepository.countByUserEmail(email);
    }


}
