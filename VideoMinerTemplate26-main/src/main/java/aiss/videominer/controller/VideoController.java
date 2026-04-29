package aiss.videominer.controller;

import aiss.videominer.model.Video;
import aiss.videominer.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        Optional<Video> video = videoService.getVideoById(id);
        return video.map(v -> new ResponseEntity<>(v, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) {
        Video createdVideo = videoService.createVideo(video);
        return new ResponseEntity<>(createdVideo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable String id, @Valid @RequestBody Video videoDetails) {
        Video updatedVideo = videoService.updateVideo(id, videoDetails);
        if (updatedVideo != null) {
            return new ResponseEntity<>(updatedVideo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        Optional<Video> video = videoService.getVideoById(id);
        if (video.isPresent()) {
            videoService.deleteVideo(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
