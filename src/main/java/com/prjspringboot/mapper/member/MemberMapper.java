package com.prjspringboot.mapper.member;

import com.prjspringboot.domain.member.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
