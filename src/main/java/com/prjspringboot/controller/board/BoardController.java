package com.prjspringboot.controller.board;

import com.prjspringboot.domain.board.Board;
import com.prjspringboot.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(
            Authentication authentication,
            Board board,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files) throws IOException {

        if (files != null) {
            System.out.println("files = " + files.length);


            for (MultipartFile file : files) {
                System.out.println("file.name = " + file.getOriginalFilename());
            }

        }

        if (boardService.validate(board)) {
            boardService.add(board, authentication, files);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(value = "type", required = false) String searchType,
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        System.out.println("page = " + page);
        return boardService.list(page, searchType, keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        Board board = boardService.get(id);

        if (board == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(board);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity delete(@PathVariable Integer id, Authentication authentication) {

        if (boardService.hasAccess(id, authentication)) {
            boardService.remove(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/edit")
    public ResponseEntity update(Board board, Authentication authentication
            , @RequestParam(value = "removeFileList[]", required = false) List<String> removeFileList,
                                 @RequestParam(value = "addFileList[]", required = false) MultipartFile[] addFileList) throws Exception {


        if (!boardService.hasAccess(board.getId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (boardService.validate(board)) {

            boardService.edit(board, removeFileList, addFileList);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }

    }


}
