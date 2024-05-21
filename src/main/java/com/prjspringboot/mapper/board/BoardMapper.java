package com.prjspringboot.mapper.board;

import com.prjspringboot.domain.board.Board;
import org.apache.ibatis.annotations.*;

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

    @Delete("""
            delete from board
            where id = #{id}
            """)
    int deleteById(Integer id);

    @Update("""
            update board
            set title = #{title}, content = #{content}, writer = #{writer}
            where id = #{id}                   
            """)
    void update(Board board);
}
