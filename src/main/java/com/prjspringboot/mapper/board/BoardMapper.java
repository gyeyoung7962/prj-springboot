package com.prjspringboot.mapper.board;

import com.prjspringboot.domain.board.Board;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {


    @Insert("""
            insert into board(title, content, writer)
            values (#{title}, #{content}, #{writer})
            """)
    void add(Board board);

    @Select("""
            select id, title, writer
            from board
            order by id desc
            """)
    List<Board> selectAll();

    @Select("""
            select *
            from board
            where id = #{id}
            """)
    Board selectById(Integer id);
}
