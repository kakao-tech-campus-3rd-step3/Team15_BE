package katecam.hyuswim.ai.client;

import katecam.hyuswim.ai.dto.ChatRequest;
import katecam.hyuswim.ai.dto.Message;
import katecam.hyuswim.ai.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${openai.api-key:}")
    private String apiKey;

    public String generateReply(String postTitle, String postContent) {
        if (apiKey == null || apiKey.isBlank()) {
            return "[AI 댓글 비활성화 상태]";
        }

        String prompt = """
            게시글 제목: %s
            게시글 내용: %s

            위 게시글을 읽고 댓글을 작성해줘.
            """.formatted(postTitle, postContent);

        ChatRequest request = new ChatRequest(
                "gpt-4o-mini",
                List.of(
                        new Message("system",
                                "너는 실제 커뮤니티에 활동하는 20대 평범한 유저야.\n" +
                                        "AI라는 말은 절대 하지 마.\n" +
                                        "너무 공손하거나 형식적인 표현 말고, 편한 말투로 대답해.\n" +
                                        "길게 쓰지 말고 한두 문장으로만.\n" +
                                        "친구가 댓글 다는 것처럼 자연스럽게 작성해.\n" +
                                        "이모지, 존댓말, 과장된 위로 금지."
                        ),
                        new Message("user", prompt)
                ),
                100
        );

        String responseBody = webClientBuilder
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .post()
                .uri("/v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return JsonUtils.parseContent(responseBody);
    }
}



