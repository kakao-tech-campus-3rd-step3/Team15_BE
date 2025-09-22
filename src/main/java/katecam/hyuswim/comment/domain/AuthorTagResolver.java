package katecam.hyuswim.comment.domain;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserRole;
import org.springframework.stereotype.Component;

@Component
public class AuthorTagResolver {
    public AuthorTag resolve(User user, Post post) {
        if (user.getRole() == UserRole.AI) {
            return AuthorTag.AI;
        }
        if (user.getId().equals(post.getUser().getId())) {
            return AuthorTag.AUTHOR;
        }
        return AuthorTag.NORMAL;
    }
}
