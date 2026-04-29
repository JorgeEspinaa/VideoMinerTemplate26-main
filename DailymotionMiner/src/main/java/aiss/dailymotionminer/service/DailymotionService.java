package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.DailymotionVideo;
import aiss.dailymotionminer.model.DailymotionVideoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DailymotionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DAILYMOTION_API_BASE = "https://api.dailymotion.com";

    public DailymotionVideoResponse getVideosByUser(String userId, int maxVideos, int maxPages) {
        DailymotionVideoResponse combinedResponse = new DailymotionVideoResponse();
        List<DailymotionVideo> allVideos = new ArrayList<>();

        try {
            // Bucle para iterar sobre las páginas solicitadas 
            for (int page = 1; page <= maxPages; page++) {
                String url = DAILYMOTION_API_BASE + "/user/" + userId + "/videos" +
                        "?limit=" + maxVideos +
                        "&page=" + page + 
                        "&fields=id,title,description,created_time,owner,subtitles_data,tags";

                DailymotionVideoResponse pageResponse = restTemplate.getForObject(url, DailymotionVideoResponse.class);

                if (pageResponse != null && pageResponse.getVideos() != null && !pageResponse.getVideos().isEmpty()) {
                    allVideos.addAll(pageResponse.getVideos());
                    // Si recibimos menos del límite, no hay más contenido
                    if (pageResponse.getVideos().size() < maxVideos) break; 
                } else {
                    break; 
                }
            }
            
            combinedResponse.setVideos(allVideos);
            return combinedResponse;

        } catch (HttpClientErrorException.NotFound e) {
            return null; // El usuario no existe 
        } catch (Exception e) {
            return null;
        }
    }
}