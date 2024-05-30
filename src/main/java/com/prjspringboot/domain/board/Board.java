package com.prjspringboot.domain.board;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Board {

    private Integer id;
    private String title;
    private String content;
    private String writer; //작성자 nickName
    private Integer memberId;
    private LocalDateTime regDate;

    private Integer numberOfLike;
    private Integer numberOfImages;
    private Integer numberOfComment;

    private List<BoardFile> fileList;


}
