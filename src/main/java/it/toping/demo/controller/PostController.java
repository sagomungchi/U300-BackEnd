package it.toping.demo.controller;

import it.toping.demo.model.Post;
import it.toping.demo.repository.PostRepository;
import it.toping.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/create-post", method = RequestMethod.POST)
    public @ResponseBody
    Post createAccount(@RequestBody Post input){
        return postService.postSaveAndUpdate(input);
    }

    @RequestMapping(value="/post/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Optional<Post> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @RequestMapping(value="/posts", method = RequestMethod.GET)
    public @ResponseBody
    List<Post> getPosts() {
        return postService.getPosts();
    }

}
