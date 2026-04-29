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

    // GET endpoint para pruebas (solo lectura)
    @GetMapping("/{id}")
    public ResponseEntity<?> getChannelDataReadOnly(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxComments) {
        
        try {
            PeerTubeVideoResponse response = peerTubeService.getVideosByChannel(id, maxVideos);
            // Si el servicio devuelve null o una lista vacía, mandamos el 404 que pide el PDF
            if (response == null || response.getVideos() == null || response.getVideos().isEmpty()) {
                return new ResponseEntity<>("Channel not found on PeerTube", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST endpoint - Extrae de PeerTube y guarda en VideoMiner
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
            } else if ("not_found".equals(status)) {
                // IMPORTANTE: Aquí es donde capturamos el error de "no existe" y devolvemos 404
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
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