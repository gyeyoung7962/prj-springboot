package com.prjspringboot.mapper.comment;

import com.prjspringboot.domain.comment.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
            insert into comment(board_id, member_id, comment)
            values (#{boardId}, #{memberId}, #{comment})
            """)
    int insert(Comment comment);

    @Select("""
            select m.nick_name as writer, c.comment, c.regDate , c.id
            from comment c
                     join member m
                          on c.member_id = m.id
                     join board b
                          on b.id = c.board_id
            where b.id = #{boardId};
            """)
    List<Comment> selectAllByBoardId(Integer boardId);
}
