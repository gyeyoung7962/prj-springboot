package com.prjspringboot.domain.member;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Member {

    private Integer id;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime regDate;

    public String getSignupDateAndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");

        return regDate.format(formatter);
    }
}
