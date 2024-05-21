package com.prjspringboot.controller.board;

import com.prjspringboot.domain.board.Board;
import com.prjspringboot.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list")
    public List<Board> list() {
        return boardService.list();
    }

    @GetMapping("/{id}")
    public Board get(@PathVariable Integer id) {

        return boardService.get(id);
    }

}
