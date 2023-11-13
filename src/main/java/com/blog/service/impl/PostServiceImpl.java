package com.blog.service.impl;

import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.PostDto;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.blog.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
 

    private PostRepository postRepository;
    private ModelMapper modelMapper;
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setDescription(postDto.getDescription());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        Post newPost = postRepository.save(post);

        PostDto dto = new PostDto();
        dto.setId(newPost.getId());
        dto.setTitle(newPost.getTitle());
        dto.setContent(newPost.getContent());
        dto.setDescription(newPost.getDescription());

        return dto;
    }

    @Override
    public List<PostDto> lisAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?  Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> listOfPosts = postRepository.findAll(pageable);
        List<Post> posts = listOfPosts.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public PostDto getPostById(long id) {
       Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id", id));
    return mapToDto(post);
    }

    @Override
    public PostDto updatePost(long id,  PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post","id", id)
        );
        Post newPost = mapToEntity(postDto);
        newPost.setId(id);

        Post updatedPost = postRepository.save(newPost);

        PostDto dto = mapToDto(updatedPost);
        return dto;
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post","id", id)
        );
        postRepository.deleteById(id);
    }


    PostDto mapToDto(Post post) {
        PostDto dto = modelMapper.map(post, PostDto.class);
        //PostDto dto = new PostDto();
        //dto.setId(post.getId());
        //dto.setTitle(post.getTitle());
        //dto.setContent(post.getContent());
       // dto.setDescription(post.getDescription());
        return dto;
    }

   Post mapToEntity(PostDto postDto) {
     Post post = modelMapper.map(postDto, Post.class);
        //Post post = new Post();
        //post.setId(postDto.getId());
        //post.setTitle(postDto.getTitle());
        //post.setContent(postDto.getContent());
        //post.setDescription(postDto.getDescription());
        return post;
    }
}
