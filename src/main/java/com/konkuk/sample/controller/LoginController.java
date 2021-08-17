package com.konkuk.sample.controller;

import com.konkuk.sample.domain.Member;
import com.konkuk.sample.exception.MemberNotFoundException;
import com.konkuk.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;

    @GetMapping(value = "/")
    public String loginForm(HttpServletRequest request, Model model) {
        String name = (String) request.getSession().getAttribute("name");
        if(name == null){ // 로그아웃된 상태일 때
            System.out.println("세션 내에 회원 정보가 없음");
            LoginForm form = new LoginForm();
            model.addAttribute("form", form);
            return "initial_page/login";
        }
        else{ // 로그인된 상태일 때
            System.out.println("세션 내 회원 정보 : " + name);
            return "redirect:/home";
        }
    }

    @PostMapping(value = "/login")
    public String login(HttpServletRequest request, LoginForm form) {
        String name = form.getName();

        Member member = null;
        try{
            member = memberService.getMemberByName(name); // 회원 조회
        }
        catch (MemberNotFoundException e){
            System.out.println(e.getMessage());
        }

        if(member == null){ // 존재하지 않는 회원일 때 -> 로그인 실패
            return "redirect:/";
        }
        else{ // 존재하는 회원일 때 -> 로그인 성공
            request.getSession().setAttribute("name", name);
            System.out.println("set session : " + name);
            return "redirect:/home";
        }
    }

    @GetMapping(value = "/sign_up")
    public String signUpForm(Model model) {
        SignUpForm form = new SignUpForm();
        model.addAttribute("form", form);
        return "initial_page/sign_up";
    }

    @PostMapping(value = "/sign_up")
    public String signUp(SignUpForm form) {
        memberService.signUp(form.getBirth(), form.getName());
        return "redirect:/";
    }
}