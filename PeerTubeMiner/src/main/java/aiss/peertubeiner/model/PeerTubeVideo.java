package aiss.peertubeiner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerTubeVideo {
    
    @JsonProperty("uuid")
    private String uuid;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("publishedAt")
    private String publishedAt;
    
    @JsonProperty("account")
    private PeerTubeAccount account;
    
    @JsonProperty("captions")
    private CaptionResponse captions;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public PeerTubeAccount getAccount() {
        return account;
    }

    public void setAccount(PeerTubeAccount account) {
        this.account = account;
    }

    public CaptionResponse getCaptions() {
        return captions;
    }

    public void setCaptions(CaptionResponse captions) {
        this.captions = captions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CaptionResponse {
        @JsonProperty("data")
        private List<Caption> data;

        public List<Caption> getData() {
            return data;
        }

        public void setData(List<Caption> data) {
            this.data = data;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Caption {
        @JsonProperty("fileUrl")
        private String fileUrl;
        
        @JsonProperty("language")
        private Language language;

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public Language getLanguage() {
            return language;
        }

        public void setLanguage(Language language) {
            this.language = language;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Language {
        @JsonProperty("id")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
