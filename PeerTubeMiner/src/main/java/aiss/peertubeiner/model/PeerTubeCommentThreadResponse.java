package aiss.peertubeiner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerTubeCommentThreadResponse {
    
    @JsonProperty("data")
    private List<CommentThread> threads;

    public List<CommentThread> getThreads() {
        return threads;
    }

    public void setThreads(List<CommentThread> threads) {
        this.threads = threads;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommentThread {
        @JsonProperty("id")
        private Long id;
        
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("createdAt")
        private String createdAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
