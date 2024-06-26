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
    @Options(useGeneratedKeys = true, keyProperty = "id")
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
            <script>
            select b.id, 
                    b.title, 
                    m.nick_name as writer,
                    count(distinct f.name) as number_of_images,
                    count(distinct l.member_id) number_of_like,
                    count(distinct c.id) number_of_comment
            from board b join member m on b.member_id = m.id
            left join board_file f on b.id = f.board_id
            left join board_like l on b.id = l.board_id
            left join comment c on b.id = c.board_id
            <trim prefix="where" prefixOverrides="OR">
            <if test="searchType != null">
            <bind name="pattern" value="'%'+keyword+'%'"/>
            <if test="searchType == 'all' || searchType == 'text'">
            OR b.title like #{pattern}
            OR b.content like #{pattern}
            </if>
            <if test="searchType == 'all' || searchType == 'nickName'">
            OR m.nick_name like #{pattern}
            </if>
            </if>
            </trim>
            group by b.id 
            order by id desc
            limit #{offset}, 10
            </script>
            """)
    List<Board> selectAllPaging(Integer offset, String searchType, String keyword);


    @Select("""
            select count(*) from board
            """)
    Integer countAll();

    @Select("""
            <script>
            select count(b.id)
            from board b join member m on b.member_id = m.id
            <trim prefix="where" prefixOverrides="OR">
            <if test="searchType != null">
            <bind name="pattern" value="'%'+keyword+'%'"/>
            <if test="searchType == 'all' || searchType == 'text'">
            OR b.title like #{pattern}
            OR b.content like #{pattern}
            </if>
            <if test="searchType == 'all' || searchType == 'nickName'">
            OR m.nick_name like #{pattern}
            </if>
            </if>
            </trim>
            </script>
            """)
    Integer countAllWithSearch(String searchType, String keyword);

    @Insert("""
            insert into board_file(board_id, name)
            values (#{boardId}, #{name})
            """)
    int insertFileName(Integer boardId, String name);

    @Select("""
            select name
            from board_file
            where board_id = #{boardId}
            """)
    List<String> selectFileNameByBoardId(Integer boardId);

    @Delete("""
            delete
            from board_file where board_id = #{boardId} 
            """)
    int deleteFileByBoardId(Integer boardId);


    @Select("""
            select id from board where member_id = #{memberId}
            """)
    List<Board> selectByMemberId(Integer memberId);

    @Delete("""
                delete from board_file
                where board_id = #{boardId}
                and name=#{fileName}
            """)
    int deleteFileByBoardIdAndName(Integer boardId, String fileName);

    @Delete("""
            delete from board_like where board_id = #{boardId} and member_id = #{memberId}
            """)
    int deleteLikeByBoardIdAndMemberId(Integer boardId, Integer memberId);

    @Insert("""
            insert into board_like(board_id, member_id)
            values (#{boardId}, #{memberId})
            """)
    int insertLikeByBoardIdAndMemberId(Integer boardId, Integer memberId);

    @Select("""
            select count(*)
            from board_like where board_id = #{boardId}
            """)
    int selectCountLikeByBoardId(Integer boardId);


    @Select("""
            select count(*) from board_like where board_id = #{boardId} and member_id = #{memberId}
            """)
    int selectLikeByBoardIdAndMemberId(Integer boardId, String memberId);

    @Delete("""
            delete from board_like
            where board_id = #{boardId}
            """)
    int deleteLikeBoardId(Integer boardId);

    @Delete("""
            delete from board_like where member_id = #{memberId}
            """)
    int deleteLikeByMember(Integer memberId);

}
