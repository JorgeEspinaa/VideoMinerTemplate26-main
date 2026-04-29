package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.service.CaptionService;
import aiss.videominer.service.CommentService;
import aiss.videominer.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private VideoService videoService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private CaptionService captionService;

    // Get all comments for a specific video
    @GetMapping("/videos/{videoId}/comments")
    public ResponseEntity<?> getCommentsByVideo(@PathVariable String videoId) {
        Optional<Video> video = videoService.getVideoById(videoId);
        if (!video.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(video.get().getComments(), HttpStatus.OK);
    }

    // Get all captions for a specific video
    @GetMapping("/videos/{videoId}/captions")
    public ResponseEntity<?> getCaptionsByVideo(@PathVariable String videoId) {
        Optional<Video> video = videoService.getVideoById(videoId);
        if (!video.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(video.get().getCaptions(), HttpStatus.OK);
    }
}
