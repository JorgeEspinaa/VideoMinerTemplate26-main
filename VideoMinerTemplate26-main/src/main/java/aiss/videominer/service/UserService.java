package aiss.videominer.service;

import aiss.videominer.model.User;
import aiss.videominer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            if (userDetails.getName() != null) {
                existingUser.setName(userDetails.getName());
            }
            if (userDetails.getUser_link() != null) {
                existingUser.setUser_link(userDetails.getUser_link());
            }
            if (userDetails.getPicture_link() != null) {
                existingUser.setPicture_link(userDetails.getPicture_link());
            }
            return userRepository.save(existingUser);
        }
        return null;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
