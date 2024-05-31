package com.prjspringboot.mapper.comment;

import com.prjspringboot.domain.comment.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
            insert into comment(board_id, member_id, comment)
            values (#{boardId}, #{memberId}, #{comment})
            """)
    int insert(Comment comment);

    @Select("""
            select m.nick_name as writer, c.comment, c.regDate , c.id, c.member_id
            from comment c
                     join member m
                          on c.member_id = m.id
                     join board b
                          on b.id = c.board_id
            where b.id = #{boardId};
            """)
    List<Comment> selectAllByBoardId(Integer boardId);

    @Delete("""
            delete from
            comment
            where id = #{id}
            """)
    int delete(Integer id);

    @Delete("""
            delete from
            comment
            where member_id = #{memberId}
            """)
    int deleteCommentByMember(Integer memberId);

    @Select("""
            select *
            from comment
            where id = #{id}
            """)
    Comment selectById(Integer id);

    @Delete("""
            delete from comment
            where board_id = #{boardId}
            """)
    int deleteByBoardId(Integer boardId);


    @Update("""
            update comment
            set comment = #{comment}
            where id = #{id}
            """)
    int editComment(Comment comment);
}
