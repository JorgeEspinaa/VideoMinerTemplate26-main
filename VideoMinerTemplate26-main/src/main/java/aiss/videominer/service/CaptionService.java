package aiss.videominer.service;

import aiss.videominer.model.Caption;
import aiss.videominer.repository.CaptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaptionService {

    @Autowired
    private CaptionRepository captionRepository;

    public List<Caption> getAllCaptions() {
        return captionRepository.findAll();
    }

    public Optional<Caption> getCaptionById(String id) {
        return captionRepository.findById(id);
    }

    public Caption createCaption(Caption caption) {
        return captionRepository.save(caption);
    }

    public Caption updateCaption(String id, Caption captionDetails) {
        Optional<Caption> caption = captionRepository.findById(id);
        if (caption.isPresent()) {
            Caption existingCaption = caption.get();
            if (captionDetails.getLink() != null) {
                existingCaption.setLink(captionDetails.getLink());
            }
            if (captionDetails.getLanguage() != null) {
                existingCaption.setLanguage(captionDetails.getLanguage());
            }
            return captionRepository.save(existingCaption);
        }
        return null;
    }

    public void deleteCaption(String id) {
        captionRepository.deleteById(id);
    }
}
