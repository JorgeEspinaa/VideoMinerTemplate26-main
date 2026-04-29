package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.service.CaptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/captions")
public class CaptionController {

    @Autowired
    private CaptionService captionService;

    @GetMapping
    public ResponseEntity<List<Caption>> getAllCaptions() {
        List<Caption> captions = captionService.getAllCaptions();
        return new ResponseEntity<>(captions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caption> getCaptionById(@PathVariable String id) {
        Optional<Caption> caption = captionService.getCaptionById(id);
        return caption.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Caption> createCaption(@Valid @RequestBody Caption caption) {
        Caption createdCaption = captionService.createCaption(caption);
        return new ResponseEntity<>(createdCaption, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Caption> updateCaption(@PathVariable String id, @Valid @RequestBody Caption captionDetails) {
        Caption updatedCaption = captionService.updateCaption(id, captionDetails);
        if (updatedCaption != null) {
            return new ResponseEntity<>(updatedCaption, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaption(@PathVariable String id) {
        Optional<Caption> caption = captionService.getCaptionById(id);
        if (caption.isPresent()) {
            captionService.deleteCaption(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
