package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.DailymotionVideoResponse;
import aiss.dailymotionminer.service.DailymotionService;
import aiss.dailymotionminer.service.VideoMinerIntegrationService;
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
    private DailymotionService dailymotionService;

    // GET endpoint for testing (read-only) - returns Dailymotion data without storing
    @GetMapping("/{id}")
    public ResponseEntity<?> getChannelDataReadOnly(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxPages) {
        
        try {
            DailymotionVideoResponse response = dailymotionService.getVideosByUser(id, maxVideos, maxPages);
            if (response == null) {
                return new ResponseEntity<>("Failed to fetch data from Dailymotion", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST endpoint - fetches from Dailymotion and stores in VideoMiner
    @PostMapping("/{id}")
    public ResponseEntity<?> storeChannelData(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxPages) {
        
        try {
            Map<String, Object> result = integrationService.fetchAndStoreChannelData(id, maxVideos, maxPages);
            
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
