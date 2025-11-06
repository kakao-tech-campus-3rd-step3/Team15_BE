package katecam.hyuswim.post.service;

import katecam.hyuswim.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewSyncScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;

    @Scheduled(fixedRate = 900_000) // 15분마다 실행
    @Transactional
    public void syncViewCountsToDB() {
        Set<String> keys = redisTemplate.keys("post:viewCount:*");
        if (keys.isEmpty()) {
            log.info("🔁 [Scheduler] No keys found for sync.");
            return;
        }

        for (String key : keys) {
            try {
                Long postId = Long.parseLong(key.split(":")[2]);
                String countStr = redisTemplate.opsForValue().get(key);
                log.info("🔁 [Scheduler] key={}, value={}", key, countStr);

                if (countStr == null) continue;
                Long count = Long.parseLong(countStr);

                postRepository.incrementViewCount(postId, count);
                redisTemplate.delete(key);

                log.info("✅ [Scheduler] Synced postId={}, +{} views", postId, count);
            } catch (Exception e) {
                log.error("❌ [Scheduler] Error syncing key: {}", key, e);
            }
        }
    }
}

