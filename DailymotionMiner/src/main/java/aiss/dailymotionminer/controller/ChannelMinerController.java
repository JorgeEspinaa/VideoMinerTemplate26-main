package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.DailymotionVideoResponse;
import aiss.dailymotionminer.service.DailymotionService;
import aiss.dailymotionminer.service.VideoMinerIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channels")
public class ChannelMinerController {

    @Autowired
    private VideoMinerIntegrationService integrationService;

    @Autowired
    private DailymotionService dailymotionService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getChannelDataReadOnly(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxPages) {
        
        DailymotionVideoResponse response = dailymotionService.getVideosByUser(id, maxVideos, maxPages);
        if (response == null) {
            return new ResponseEntity<>("Channel not found", HttpStatus.NOT_FOUND); // 
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> storeChannelData(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxPages) {
        
        var result = integrationService.fetchAndStoreChannelData(id, maxVideos, maxPages);
        if ("error".equals(result.get("status"))) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); // 
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}