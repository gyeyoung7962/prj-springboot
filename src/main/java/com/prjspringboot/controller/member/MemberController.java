package com.prjspringboot.controller.member;

import com.prjspringboot.domain.member.Member;
import com.prjspringboot.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody Member member) {

        if (service.validate(member)) {

            service.add(member);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }

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
    public ResponseEntity checkNickName(@RequestParam("nickName") String nickName) {

        Member member = service.getByNickName(nickName);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nickName);
    }

    @GetMapping("/list")
    public List<Member> memberList() {

        List<Member> list = service.memberList();

        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        Member member = service.getById(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(member);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@RequestBody Member member) {

        if (service.hasAccess(member)) {
            service.remove(member.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/modify")
    public void modify(@RequestBody Member member) {

        System.out.println("member = " + member);

        service.modfiy(member);
    }

}
