package aiss.peertubeiner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerTubeVideoResponse {
    
    @JsonProperty("data")
    private List<PeerTubeVideo> videos;

    public List<PeerTubeVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<PeerTubeVideo> videos) {
        this.videos = videos;
    }
}
