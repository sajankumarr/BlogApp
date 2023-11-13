package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.CommentDto;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;

    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id",postId)
        );
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        CommentDto dto = mapToDTO(newComment);
        return dto;
    }

    @Override
    public List<CommentDto> getCommentsBYPostId(long postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id", postId)
        );
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment->mapToDTO(comment)).collect(Collectors.toList());


    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id", postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Post","id", postId)
        );
        return mapToDTO(comment);

    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id", postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Post","id", commentId)
        );

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updateComment = commentRepository.save(comment);
        return mapToDTO(updateComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id", postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Post","id",commentId )
        );
        commentRepository.deleteById(commentId);

    }

    private CommentDto mapToDTO(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;

    }
}