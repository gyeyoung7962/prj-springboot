package com.prjspringboot.service.member;

import com.prjspringboot.domain.member.Member;
import com.prjspringboot.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;


    public void add(Member member) {

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setEmail(member.getEmail().trim());
        member.setNickName(member.getNickName().trim());

        mapper.insert(member);
    }

    public Member getByEmail(String email) {

        return mapper.selectByEmail(email.trim());

    }

    public Member getByNickName(String nickName) {

        return mapper.selectByNickName(nickName.trim());
    }

    public boolean validate(Member member) {

        if (member.getEmail() == null || member.getEmail().isBlank()) {
            return false;
        }
        if (member.getNickName() == null || member.getNickName().isBlank()) {
            return false;
        }
        if (member.getPassword() == null || member.getPassword().isBlank()) {
            return false;
        }
        String emailPattern = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*";

        if (!member.getEmail().trim().matches(emailPattern)) {
            return false;
        }
        return true;
    }

    public List<Member> memberList() {

        return mapper.memberList();
    }

    public Member getById(Integer id) {

        return mapper.selectById(id);
    }

    public void remove(Integer id) {
        mapper.deleteById(id);
    }

    public boolean hasAccess(Member member) {

        Member dbMember = mapper.selectById(member.getId());

        if (dbMember == null) {
            return false;
        }
        //db비밀번호는 encode
        //member로 받아온 비밀번호 decode
        return passwordEncoder.matches(member.getPassword(), dbMember.getPassword());
    }
}