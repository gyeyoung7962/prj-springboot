package com.prjspringboot.mapper.member;

import com.prjspringboot.domain.member.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    @Insert("""
            insert into member(email, password, nick_name)
            values(#{email}, #{password}, #{nickName})
            """)
    void insert(Member member);

    @Select("""
            select *
            from member
            where email = #{email}
            """)
    Member selectByEmail(String email);

    @Select("""
            select *
            from member
            where nick_name = #{nickName}
            """)
    Member selectByNickName(String nickName);

    @Select("""
            select *
            from member
            """)
    List<Member> memberList();

    @Select("""
            select id, password, email, nick_name, regDate
            from member
            where id = #{id}
            """)
    Member selectById(Integer id);


    @Delete("""
            delete 
            from member
            where id = #{id}
            """)
    int deleteById(Integer id);

    @Update("""
            update member
            set password = #{password}, nick_name = #{nickName}
            where id = #{id}
            """)
    int update(Member member);

    @Select("""
            select *
            from member
            where nick_name = #{nickName}
            """)
    Member getInfoByNick(String nickName);
}
