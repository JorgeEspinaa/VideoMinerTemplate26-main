package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.DailymotionVideoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DailymotionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DAILYMOTION_API_BASE = "https://api.dailymotion.com";

    public DailymotionVideoResponse getVideosByUser(String userId, int maxVideos, int maxPages) {
        String url = DAILYMOTION_API_BASE + "/user/" + userId + "/videos" +
                "?limit=" + maxVideos + 
                "&fields=id,title,description,created_time,owner,subtitles_data,tags";
        
        try {
            return restTemplate.getForObject(url, DailymotionVideoResponse.class);
        } catch (Exception e) {
            System.err.println("Error fetching videos from Dailymotion: " + e.getMessage());
            return null;
        }
    }
}
