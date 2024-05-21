package com.prjspringboot.controller.board;

import com.prjspringboot.domain.board.Board;
import com.prjspringboot.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Board board) {

        if (boardService.validate(board)) {
            boardService.add(board);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

}
