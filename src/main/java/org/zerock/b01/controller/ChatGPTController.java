package org.zerock.b01.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.b01.dto.MemberRegisterDTO;
import org.zerock.b01.service.OpenAIService;

import javax.servlet.http.HttpSession;

@Log4j2
@Controller
public class ChatGPTController {
    @Autowired
    private OpenAIService openAIService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/Trade/Search/searchView")
    public String searchView(){

            log.info("searchView입장!!");
            return "/TopMenu/Trade/Search/searchView";

    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TopMenu/Trade/Search/resultView")
    public String getSearchResults(@RequestParam("query") String query, Model model) {
        try {
            log.info("resultView 입장시작!!");
            String results = openAIService.getRelatedWords(query);
            model.addAttribute("results", results);
            return "/TopMenu/MyLocal/Search/resultView";
        } catch (Exception e) {
            log.info("resultView 입장 오류!!");
            e.printStackTrace();
            return "error";
        }

    }
}