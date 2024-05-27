package com.prjspringboot.mapper.board;

import com.prjspringboot.domain.board.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {


    @Insert("""
            insert into board(title, content, member_id)
            values (#{title}, #{content}, #{memberId})
            """)
    void add(Board board);

    @Select("""
            select b.id, b.title, m.nick_name as writer
            from board b join member m
            on b.member_id = m.id
            order by id desc
            """)
    List<Board> selectAll();

    @Select("""
            select b.title, b.content, b.regDate,m.nick_name as writer, b.member_id, b.id
            from board b join member m
            on b.member_id = m.id
            where b.id = #{id}
            """)
    Board selectById(Integer id);

    @Delete("""
            delete from board
            where id = #{id}
            """)
    int deleteById(Integer id);

    @Update("""
            update board
            set title = #{title}, content = #{content}
            where id = #{id}                   
            """)
    void update(Board board);


    @Delete("""
            delete from board
            where member_id = #{memberId}
            """)
    int deleteByMemberId(Integer memberId);


    @Select("""
            select b.id, b.title, m.nick_name as writer
            from board b join member m
            on b.member_id = m.id
            order by id desc
            limit #{offset}, 10
            """)
    List<Board> selectAllPaging(Integer offset);
}
