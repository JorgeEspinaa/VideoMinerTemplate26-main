package aiss.videominer.service;

import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Optional<Channel> getChannelById(String id) {
        return channelRepository.findById(id);
    }

    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public Channel updateChannel(String id, Channel channelDetails) {
        Optional<Channel> channel = channelRepository.findById(id);
        if (channel.isPresent()) {
            Channel existingChannel = channel.get();
            if (channelDetails.getName() != null) {
                existingChannel.setName(channelDetails.getName());
            }
            if (channelDetails.getDescription() != null) {
                existingChannel.setDescription(channelDetails.getDescription());
            }
            if (channelDetails.getCreatedTime() != null) {
                existingChannel.setCreatedTime(channelDetails.getCreatedTime());
            }
            if (channelDetails.getVideos() != null) {
                existingChannel.setVideos(channelDetails.getVideos());
            }
            return channelRepository.save(existingChannel);
        }
        return null;
    }

    public void deleteChannel(String id) {
        channelRepository.deleteById(id);
    }
}
