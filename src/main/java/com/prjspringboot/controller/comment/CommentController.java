package com.prjspringboot.controller.comment;

import com.prjspringboot.domain.comment.Comment;
import com.prjspringboot.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService service;

    @PostMapping("/add")
    public void addComment(@RequestBody Comment comment, Authentication authentication) {

        service.add(comment, authentication);

    }
}
