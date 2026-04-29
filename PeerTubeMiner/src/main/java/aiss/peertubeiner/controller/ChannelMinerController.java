package aiss.peertubeiner.controller;

import aiss.peertubeiner.model.PeerTubeVideoResponse;
import aiss.peertubeiner.service.PeerTubeService;
import aiss.peertubeiner.service.VideoMinerIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/channels")
public class ChannelMinerController {

    @Autowired
    private VideoMinerIntegrationService integrationService;

    @Autowired
    private PeerTubeService peerTubeService;

    // GET endpoint for testing (read-only) - returns PeerTube data without storing
    @GetMapping("/{id}")
    public ResponseEntity<?> getChannelDataReadOnly(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxComments) {
        
        try {
            PeerTubeVideoResponse response = peerTubeService.getVideosByChannel(id, maxVideos);
            if (response == null) {
                return new ResponseEntity<>("Failed to fetch data from PeerTube", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST endpoint - fetches from PeerTube and stores in VideoMiner
    @PostMapping("/{id}")
    public ResponseEntity<?> storeChannelData(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxComments) {
        
        try {
            Map<String, Object> result = integrationService.fetchAndStoreChannelData(id, maxVideos, maxComments);
            
            String status = (String) result.get("status");
            if ("success".equals(status)) {
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> error = new java.util.HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
