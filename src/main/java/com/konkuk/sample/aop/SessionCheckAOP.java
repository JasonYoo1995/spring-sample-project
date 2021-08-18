package com.konkuk.sample.aop;


import com.konkuk.sample.exception.EmptySessionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@Aspect
class SessionCheckAOP {
    // LoginController를 제외한 모든 Controller에 AOP 적용
    @Around("execution(* com.konkuk.sample.controller..*(..)) && !execution(* com.konkuk.sample.controller.LoginController.*(..)) ")
    public Object sessionCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 현재 요청의 세션을 받아옴
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        HttpSession httpSession = request.getSession(true);

        Long memberId = (Long) httpSession.getAttribute("memberId");
        if(memberId == null){
            throw new EmptySessionException("세션이 만료되었습니다. 다시 로그인하세요. (주소창에 'http://localhost:8080/' 입력)");
        }
        return joinPoint.proceed();
    }
}