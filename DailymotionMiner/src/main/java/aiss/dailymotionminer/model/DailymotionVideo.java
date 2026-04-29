package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailymotionVideo {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("created_time")
    private Long createdTime;
    
    @JsonProperty("owner")
    private DailymotionOwner owner;
    
    @JsonProperty("subtitles_data")
    private List<Subtitle> subtitles;
    
    @JsonProperty("tags")
    private List<String> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public DailymotionOwner getOwner() {
        return owner;
    }

    public void setOwner(DailymotionOwner owner) {
        this.owner = owner;
    }

    public List<Subtitle> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Subtitle> subtitles) {
        this.subtitles = subtitles;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Subtitle {
        @JsonProperty("url")
        private String url;
        
        @JsonProperty("language")
        private String language;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}
