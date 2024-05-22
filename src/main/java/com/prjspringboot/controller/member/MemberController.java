package com.prjspringboot.controller.member;

import com.prjspringboot.domain.member.Member;
import com.prjspringboot.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody Member member) {

        service.add(member);
        return null;
    }

    @GetMapping(value = "/check", params = "email")
    public ResponseEntity checkEmail(@RequestParam("email") String email) {

        System.out.println("email = " + email);
        Member member = service.getByEmail(email);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(email);
    }

    @GetMapping(value = "/check", params = "nickName")
    public void checkNickName(@RequestParam("nickName") String nickName) {
        System.out.println("nickName = " + nickName);
    }
}
