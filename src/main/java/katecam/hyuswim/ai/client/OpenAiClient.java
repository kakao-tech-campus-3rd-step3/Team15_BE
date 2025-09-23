package katecam.hyuswim.ai.client;

import katecam.hyuswim.ai.dto.ChatRequest;
import katecam.hyuswim.ai.dto.Message;
import katecam.hyuswim.ai.util.JsonUtils;
import katecam.hyuswim.counseling.domain.CounselingStep;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestClient.Builder restClientBuilder;

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

        String responseBody = restClientBuilder
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .post()
                .uri("/v1/chat/completions")
                .body(request)
                .retrieve()
                .body(String.class);

        return JsonUtils.parseContent(responseBody);
    }

    public String generateCounselingReply(List<Message> conversation, CounselingStep step) {
        if (apiKey == null || apiKey.isBlank()) {
            return "[AI 상담 비활성화 상태]";
        }

        String systemPrompt = """
        너는 사용자의 이야기를 편하게 들어주는 친구 같은 상담자야.
        너무 진지하게만 굴지 말고, 가볍게 공감해주면서 자연스럽게 대화를 이어가.
        AI라는 말은 절대 하지 마.
        존댓말이나 과도한 공손체 대신, 편한 반말 톤을 써.
        사용자가 힘든 이야기를 꺼내면 먼저 감정을 공감해주고,
        그다음에 조금 더 구체적으로 물어봐.
        사용자가 '오늘은 여기까지 하자'라고 말하거나, 대화가 충분히 길어졌다고 판단되면 마지막 답변을 줄 때 반드시 [END_SESSION] 토큰을 포함해 마무리 멘트를 해. [END_SESSION] 토큰은 오직 상담을 종료할 때만 사용해야 해.
        """;

        ChatRequest request = new ChatRequest(
                "gpt-4o-mini",
                buildMessages(systemPrompt, conversation),
                200
        );

        String responseBody = restClientBuilder
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .post()
                .uri("/v1/chat/completions")
                .body(request)
                .retrieve()
                .body(String.class);

        return JsonUtils.parseContent(responseBody);
    }

    private List<Message> buildMessages(String systemPrompt, List<Message> conversation) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", systemPrompt));
        messages.addAll(conversation);
        return messages;
    }
}
