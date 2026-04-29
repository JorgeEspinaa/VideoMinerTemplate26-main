package aiss.peertubeiner.service;

import aiss.peertubeiner.model.PeerTubeVideo;
import aiss.peertubeiner.model.PeerTubeVideoResponse;
import aiss.peertubeiner.model.PeerTubeCommentThreadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class VideoMinerIntegrationService {

    @Autowired
    private PeerTubeService peerTubeService;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String VIDEOMINER_API_BASE = "http://localhost:8080/api";

    public Map<String, Object> fetchAndStoreChannelData(String channelHandle, int maxVideos, int maxComments) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. Fetch videos from PeerTube
            PeerTubeVideoResponse videoResponse = peerTubeService.getVideosByChannel(channelHandle, maxVideos);
            if (videoResponse == null || videoResponse.getVideos() == null) {
                result.put("status", "error");
                result.put("message", "Failed to fetch videos from PeerTube");
                return result;
            }

            // 2. Create channel in VideoMiner
            String channelId = channelHandle;
            Map<String, Object> channel = new HashMap<>();
            channel.put("id", channelId);
            channel.put("name", channelHandle);
            channel.put("description", "Channel from PeerTube: " + channelHandle);
            channel.put("createdTime", new Date().toString());
            channel.put("videos", new ArrayList<>());

            restTemplate.postForObject(VIDEOMINER_API_BASE + "/channels", channel, Map.class);

            // 3. For each video, fetch comments and create in VideoMiner
            List<String> storedVideos = new ArrayList<>();
            
            for (PeerTubeVideo video : videoResponse.getVideos()) {
                Map<String, Object> videoData = new HashMap<>();
                videoData.put("id", video.getUuid());
                videoData.put("name", video.getName());
                videoData.put("description", video.getDescription());
                videoData.put("releaseTime", video.getPublishedAt());

                // Create user/author
                Map<String, Object> author = new HashMap<>();
                if (video.getAccount() != null) {
                    author.put("id", video.getAccount().getId().toString());
                    author.put("name", video.getAccount().getName());
                    author.put("user_link", video.getAccount().getUrl());
                    if (video.getAccount().getAvatar() != null) {
                        author.put("picture_link", video.getAccount().getAvatar().getUrl());
                    }
                }
                videoData.put("user", author);

                // Fetch and add comments
                List<Map<String, Object>> comments = new ArrayList<>();
                PeerTubeCommentThreadResponse commentResponse = peerTubeService.getCommentsByVideo(video.getUuid(), maxComments);
                if (commentResponse != null && commentResponse.getThreads() != null) {
                    for (PeerTubeCommentThreadResponse.CommentThread thread : commentResponse.getThreads()) {
                        Map<String, Object> comment = new HashMap<>();
                        comment.put("id", thread.getId().toString());
                        comment.put("text", thread.getText());
                        comment.put("createdOn", thread.getCreatedAt());
                        comments.add(comment);
                    }
                }
                videoData.put("comments", comments);

                // Add captions
                List<Map<String, Object>> captions = new ArrayList<>();
                if (video.getCaptions() != null && video.getCaptions().getData() != null) {
                    for (PeerTubeVideo.Caption caption : video.getCaptions().getData()) {
                        Map<String, Object> captionData = new HashMap<>();
                        captionData.put("id", UUID.randomUUID().toString());
                        captionData.put("link", caption.getFileUrl());
                        if (caption.getLanguage() != null) {
                            captionData.put("language", caption.getLanguage().getId());
                        }
                        captions.add(captionData);
                    }
                }
                videoData.put("captions", captions);

                // Store video in VideoMiner
                restTemplate.postForObject(VIDEOMINER_API_BASE + "/videos", videoData, Map.class);
                storedVideos.add(video.getUuid());
            }

            result.put("status", "success");
            result.put("message", "Data imported successfully from PeerTube");
            result.put("videosImported", storedVideos.size());
            result.put("channelId", channelId);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Error: " + e.getMessage());
        }

        return result;
    }
}
