package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MemberRegisterDTO;
import org.zerock.b01.service.MemberService;

import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
@RequiredArgsConstructor
@Log4j2
public class LoginPageController {
    private final MemberService memberService;

    /*-----------------------------------▼ 로그인---------------------------------------------*/



    @GetMapping("/LoginPage/login")
    public String loginGet(@RequestParam(required = false) String error, RedirectAttributes redirectAttributes) {
        log.info("loginGet 입장!!");
        if (error != null) {
            redirectAttributes.addFlashAttribute("result", "error");
            return "redirect:/LoginPage/login";
        }
        return "/loginPage/login";
    }






    @GetMapping("/LoginPage/logout")
    public String logoutGet(HttpSession session) {
        session.invalidate();
        return "redirect:/LoginPage/login";
    }



    /*-----------------------------------▼ 회원가입---------------------------------------------*/


    @GetMapping("/LoginPage/register")
    public String registerform(){
        return "/LoginPage/register";
    }



    @PostMapping("/LoginPage/register")
    public String joinPOST(Member member, RedirectAttributes redirectAttributes){
        log.info("join post...");

        try{
            memberService.join(member);
        }catch (MemberService.MidExistException e){
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/LoginPage/register";
        }

        return "redirect:/LoginPage/login";
    }







    @GetMapping("/LoginPage/welcome")
    public void welcomeform(){

    }

    /*-----------------------------------▼ 아이디 중복 체크---------------------------------------------*/


    @PostMapping("/LoginPage/idcheck")
    public @ResponseBody String idCheck(@RequestParam String id){
        String checkResult = memberService.duplicationId(id);

        return checkResult;
    }


    /*-----------------------------------▼ 회원탈퇴---------------------------------------------*/


    @GetMapping("/LoginPage/unregister")
    public String unregisterform(Model model, Principal principal){
        MemberRegisterDTO memberRegisterDTO =  memberService.member(principal.getName());
        if(memberRegisterDTO !=null) {
            model.addAttribute("id", memberRegisterDTO.getMid());
            return "/LoginPage/unregister";

        }else{
            log.info("왜 아이디가 없지??");
            throw new RuntimeException();
        }
    }


    @PostMapping("/LoginPage/unregister")
    public String unregister(RedirectAttributes redirectAttributes, Principal principal, @RequestParam("password") String password) {
        String id = principal.getName();
        boolean passwordMatching = memberService.findMember(id, password);

        if (passwordMatching) {
            memberService.deleteMember(id);
            return "redirect:/LoginPage/goodbye";

        } else {
            redirectAttributes.addFlashAttribute("result","error");
            return "redirect:/LoginPage/unregister";
        }
    }

    @GetMapping("/LoginPage/goodbye")
    public String goodbye(HttpSession session){
        session.invalidate();
        return "/LoginPage/goodbye";
    }

}
