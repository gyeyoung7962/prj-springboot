package com.prjspringboot.service.comment;


import com.prjspringboot.domain.comment.Comment;
import com.prjspringboot.mapper.comment.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommentService {

    private final CommentMapper mapper;

    public void add(Comment comment, Authentication authentication) {

        comment.setMemberId(Integer.valueOf(authentication.getName()));

        mapper.insert(comment);
    }

    public List<Comment> list(Integer boardId) {

        return mapper.selectAllByBoardId(boardId);
    }

    public boolean validate(Comment comment) {

        if (comment == null) {
            return false;
        }
        if (comment.getComment().isBlank()) {
            return false;
        }
        if (comment.getBoardId() == null) {
            return false;
        }
        return true;
    }

    public void remove(Comment comment) {

        mapper.delete(comment.getId());
    }

    public boolean hasAccess(Comment comment, Authentication authentication) {

        Comment db = mapper.selectById(comment.getId());
        if (db == null) {
            return false;
        }
        if (!authentication.getName().equals(db.getMemberId().toString())) {
            return false;
        }
        return true;
    }
}
