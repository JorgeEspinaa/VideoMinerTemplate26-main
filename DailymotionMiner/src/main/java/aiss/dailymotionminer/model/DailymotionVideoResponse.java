package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailymotionVideoResponse {
    
    @JsonProperty("list")
    private List<DailymotionVideo> videos;
    
    @JsonProperty("has_more")
    private Boolean hasMore;
    
    @JsonProperty("page")
    private Integer page;

    public List<DailymotionVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<DailymotionVideo> videos) {
        this.videos = videos;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
