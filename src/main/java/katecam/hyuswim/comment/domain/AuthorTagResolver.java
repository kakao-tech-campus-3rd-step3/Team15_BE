package katecam.hyuswim.comment.domain;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorTagResolver {
    public AuthorTag resolve(User user, Post post){
        if (user.getId().equals(post.getUser().getId())){
            return AuthorTag.AUTHOR;
        }
        return AuthorTag.NORMAL;
    }
}
