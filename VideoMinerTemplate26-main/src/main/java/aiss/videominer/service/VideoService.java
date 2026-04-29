package aiss.videominer.service;

import aiss.videominer.model.Video;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Optional<Video> getVideoById(String id) {
        return videoRepository.findById(id);
    }

    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    public Video updateVideo(String id, Video videoDetails) {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isPresent()) {
            Video existingVideo = video.get();
            if (videoDetails.getName() != null) {
                existingVideo.setName(videoDetails.getName());
            }
            if (videoDetails.getDescription() != null) {
                existingVideo.setDescription(videoDetails.getDescription());
            }
            if (videoDetails.getReleaseTime() != null) {
                existingVideo.setReleaseTime(videoDetails.getReleaseTime());
            }
            if (videoDetails.getAuthor() != null) {
                existingVideo.setAuthor(videoDetails.getAuthor());
            }
            if (videoDetails.getComments() != null) {
                existingVideo.setComments(videoDetails.getComments());
            }
            if (videoDetails.getCaptions() != null) {
                existingVideo.setCaptions(videoDetails.getCaptions());
            }
            return videoRepository.save(existingVideo);
        }
        return null;
    }

    public void deleteVideo(String id) {
        videoRepository.deleteById(id);
    }
}
