package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.controller.Util.SecurityUtil;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.*;
import org.zerock.b01.dto.Images.LocalBoardListAllDTO;
import org.zerock.b01.dto.Reply.LocalBoardReplyCountDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyCountDTO;
import org.zerock.b01.repository.BoardRepository;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.service.*;
import org.zerock.b01.dto.BoardDTO;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Log4j2

public class TopMenuController {

    private final MemberService memberService;


    private final MemberRepository memberRepository;

    @Value("${org.zerock.upload.path}")
    private String uploadPath;
    private final BoardService boardService;
    private final TradeBoardService tradeBoardService;




    /*--------------------------------------------Company------------------------------------------------*/




    @GetMapping("/TopMenu/Company/company")
    public void Company(Model model) {log.info("company 입장!!");}




    /*--------------------------------------------Main------------------------------------------------*/

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/Main/mainPage")
    public void mainPage(Model model) {
            log.info("mainPage 입장!!");
        }





    /*---------------------------------------------MyLocal-------------------------------------------------*/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyLocal/LocalBoard/localBoard") //Local Board list
    public String localBoardlist(PageRequestDTO pageRequestDTO, Model model) {

       // PageResponseDTO<LocalBoardReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
        PageResponseDTO<LocalBoardListAllDTO> responseDTO = boardService.BoardListWithAll(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);

        log.info("localBoardlist 입장!!");



        return "/TopMenu/MyLocal/LocalBoard/localBoard";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyLocal/LocalBoard/localboardregister") //Local Board Register
    public void LocalBoardRegister(Principal principal, Model model){
        String id = principal.getName();
        MemberRegisterDTO memberRegisterDTO = memberService.member(id);
        model.addAttribute("dto",memberRegisterDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TopMenu/MyLocal/LocalBoard/localboardregister")
    public String LocalBoardRegister(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("지역 게시판 등록");
        if(bindingResult.hasErrors()){
            log.info("유효성 오류");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/TopMenu/MyLocal/LocalBoard/localboardregister";
        }


        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result",bno);

        return "redirect:/TopMenu/MyLocal/LocalBoard/localBoard";

    }




    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyLocal/LocalBoard/localboardread") //read
    public void LocalBoardRead(Long bno, PageRequestDTO pageRequestDTO, Model model, Principal principal){
        String id = principal.getName();
        BoardDTO boardDTO = boardService.readOne(bno);
        MemberRegisterDTO member =memberService.myPage(id);

        model.addAttribute("replyer",member);

        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);

    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyLocal/LocalBoard/localboardmodify") //Local board modify
    public void LocalBoardModify(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);

    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/TopMenu/MyLocal/LocalBoard/localboardmodify")
    public String LocalBoardModify(PageRequestDTO pageRequestDTO, @Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("Local board modify...");
        if(bindingResult.hasErrors()){
            String link= pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno",boardDTO.getBno());
            return "redirect:/TopMenu/MyLocal/LocalBoard/localboardmodify?"+link;
        }
        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result","modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/TopMenu/MyLocal/LocalBoard/localboardread";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TopMenu/MyLocal/LocalBoard/localboardremove") //Local board remove
    public String localboardremove(RedirectAttributes redirectAttributes,BoardDTO boardDTO){
        long bno = boardDTO.getBno();
        log.info("remove..."+bno);
        boardService.delete(bno);

        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result","removed");
        return "redirect:/TopMenu/MyLocal/LocalBoard/localBoard";
    }




    public void removeFiles(List<String> files){
        for(String fileName:files){
            Resource resource = new FileSystemResource(uploadPath+ File.separator+fileName);
            String resourceName = resource.getFilename();

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();

                if(contentType.startsWith("image")){
                    File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);
                    thumbnailFile.delete();
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }






    /*---------------------------------------------MyPage-------------------------------------------------*/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyPage/myPage")
    public String myPage(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        if(username == null) {
            return "redirect:/LoginPage/login";
        }

        log.info("myPage 입장!!");
        MemberRegisterDTO member = memberService.myPage(username);
        model.addAttribute("member", member);
        return "/TopMenu/MyPage/myPage";
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyPage/memberModify")
    public String memberModifyGet(Model model, HttpSession session) {
        String username = SecurityUtil.getCurrentUsername();
        if(username == null) {
            return "redirect:/LoginPage/login";
        }
        log.info("memberModify 입장!!");
        MemberRegisterDTO member = memberService.myPage(username);
        model.addAttribute("member", member);
        return "/TopMenu/MyPage/memberModify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TopMenu/MyPage/memberModify")
    public String memberModifyPost(@ModelAttribute MemberRegisterDTO updatedMember, RedirectAttributes rttr) {

        String username = SecurityUtil.getCurrentUsername();
        log.info("username 통과!! "+username);
        if(username == null) {
            return "redirect:/LoginPage/login";
        }

        MemberRegisterDTO member = memberService.updateMemberInfo(updatedMember);

        if (member != null) {
            rttr.addFlashAttribute("member", member);
            return "redirect:/TopMenu/MyPage/myPage";
        } else {
            rttr.addFlashAttribute("member", "회원정보 업데이트 중 오류가 발생했습니다.");
            return "redirect:/TopMenu/MyPage/myPage";
        }
    }








    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/MyPage/modifyPw")
    public String modifyPwGet(HttpSession session) {
//        if (session.getAttribute("requireReAuth") != null && session.getAttribute("requireReAuth").equals(true)) {
//            log.info("패스워드 수정 페이지로 가기전 다시 인증하기");
//            return "redirect:/TopMenu/MyPage/reAuth";
//
//        }
        log.info("패스워드 수정 페이지 가기");

        return "/TopMenu/MyPage/modifyPw";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TopMenu/MyPage/modifyPw")
    public String modifyPwPost(Principal principal, @RequestParam String pw) {
        log.info("비밀번호 수정창 Post 입장!");

        String id = principal.getName();
        memberService.updatePw(id ,pw);
        return "redirect:/TopMenu/MyPage/myPage";
    }


//
//    @GetMapping("/TopMenu/MyPage/reAuth")
//    public String reAuthenticateGet() {
//        return "/TopMenu/MyPage/reAuth";
//    }
//
//
//
//    @PostMapping("/TopMenu/MyPage/reAuth")
//    public String processReAuth(HttpSession session, @RequestParam String username, @RequestParam String password) {
//        // 여기서 사용자 인증 로직 구현
//        // 예시: if (username.equals("user") && password.equals("password")) {
//        if (authenticate(username, password)) {
//            session.removeAttribute("requireReAuth");
//            return "redirect:/TopMenu/MyPage/modifyPw";
//        }
//        return "/TopMenu/MyPage/reAuth";
//    }
//
//    private boolean authenticate(String username, String password) {
//        MemberRegisterDTO memberRegisterDTO = new MemberRegisterDTO();
//       return username.equals(memberRegisterDTO.getMid()) && password.equals(memberRegisterDTO.getMpw());
//    }













    /*---------------------------------------------Trade-------------------------------------------------*/
    @Autowired
    private OpenAIService openAIService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TopMenu/Trade/TradeBoard")
    public String getSearchResults(PageRequestDTO pageRequestDTO,@RequestParam("query") String query, Model model) {


        try {
            log.info("resultView 입장시작!!");


            PageResponseDTO pageResponseDTO = tradeBoardService.listWithTradeReplyCount(pageRequestDTO);
            model.addAttribute("traderesponseDTO", pageResponseDTO);

            String results = openAIService.getRelatedWords(query);
            model.addAttribute("results", results);
            return "TopMenu/Trade/TradeBoard";
        } catch (Exception e) {
            log.info("resultView 입장 오류!!");
            e.printStackTrace();
            return "error";
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/Trade/TradeBoard") //Trade Board list
    public String TradeBoard(PageRequestDTO pageRequestDTO, Model model) {
        //PageResponseDTO pageResponseDTO = tradeBoardService.list(pageRequestDTO);
        PageResponseDTO<TradeBoardReplyCountDTO> pageResponseDTO = tradeBoardService.listWithTradeReplyCount(pageRequestDTO);
        model.addAttribute("traderesponseDTO", pageResponseDTO);
        return "/TopMenu/Trade/TradeBoard";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/Trade/register")
    public void registerGET(Model model, Principal principal) {
        String id = principal.getName();
        MemberRegisterDTO memberRegisterDTO = memberService.member(id);
        model.addAttribute("dto",memberRegisterDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TopMenu/Trade/register")
    public String registerPost(TradeDTO tradeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Trade board register");
        if (bindingResult.hasErrors()) {
            log.info("유효성 검사 오류남");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/TopMenu/Trade/register";
        }
        log.info(tradeDTO);
        Long bno = tradeBoardService.register(tradeDTO);

        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/TopMenu/Trade/TradeBoard";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/Trade/read")
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model, Principal principal){
        String id = principal.getName();
        TradeDTO tradeDTO = tradeBoardService.readOne(bno);
        MemberRegisterDTO member = memberService.myPage(id);

        model.addAttribute("replyer",member);
        
        log.info(tradeDTO);
        model.addAttribute("dto", tradeDTO);




    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/TopMenu/Trade/modify")
    public void modify(Long bno, PageRequestDTO pageRequestDTO, Model model){
        TradeDTO tradeDTO = tradeBoardService.readOne(bno);
        log.info(tradeDTO);
        model.addAttribute("dto", tradeDTO);

    }



    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/TopMenu/Trade/modify")
    public String TradeBoardModify(PageRequestDTO pageRequestDTO, @Valid TradeDTO tradeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("trade board modify....");
        if(bindingResult.hasErrors()){
            String link= pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno",tradeDTO.getBno());
            return "redirect:/TopMenu/Trade/modify?"+link;
        }
        tradeBoardService.modify(tradeDTO);
        redirectAttributes.addFlashAttribute("result","modified");
        redirectAttributes.addAttribute("bno", tradeDTO.getBno());
        return "redirect:/TopMenu/Trade/read";
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/TopMenu/Trade/remove")
    public String remove(Long bno,RedirectAttributes redirectAttributes){
        log.info("remove..."+bno);
        tradeBoardService.delete(bno);
        redirectAttributes.addFlashAttribute("result","removed");
        return "redirect:/TopMenu/Trade/TradeBoard";
    }

}
