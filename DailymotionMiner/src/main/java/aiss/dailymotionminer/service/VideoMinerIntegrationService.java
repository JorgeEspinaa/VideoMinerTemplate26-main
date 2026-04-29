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

            // 2. Create channel in VideoMiner
            String channelId = userId;
            Map<String, Object> channel = new HashMap<>();
            channel.put("id", channelId);
            channel.put("name", userId);
            channel.put("description", "Channel from Dailymotion: " + userId);
            channel.put("createdTime", new Date().toString());
            channel.put("videos", new ArrayList<>());

            restTemplate.postForObject(VIDEOMINER_API_BASE + "/channels", channel, Map.class);

            // 3. For each video, create in VideoMiner
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

                // Add tags as comments (since Dailymotion doesn't have comments in API)
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

                // Store video in VideoMiner
                restTemplate.postForObject(VIDEOMINER_API_BASE + "/videos", videoData, Map.class);
                storedVideos.add(video.getId());
            }

            result.put("status", "success");
            result.put("message", "Data imported successfully from Dailymotion");
            result.put("videosImported", storedVideos.size());
            result.put("channelId", channelId);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Error: " + e.getMessage());
        }

        return result;
    }
}
