package com.prjspringboot.domain.board;

import lombok.Data;

@Data
public class Board {

    private String title;
    private String content;
    private String writer;
}
