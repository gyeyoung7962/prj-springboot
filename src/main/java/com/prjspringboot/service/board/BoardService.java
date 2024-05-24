package com.prjspringboot.service.board;

import com.prjspringboot.domain.board.Board;
import com.prjspringboot.mapper.board.BoardMapper;
import com.prjspringboot.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper mapper;
    private final MemberMapper memberMapper;


    public void add(Board board, Authentication authentication) {

        board.setMemberId(Integer.valueOf(authentication.getName()));
        mapper.add(board);
    }

    public boolean validate(Board board) {

        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }
        return true;
    }

    public List<Board> list() {
        return mapper.selectAll();
    }

    public Board get(Integer id) {

        return mapper.selectById(id);
    }

    public void remove(Integer id) {

        mapper.deleteById(id);
    }

    public void update(Board board) {

        mapper.update(board);
    }
}
