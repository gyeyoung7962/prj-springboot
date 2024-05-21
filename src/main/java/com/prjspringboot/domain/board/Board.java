package com.prjspringboot.domain.board;

import lombok.Data;

@Data
public class Board {

    private Integer id;
    private String title;
    private String content;
    private String writer;
}
