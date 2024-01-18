package org.zerock.b01.service.reply;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.TradeBoardReply;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyDTO;
import org.zerock.b01.repository.reply.TradeBoardReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TradeBoardReplyServiceImpl implements TradeBoardReplyService {

    private final TradeBoardReplyRepository tradeBoardReplyRepository;
    private final ModelMapper modelMapper;

    /* =============================================▼ Trade Board 댓글 등록 =====================================================*/

    @Override
    public Long register(TradeBoardReplyDTO tradeBoardReplyDTO){

        TradeBoardReply tradeBoardReply = modelMapper.map(tradeBoardReplyDTO, TradeBoardReply.class);

        Long rno = tradeBoardReplyRepository.save(tradeBoardReply).getRno();

        return rno;

    }

    /*=======================================▼ ▼ Trade Board 댓글 읽기==============================================================*/


    @Override
    public TradeBoardReplyDTO read(Long rno){

        Optional<TradeBoardReply> optionalTradeBoardReply = tradeBoardReplyRepository.findById(rno);

        TradeBoardReply tradeBoardReply = optionalTradeBoardReply.orElseThrow();

        return modelMapper.map(tradeBoardReply, TradeBoardReplyDTO.class);


    }

    /*=======================================▼ ▼ Trade Board 댓글 수정==============================================================*/


    @Override
    public void modify(TradeBoardReplyDTO tradeBoardReplyDTO){

        Optional<TradeBoardReply> optionalTradeBoardReply = tradeBoardReplyRepository.findById(tradeBoardReplyDTO.getRno());

        TradeBoardReply tradeBoardReply = optionalTradeBoardReply.orElseThrow();

        tradeBoardReply.changereply(tradeBoardReplyDTO.getReplytext());

        tradeBoardReplyRepository.save(tradeBoardReply);

    }


    /*=======================================▼ ▼ Trade Board 댓글 삭제==============================================================*/

    @Override
    public void remove(Long rno){
        tradeBoardReplyRepository.deleteById(rno);

    }



    /*==================================▼ ▼ Trade Board 댓글 목록화와 페이징=======================================================*/


    @Override
    public PageResponseDTO<TradeBoardReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()<=0? 0: pageRequestDTO.getPage()-1,

                pageRequestDTO.getSize(), Sort.by("rno").ascending());

        Page<TradeBoardReply> result = tradeBoardReplyRepository.listOfTradeBoard(bno, pageable);

        List<TradeBoardReplyDTO> dtoList = result.getContent().stream()
                .map(reply -> modelMapper.map(reply, TradeBoardReplyDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<TradeBoardReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }

}
