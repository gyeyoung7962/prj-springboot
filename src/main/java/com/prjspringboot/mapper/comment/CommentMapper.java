package com.prjspringboot.mapper.comment;

import com.prjspringboot.domain.comment.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    @Insert("""
            insert into comment(board_id, member_id, comment)
            values (#{boardId}, #{memberId}, #{comment})
            """)
    int insert(Comment comment);

}
