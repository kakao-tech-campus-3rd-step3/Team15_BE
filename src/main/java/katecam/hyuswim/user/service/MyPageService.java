package katecam.hyuswim.user.service;

import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.MyOverviewResponse;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public int selectMyLikesCount(String email) {
        return postLikeRepository.countByUserEmail(email);
    }


}
