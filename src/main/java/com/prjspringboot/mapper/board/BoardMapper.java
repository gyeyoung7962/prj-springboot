package com.prjspringboot.mapper.board;

import com.prjspringboot.domain.board.Board;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {


    @Insert("""
            insert into board(title, content, writer)
            values (#{title}, #{content}, #{writer})
            """)
    void add(Board board);
}
