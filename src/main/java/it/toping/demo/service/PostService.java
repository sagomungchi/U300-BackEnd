package it.toping.demo.service;

import it.toping.demo.model.Post;
import it.toping.demo.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post postSaveAndUpdate(Post input){
        return postRepository.save(input);
    }

    public Optional<Post> getPost(Long id){
        return postRepository.findById(id);
    }

    public List<Post> getPosts(){
        return postRepository.findAll();
    }
}