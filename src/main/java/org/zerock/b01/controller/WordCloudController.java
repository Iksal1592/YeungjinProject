package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.b01.dto.MemberRegisterDTO;
import org.zerock.b01.repository.BoardRepository;
import org.zerock.b01.service.BoardService;
import org.zerock.b01.service.OpenAIService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@Controller
public class WordCloudController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyLocal/LocalBoard/WordCloud/wordcloud")
    public String showWords(Model model) {
        List<String> words = boardService.extractWords1();
        model.addAttribute("words", words);
        return "/TopMenu/MyLocal/LocalBoard/WordCloud/wordcloud";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/word-frequencies")
    public ResponseEntity<Map<String, Integer>> getWordFrequencies(@RequestParam(name = "months", required = false, defaultValue = "0") int months) {

        LocalDateTime startDate;
        if (months > 0) {
            startDate = LocalDateTime.now().minusMonths(months);
        } else {
            startDate = boardRepository.findMinDate();
        }
        LocalDateTime endDate = LocalDateTime.now();
        Map<String, Integer> frequencies = boardService.getWordFrequencies(startDate, endDate);
        return ResponseEntity.ok(frequencies);
    }



}
