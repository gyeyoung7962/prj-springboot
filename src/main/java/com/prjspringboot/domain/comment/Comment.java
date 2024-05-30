package com.prjspringboot.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {

    private Integer id;
    private Integer boardId;
    private Integer memberId;
    private String comment;

    private String writer;
    private LocalDateTime regDate;
}