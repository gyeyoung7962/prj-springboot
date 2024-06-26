package com.prjspringboot.controller.member;

import com.prjspringboot.domain.member.Member;
import com.prjspringboot.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public List<Member> memberList() {

        List<Member> list = service.memberList();

        return list;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity get(@PathVariable Integer id, Authentication authentication) {

        if (!service.hasAccess(id, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Member member = service.getById(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(member);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity delete(@RequestBody Member member, Authentication authentication) {

        if (service.hasAccess(member, authentication)) {
            service.remove(member.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity modify(@RequestBody Member member,
                                 Authentication authentication) {

        if (service.hasAccessModify(member, authentication)) {

            // "token", "wd3adsw"
            Map<String, Object> result = service.modify(member, authentication);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @GetMapping("/checkNick")
    public ResponseEntity checkNick(@RequestParam("nickName") String nickName) {

        Member member = service.getInfoByNick(nickName);

        if (member == null) {

            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(nickName);
        }

    }

    @PostMapping("/token")
    public ResponseEntity token(@RequestBody Member member) {
        Map<String, Object> map = service.getToken(member);

        if (map == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(map);
    }

}
