package com.prjspringboot.service.comment;


import com.prjspringboot.domain.comment.Comment;
import com.prjspringboot.mapper.comment.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommentService {

    private final CommentMapper mapper;

    public void add(Comment comment, Authentication authentication) {

        comment.setMemberId(Integer.valueOf(authentication.getName()));

        mapper.insert(comment);
    }
}
