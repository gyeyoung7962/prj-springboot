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
            select *
            from comment
            where board_id = #{boardId}
            order by id asc
            """)
    List<Comment> selectAllByBoardId(Integer boardId);
}
