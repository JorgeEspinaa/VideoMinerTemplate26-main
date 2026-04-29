package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.DailymotionVideo;
import aiss.dailymotionminer.model.DailymotionVideoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.*;
import java.util.*;

@Service
public class VideoMinerIntegrationService {

    @Autowired
    private DailymotionService dailymotionService;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String VIDEOMINER_API_BASE = "http://localhost:8080/api";

    public Map<String, Object> fetchAndStoreChannelData(String userId, int maxVideos, int maxPages) {
    Map<String, Object> result = new HashMap<>();
    
    try {
        // 1. Fetch videos from Dailymotion
        DailymotionVideoResponse videoResponse = dailymotionService.getVideosByUser(userId, maxVideos, maxPages);
        if (videoResponse == null || videoResponse.getVideos() == null) {
            result.put("status", "error");
            result.put("message", "Failed to fetch videos from Dailymotion");
            return result;
        }

        // 2. Fetch Channel details (Petición extra para datos reales)
        Map<String, Object> channel = new HashMap<>();
        channel.put("id", userId);
        
        try {
            Map<String, Object> userDetails = restTemplate.getForObject("https://api.dailymotion.com/user/" + userId, Map.class);
            if (userDetails != null) {
                channel.put("name", userDetails.getOrDefault("screenname", userId));
                channel.put("description", userDetails.getOrDefault("description", "No description"));
                Object createdTime = userDetails.get("created_time");
                channel.put("createdTime", createdTime != null ? new Date(((Number)createdTime).longValue() * 1000).toString() : new Date().toString());
            }
        } catch (Exception e) {
            channel.put("name", userId);
            channel.put("description", "Channel from Dailymotion: " + userId);
            channel.put("createdTime", new Date().toString());
        }

        // 3. For each video, prepare the data and add it to a list
        List<Map<String, Object>> videosList = new ArrayList<>();
        List<String> storedVideos = new ArrayList<>();
        
        for (DailymotionVideo video : videoResponse.getVideos()) {
            Map<String, Object> videoData = new HashMap<>();
            videoData.put("id", video.getId());
            videoData.put("name", video.getTitle());
            videoData.put("description", video.getDescription());
            videoData.put("releaseTime", new Date(video.getCreatedTime() * 1000).toString());

            // Create user/author
            Map<String, Object> author = new HashMap<>();
            if (video.getOwner() != null) {
                author.put("id", video.getOwner().getId());
                author.put("name", video.getOwner().getUsername());
                author.put("user_link", video.getOwner().getUrl());
                author.put("picture_link", video.getOwner().getAvatarUrl());
            }
            videoData.put("user", author);

            // Add subtitles as captions
            List<Map<String, Object>> captions = new ArrayList<>();
            if (video.getSubtitles() != null) {
                for (DailymotionVideo.Subtitle subtitle : video.getSubtitles()) {
                    Map<String, Object> captionData = new HashMap<>();
                    captionData.put("id", UUID.randomUUID().toString());
                    captionData.put("link", subtitle.getUrl());
                    captionData.put("language", subtitle.getLanguage());
                    captions.add(captionData);
                }
            }
            videoData.put("captions", captions);

            // Add tags as comments
            List<Map<String, Object>> comments = new ArrayList<>();
            if (video.getTags() != null) {
                for (String tag : video.getTags()) {
                    Map<String, Object> comment = new HashMap<>();
                    comment.put("id", UUID.randomUUID().toString());
                    comment.put("text", "Tag: " + tag);
                    comment.put("createdOn", new Date().toString());
                    comments.add(comment);
                }
            }
            videoData.put("comments", comments);

            // IMPORTANTE: Añadimos el vídeo a la lista, NO hacemos el POST todavía
            videosList.add(videoData);
            storedVideos.add(video.getId());
        }

        // 4. Metemos la lista completa de vídeos dentro del canal
        channel.put("videos", videosList);

        // 5. UN ÚNICO POST para enviar toda la jerarquía de datos a VideoMiner
        restTemplate.postForObject(VIDEOMINER_API_BASE + "/channels", channel, Map.class);

        result.put("status", "success");
        result.put("message", "Data imported successfully from Dailymotion");
        result.put("videosImported", storedVideos.size());
        result.put("channelId", userId);

    } catch (Exception e) {
        result.put("status", "error");
        result.put("message", "Error: " + e.getMessage());
    }

    return result;
}
}
