package com.prjspringboot.controller.comment;

import com.prjspringboot.domain.comment.Comment;
import com.prjspringboot.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService service;

    @PostMapping("/add")
    public void addComment(@RequestBody Comment comment, Authentication authentication) {

        service.add(comment, authentication);

    }

    @GetMapping("/list/{boardId}")
    public List<Comment> list(@PathVariable Integer boardId) {
        return service.list(boardId);
    }
}
