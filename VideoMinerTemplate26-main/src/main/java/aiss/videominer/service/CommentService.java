package aiss.videominer.service;

import aiss.videominer.model.Comment;
import aiss.videominer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(String id, Comment commentDetails) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            Comment existingComment = comment.get();
            if (commentDetails.getText() != null) {
                existingComment.setText(commentDetails.getText());
            }
            if (commentDetails.getCreatedOn() != null) {
                existingComment.setCreatedOn(commentDetails.getCreatedOn());
            }
            return commentRepository.save(existingComment);
        }
        return null;
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
