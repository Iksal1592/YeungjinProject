package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.Reply.LocalBoardReplyDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyDTO;
import org.zerock.b01.service.reply.LocalBoardReplyService;
import org.zerock.b01.service.reply.TradeBoardReplyService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/* @RestController :
Rest API를 위해서 사용되어짐, JSON 통신과 주고 받기 위함 화면이동없이 데이터만 주고 받기 위함 */

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/TopMenu/MyLocal/LocalBoard/localboardread/replies")
public class ReplyController {

    private final LocalBoardReplyService localBoardReplyService;
    private final TradeBoardReplyService tradeBoardReplyService;


    /* =====================================▼ 댓글 등록 ===================================================*/

    @PostMapping(value = "/replyregister", consumes = MediaType.APPLICATION_JSON_VALUE) //JSON 형태로 값을 변환시킴
    public Map<String, Long> register(
            //DTO의 값을 JSON 형태로 서버쪽에서 받아서 java 형태로 변환됨
            @Valid @RequestBody LocalBoardReplyDTO localBoardReplyDTO,
            BindingResult bindingResult) throws BindException {

        log.info(localBoardReplyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>(); //Map 객체를 만들어줌

        Long rno = localBoardReplyService.register(localBoardReplyDTO); //서비스의 register값을 rno에 담아줌
        resultMap.put("rno", rno); //Map에다가 담아준다.

        log.info("Local Board 게시글 등록됨");
        return resultMap;//resultMap에 값을 담아서 그대로 호출한다.


    }

    /* =====================================▼ 댓글 읽기 ===================================================*/

    @GetMapping("/read/{rno}")
    public LocalBoardReplyDTO read(@PathVariable("rno") Long rno) {
        //rno의 값으로 ReplyDTO를 반환한다.

        LocalBoardReplyDTO localBoardReplyDTO = localBoardReplyService.read(rno);
        //ReplyService.read 메서드의 반환값은 LocalBaordReplyDTO이다.

        return localBoardReplyDTO;

    }

    /*==================================▼ 댓글 수정 =================================================== */

    //특정 게시물의 특정 번호를 찾는다.

    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE) //JSON 형태로 들어감
    public Map<String, Long> modify(@PathVariable("rno") Long rno,
                                    @RequestBody LocalBoardReplyDTO localBoardReplyDTO) {

        //RequestBody는 JSON 형태를 자바형태로 변환하여 Http의 본문 내용을 주고 받기 위함이다.

        localBoardReplyDTO.setRno(rno); //특정 rno값을 넣어서 DTO가 변경 가능하도록 준비를 한다.
        localBoardReplyService.modify(localBoardReplyDTO); //해당 DTO의 값을 변경한다.

        Map<String, Long> resultMap = new HashMap<>(); //맵의 객체를 생성함
        resultMap.put("rno", rno); //Map에다가 값을 추가해주기 위해서 put을 사용한다.


        return resultMap;


    }

    /*==================================▼ 댓글 삭제 =================================================== */


    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {
        localBoardReplyService.remove(rno); //특정 rno의 값을 삭제함
        Map<String, Long> resultMap = new HashMap<>(); //객체 생성
        resultMap.put("rno", rno); //삭제한 rno의 값을 map에 넣어줌
        return resultMap;


    }

    /*==================================▼ 게시글 댓글 목록 =================================================== */
    //댓글을 목록화하고 목록화한 것을 페이지네이션한다.


    @GetMapping(value="/{bno}")
    public PageResponseDTO<LocalBoardReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){

        PageResponseDTO<LocalBoardReplyDTO> responseDTO = localBoardReplyService.getListOfBoard(bno, pageRequestDTO);
        return responseDTO;

    }





/*===========================================================================================================================================================================*/




    /*===========================================▼Trade Board 댓글 등록====================================================== */

    @PostMapping(value = "/tradereplyregister", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> tradeBoardRegister(

            @Valid @RequestBody TradeBoardReplyDTO tradeBoardReplyDTO,
            BindingResult bindingResult) throws BindException {

        log.info((tradeBoardReplyDTO));

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = tradeBoardReplyService.register(tradeBoardReplyDTO);

        resultMap.put("rno", rno);

        log.info("Trade Board 게시글 등록됨");

        return resultMap;


    }


    /*===========================================▼Trade Board 댓글 읽기====================================================== */


    @GetMapping("/tradereplyread/{rno}")
    public TradeBoardReplyDTO tradeBoardRead(@PathVariable("rno") Long rno){

        TradeBoardReplyDTO tradeBoardReplyDTO = tradeBoardReplyService.read(rno);

        return tradeBoardReplyDTO;
    }





    /*===========================================▼Trade Board 댓글 수정====================================================== */

    @PutMapping(value = "/tradereplymodify/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> tradeBoardmodify(@PathVariable("rno") Long rno,
                                    @RequestBody TradeBoardReplyDTO tradeBoardReplyDTO){


        tradeBoardReplyDTO.setRno(rno);
        tradeBoardReplyService.modify(tradeBoardReplyDTO);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }



    /*===========================================▼Trade Board 댓글 삭제====================================================== */

    @DeleteMapping(value = "tradereplydelete/{rno}")
    public Map<String, Long> tradeBoardremove(@PathVariable("rno") Long rno) {

        tradeBoardReplyService.remove(rno);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;

    }



    /*===========================================▼Trade Board 댓글 목록====================================================== */

    @GetMapping(value = "tradereplylist/{bno}")
    public PageResponseDTO<TradeBoardReplyDTO> tradeBoardGetList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){

        PageResponseDTO<TradeBoardReplyDTO> responseDTO = tradeBoardReplyService.getListOfBoard(bno, pageRequestDTO) ;

        return responseDTO;



    }




}

