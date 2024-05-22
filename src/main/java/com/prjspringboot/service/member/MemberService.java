package com.prjspringboot.service.member;

import com.prjspringboot.domain.member.Member;
import com.prjspringboot.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberService {

    private MemberMapper mapper;


    public void add(Member member) {
        mapper.insert(member);
    }
}
