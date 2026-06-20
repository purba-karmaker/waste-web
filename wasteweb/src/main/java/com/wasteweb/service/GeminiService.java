package com.wasteweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wasteweb.model.WasteAnalysis;
import com.wasteweb.model.WasteItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WasteAnalysis analyzeWaste(WasteItem item) {
        try {
            String prompt = buildPrompt(item);
            String response = callGroq(prompt);
            System.out.println("=== GROQ RESPONSE: " + response);
            return parseAnalysis(response, item);
        } catch (Exception e) {
            System.err.println("Groq error: " + e.getMessage());
            return buildFallbackAnalysis(item);
        }
    }

    private String buildPrompt(WasteItem item) {
        return String.format("""
            You are an expert in circular economy and waste-to-resource transformation.
            
            Analyze this waste item and respond in STRICT JSON format only (no markdown, no extra text, no code fences):
            
            Item: %s
            Quantity: %s %s
            Location: %s
            
            Respond with this exact JSON structure:
            {
              "cleanDescription": "professional, specific description of the waste item",
              "category": "one of: Food & Organic, Material, Packaging, Textile, Electronic, Chemical, Other",
              "reuseOptions": [
                {
                  "title": "Option name",
                  "description": "How this waste becomes a resource",
                  "icon": "single emoji",
                  "priority": "HIGH or MEDIUM or LOW"
                }
              ],
              "impactStats": {
                "co2SavedKg": number,
                "landfillAvoidedKg": number,
                "equivalentTo": "fun real-world equivalent e.g. not driving 17 miles"
              },
              "matches": [
                {
                  "name": "Organization or person name",
                  "type": "Urban Farm, Community Garden, Artist, School, etc.",
                  "distance": "X.X miles",
                  "need": "what they need this for",
                  "urgency": "HIGH, MEDIUM, or LOW"
                }
              ]
            }
            
            Provide exactly 3 reuseOptions and exactly 3 matches. Be specific and creative.
            Make impact stats realistic based on the quantity.
            Return ONLY the JSON object, nothing else.
            """,
                item.getDescription(),
                item.getQuantity(),
                item.getUnit() != null ? item.getUnit() : "units",
                item.getLocation() != null ? item.getLocation() : "local area"
        );
    }

    private String callGroq(String prompt) throws Exception {
        String requestBody = objectMapper.writeValueAsString(new java.util.HashMap<>() {{
            put("model", "llama-3.3-70b-versatile");
            put("temperature", 0.7);
            put("messages", List.of(new java.util.HashMap<>() {{
                put("role", "user");
                put("content", prompt);
            }}));
        }});

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROQ_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("=== GROQ STATUS: " + response.statusCode());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Groq API returned " + response.statusCode() + ": " + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());
        return root.path("choices").get(0).path("message").path("content").asText();
    }

    private WasteAnalysis parseAnalysis(String jsonText, WasteItem item) {
        try {
            String clean = jsonText
                    .replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .trim();

            JsonNode root = objectMapper.readTree(clean);
            WasteAnalysis analysis = new WasteAnalysis();
            analysis.setCleanDescription(root.path("cleanDescription").asText(item.getDescription()));
            analysis.setCategory(root.path("category").asText("Other"));

            List<WasteAnalysis.ReuseOption> options = new ArrayList<>();
            for (JsonNode node : root.path("reuseOptions")) {
                options.add(new WasteAnalysis.ReuseOption(
                        node.path("title").asText(),
                        node.path("description").asText(),
                        node.path("icon").asText("♻️"),
                        node.path("priority").asText("MEDIUM")
                ));
            }
            analysis.setReuseOptions(options);

            JsonNode stats = root.path("impactStats");
            analysis.setImpactStats(new WasteAnalysis.ImpactStats(
                    stats.path("co2SavedKg").asDouble(0),
                    stats.path("landfillAvoidedKg").asDouble(0),
                    stats.path("equivalentTo").asText("")
            ));

            List<WasteAnalysis.Match> matches = new ArrayList<>();
            for (JsonNode node : root.path("matches")) {
                matches.add(new WasteAnalysis.Match(
                        node.path("name").asText(),
                        node.path("type").asText(),
                        node.path("distance").asText(""),
                        node.path("need").asText(),
                        node.path("urgency").asText("MEDIUM")
                ));
            }
            analysis.setMatches(matches);
            return analysis;

        } catch (Exception e) {
            System.err.println("Parse error: " + e.getMessage());
            return buildFallbackAnalysis(item);
        }
    }

    private WasteAnalysis buildFallbackAnalysis(WasteItem item) {
        WasteAnalysis analysis = new WasteAnalysis();
        analysis.setCleanDescription(item.getDescription());
        analysis.setCategory("Other");
        analysis.setReuseOptions(List.of(
                new WasteAnalysis.ReuseOption("Composting", "Convert to nutrient-rich compost", "🌱", "HIGH"),
                new WasteAnalysis.ReuseOption("Upcycling", "Transform into new products", "♻️", "MEDIUM"),
                new WasteAnalysis.ReuseOption("Donation", "Share with community organizations", "🤝", "MEDIUM")
        ));
        analysis.setImpactStats(new WasteAnalysis.ImpactStats(2.5, 5.0, "not driving 10 miles"));
        analysis.setMatches(List.of(
                new WasteAnalysis.Match("Green Community Garden", "Urban Farm", "0.5 miles", "Composting material", "HIGH"),
                new WasteAnalysis.Match("Local Schools Network", "Education", "1.2 miles", "Science projects", "MEDIUM"),
                new WasteAnalysis.Match("Makers Studio", "Workshop", "2.1 miles", "Raw materials", "LOW")
        ));
        return analysis;
    }
}