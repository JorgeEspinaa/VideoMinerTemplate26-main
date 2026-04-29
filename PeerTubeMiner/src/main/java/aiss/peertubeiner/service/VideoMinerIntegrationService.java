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
            
            // CAMBIO CLAVE: Gestionar el 404 para el controlador
            if (videoResponse == null || videoResponse.getVideos() == null || videoResponse.getVideos().isEmpty()) {
                result.put("status", "not_found");
                result.put("message", "Channel not found or has no videos on PeerTube");
                return result;
            }

            // 2. Create the FULL channel object
            String channelId = channelHandle;
            Map<String, Object> channel = new HashMap<>();
            channel.put("id", channelId);
            channel.put("name", channelHandle);
            channel.put("description", "Channel from PeerTube: " + channelHandle);
            channel.put("createdTime", new Date().toString());
            
            // 3. Prepare the list of videos to embed inside the channel
            List<Map<String, Object>> videosList = new ArrayList<>();
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
               // Fetch and add captions
                List<Map<String, Object>> captions = new ArrayList<>();
                // CAMBIO: Ahora llamamos a la API para sacar los subtítulos en vez de intentar leerlos del video
                PeerTubeVideo.CaptionResponse captionResponse = peerTubeService.getCaptionsByVideo(video.getUuid());
                
                if (captionResponse != null && captionResponse.getData() != null) {
                    for (PeerTubeVideo.Caption caption : captionResponse.getData()) {
                        Map<String, Object> captionData = new HashMap<>();
                        captionData.put("id", UUID.randomUUID().toString()); // Generamos un ID inventado como nos pedía el modelo
                        
                        // En PeerTube a veces la URL viene relativa, así que le añadimos el dominio por si acaso
                        String fileUrl = caption.getFileUrl();
                        if (fileUrl != null && fileUrl.startsWith("/")) {
                            fileUrl = "https://framatube.org" + fileUrl;
                        }
                        captionData.put("link", fileUrl);
                        
                        if (caption.getLanguage() != null) {
                            captionData.put("language", caption.getLanguage().getId());
                        }
                        captions.add(captionData);
                    }
                }
                videoData.put("captions", captions);

                // CAMBIO CLAVE: En lugar de hacer un POST por cada video, lo añadimos a la lista del canal
                videosList.add(videoData);
                storedVideos.add(video.getUuid());
            }

            // Añadir la lista de videos completos al canal
            channel.put("videos", videosList);

            // 4. Send the FULL channel object to VideoMiner in ONE request
            restTemplate.postForObject(VIDEOMINER_API_BASE + "/channels", channel, Map.class);

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