package com.konkuk.sample.controller;

import com.konkuk.sample.domain.Member;
import com.konkuk.sample.form.MemberForm;
import com.konkuk.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/member_modify")
    public String memberModifyForm(HttpServletRequest request, Model model) {
        Long id = (Long) request.getSession().getAttribute("memberId");
        Member member = memberService.getMember(id);
        MemberForm form = new MemberForm();
        form.setName(member.getName());
        form.setBirth(member.getBirth());
        model.addAttribute("form", form);
        return "member/member_modify";
    }

    @PostMapping(value = "/member_modify")
    public String memberModify(HttpServletRequest request, MemberForm form) {
        memberService.modifyMemberInfo(form.getBirth(), form.getName(), (Long) request.getSession().getAttribute("memberId"));
        return "redirect:/";
    }
}