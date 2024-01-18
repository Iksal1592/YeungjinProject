package org.zerock.b01.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIServiceImpl implements OpenAIService {
    private final String apiEndpoint = "https://api.openai.com/v1/engines/text-davinci-003/completions";
    private final String apiKey = "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"; // 여기에 실제 API 키를 입력합니다.

    public String getRelatedWords(String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            String requestBody = "{\"prompt\":\"" + query + " 이 단어와 관련된 단어를 다른 필요없는 말 쓰지말고 '추천단어:~,  이유 : ~ '의 형식으로 답변해줘\", \"max_tokens\": 200}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiEndpoint, entity, String.class);






            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode choicesNode = rootNode.path("choices");

            if (choicesNode.isArray()) {
                JsonNode firstChoice = choicesNode.get(0);
                return firstChoice.path("text").asText();
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}