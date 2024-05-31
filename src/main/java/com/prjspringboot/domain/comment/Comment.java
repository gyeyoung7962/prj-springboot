package com.prjspringboot.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Comment {

    private Integer id;
    private Integer boardId;
    private Integer memberId;
    private String comment;

    private String writer;
    private LocalDateTime regDate;

    public String getRegDate() {
        LocalDateTime beforeOneDay = LocalDateTime.now().minusDays(1);

        if (regDate.isBefore(beforeOneDay)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return regDate.format(formatter).toString();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return regDate.format(formatter).toString();
        }


    }
}
