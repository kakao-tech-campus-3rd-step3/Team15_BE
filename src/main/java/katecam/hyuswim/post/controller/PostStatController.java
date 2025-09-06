package katecam.hyuswim.post.controller;

import katecam.hyuswim.common.ApiResponse;
import katecam.hyuswim.post.dto.PostStatsResponse;
import katecam.hyuswim.post.service.PostStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostStatController {

    private final PostStatService postStatService;

    @GetMapping("/stats")
    public ApiResponse<PostStatsResponse> getPostStats() {
        return ApiResponse.success(postStatService.getPostStats());
    }
}
