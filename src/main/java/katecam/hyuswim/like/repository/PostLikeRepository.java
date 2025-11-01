package katecam.hyuswim.like.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.like.domain.PostLike;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);

    @Query("""
        select pl.post.id
        from PostLike pl
        where pl.user.id = :userId
          and pl.isDeleted = false
          and pl.post.id in :postIds
        """)
    List<Long> findLikedPostIdsByUserIdAndPostIds(
            @Param("userId") Long userId,
            @Param("postIds") List<Long> postIds
    );


    @Query("select count(pl) from PostLike pl where pl.post.id = :postId and pl.isDeleted = false")
    int countByPostId(Long postId);

    int countByUserIdAndIsDeletedFalse(Long userId);

    List<PostLike> findByUserId(Long userId);

}
