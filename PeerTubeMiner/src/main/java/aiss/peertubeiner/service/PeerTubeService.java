package aiss.peertubeiner.service;

import aiss.peertubeiner.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PeerTubeService {

    private final RestTemplate restTemplate = new RestTemplate();
    // Usamos framatube que es una de las instancias más grandes de PeerTube
    private static final String PEERTUBE_API_BASE = "https://framatube.org/api/v1";

    public PeerTubeVideoResponse getVideosByChannel(String channelHandle, int maxVideos) {
        String url = PEERTUBE_API_BASE + "/video-channels/" + channelHandle + "/videos" +
                "?count=" + maxVideos;
        
        try {
            return restTemplate.getForObject(url, PeerTubeVideoResponse.class);
        } catch (Exception e) {
            System.err.println("Error fetching videos from PeerTube: " + e.getMessage());
            return null;
        }
    }

    public PeerTubeCommentThreadResponse getCommentsByVideo(String videoUuid, int maxComments) {
        String url = PEERTUBE_API_BASE + "/videos/" + videoUuid + "/comment-threads" +
                "?count=" + maxComments;
        
        try {
            return restTemplate.getForObject(url, PeerTubeCommentThreadResponse.class);
        } catch (Exception e) {
            System.err.println("Error fetching comments from PeerTube: " + e.getMessage());
            return null;
        }
    }

    // NUEVO MÉTODO: Para extraer los subtítulos haciendo una llamada extra a la API
    public PeerTubeVideo.CaptionResponse getCaptionsByVideo(String videoUuid) {
        String url = PEERTUBE_API_BASE + "/videos/" + videoUuid + "/captions";
        
        try {
            return restTemplate.getForObject(url, PeerTubeVideo.CaptionResponse.class);
        } catch (Exception e) {
            System.err.println("Error fetching captions from PeerTube: " + e.getMessage());
            return null;
        }
    }
}
