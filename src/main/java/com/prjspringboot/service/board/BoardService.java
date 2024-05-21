package com.prjspringboot.service.board;

import com.prjspringboot.domain.board.Board;
import com.prjspringboot.mapper.board.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper mapper;


    public void add(Board board) {

        mapper.add(board);
    }
}
