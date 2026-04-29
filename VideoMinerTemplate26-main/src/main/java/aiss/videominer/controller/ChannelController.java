package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import aiss.videominer.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels() {
        List<Channel> channels = channelService.getAllChannels();
        return new ResponseEntity<>(channels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannelById(@PathVariable String id) {
        Optional<Channel> channel = channelService.getChannelById(id);
        return channel.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Channel> createChannel(@Valid @RequestBody Channel channel) {
        Channel createdChannel = channelService.createChannel(channel);
        return new ResponseEntity<>(createdChannel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable String id, @Valid @RequestBody Channel channelDetails) {
        Channel updatedChannel = channelService.updateChannel(id, channelDetails);
        if (updatedChannel != null) {
            return new ResponseEntity<>(updatedChannel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String id) {
        Optional<Channel> channel = channelService.getChannelById(id);
        if (channel.isPresent()) {
            channelService.deleteChannel(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
