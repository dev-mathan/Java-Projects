//
//package com.example.evalpro.service;
//
//import com.example.evalpro.model.MCQEntity;
//import com.example.evalpro.repository.MCQRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class Questionservice {
//
//    @Autowired
//    private MCQRepository mcqRepository;
//
//    @Value("${groq.api.key}")
//    private String apiKey;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public List<MCQEntity> generateMCQs(String topic, String summary) {
//
//        try {
//
//            String prompt = """
//                    Generate exactly 10 multiple choice questions from the given topic and summary.
//
//                    Topic: %s
//                    Summary: %s
//
//                    Strict Format:
//
//                    Q: Question text
//                    A) Option A
//                    B) Option B
//                    C) Option C
//                    D) Option D
//                    Answer: A/B/C/D
//                    """.formatted(topic, summary);
//
//            String aiText = callGroqAPI(prompt);
//
//            List<MCQEntity> mcqList = parseMCQs(aiText,topic);
//
//            mcqRepository.saveAll(mcqList); // ✅ Save to DB
//
//            return mcqList;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating MCQs: " + e.getMessage());
//        }
//    }
//
//    private String callGroqAPI(String prompt) throws Exception {
//
//        String url = "https://api.groq.com/openai/v1/chat/completions";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(apiKey);
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", "llama-3.3-70b-versatile");
//        requestBody.put("temperature", 0.5);
//
//        List<Map<String, String>> messages = new ArrayList<>();
//
//        Map<String, String> message = new HashMap<>();
//        message.put("role", "user");
//        message.put("content", prompt); // ✅ USING YOUR TOPIC & SUMMARY
//
//        messages.add(message);
//        requestBody.put("messages", messages);
//
//        HttpEntity<String> entity =
//                new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                entity,
//                String.class
//        );
//
//        JsonNode root = mapper.readTree(response.getBody());
//
//        return root.path("choices")
//                .get(0)
//                .path("message")
//                .path("content")
//                .asText();
//    }
//
//    private List<MCQEntity> parseMCQs(String aiText , String topic) {
//
//        List<MCQEntity> list = new ArrayList<>();
//
//        if (aiText == null || aiText.isEmpty()) return list;
//
//        String[] blocks = aiText.split("Q:");
//
//        for (String block : blocks) {
//
//            if (block.trim().isEmpty()) continue;
//
//            MCQEntity mcq = new MCQEntity();
//            String[] lines = block.split("\n");
//
//            mcq.setQuestion(lines[0].trim());
//
//            for (String line : lines) {
//
//                line = line.trim();
//
//                if (line.startsWith("A)"))
//                    mcq.setOptionA(line.substring(2).trim());
//
//                else if (line.startsWith("B)"))
//                    mcq.setOptionB(line.substring(2).trim());
//
//                else if (line.startsWith("C)"))
//                    mcq.setOptionC(line.substring(2).trim());
//
//                else if (line.startsWith("D)"))
//                    mcq.setOptionD(line.substring(2).trim());
//
//                else if (line.startsWith("Answer:"))
//                    mcq.setCorrectAnswer(line.substring(7).trim());
//            }
//
//            list.add(mcq);
//        }
//
//        return list;
//    }
//}


package com.example.evalpro.service;

import com.example.evalpro.model.MCQEntity;
import com.example.evalpro.repository.MCQRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class Questionservice {

    @Autowired
    private MCQRepository mcqRepository;

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // ✅ Added department parameter
    public List<MCQEntity> generateMCQs(String topic, String summary,String department) {

        try {

            String prompt = """
                    Generate exactly 10 multiple choice questions from the given topic and summary.

                    Topic: %s
                    Summary: %s

                    Strict Format:

                    Q: Question text
                    A) Option A
                    B) Option B
                    C) Option C
                    D) Option D
                    Answer: A/B/C/D
                    """.formatted(topic, summary);

            String aiText = callGroqAPI(prompt);

            // ✅ Pass topic & department
            List<MCQEntity> mcqList = parseMCQs(aiText, topic, department);

            mcqRepository.saveAll(mcqList);

            return mcqList;

        } catch (Exception e) {
            throw new RuntimeException("Error generating MCQs: " + e.getMessage());
        }
    }

    private String callGroqAPI(String prompt) throws Exception {

        String url = "https://api.groq.com/openai/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile");
        requestBody.put("temperature", 0.5);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        messages.add(message);
        requestBody.put("messages", messages);

        HttpEntity<String> entity =
                new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        JsonNode root = mapper.readTree(response.getBody());

        return root.path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();
    }

    // ✅ Updated parse method
    private List<MCQEntity> parseMCQs(String aiText, String topic, String department) {

        List<MCQEntity> list = new ArrayList<>();

        if (aiText == null || aiText.isEmpty()) return list;

        String[] blocks = aiText.split("Q:");

        for (String block : blocks) {

            if (block.trim().isEmpty()) continue;

            MCQEntity mcq = new MCQEntity();
            String[] lines = block.split("\n");

            mcq.setQuestion(lines[0].trim());

            for (String line : lines) {

                line = line.trim();

                if (line.startsWith("A)"))
                    mcq.setOptionA(line.substring(2).trim());

                else if (line.startsWith("B)"))
                    mcq.setOptionB(line.substring(2).trim());

                else if (line.startsWith("C)"))
                    mcq.setOptionC(line.substring(2).trim());

                else if (line.startsWith("D)"))
                    mcq.setOptionD(line.substring(2).trim());

                else if (line.startsWith("Answer:"))
                    mcq.setCorrectAnswer(line.substring(7).trim());
            }

            mcq.setTopic(topic);
            mcq.setDepartment(department);

            list.add(mcq);
        }

        return list;
    }
}
