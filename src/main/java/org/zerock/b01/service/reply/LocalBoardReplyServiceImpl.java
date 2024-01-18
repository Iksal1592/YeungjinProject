package org.zerock.b01.service.reply;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.LocalBoardReply;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.Reply.LocalBoardReplyDTO;
import org.zerock.b01.repository.reply.LocalBoardReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class LocalBoardReplyServiceImpl implements LocalBoardReplyService {

    private final LocalBoardReplyRepository localreplyRepository;
    private final ModelMapper modelMapper;

    /*=======================================▼ 댓글 쓰기==============================================================*/

    @Override
    public Long register(LocalBoardReplyDTO localBoardReplyDTO){

        LocalBoardReply localBoardReply = modelMapper.map(localBoardReplyDTO, LocalBoardReply.class);
        //DTO를 entity로 변환하는 것을 entity에 담는다.

        Long rno = localreplyRepository.save(localBoardReply).getRno();
        //모델맵핑된 entity에 저장된 값을 Rno의 값을 가져와 rno로 담아준다.


        return rno;
    }


    /*=======================================▼ 댓글 읽기==============================================================*/

    @Override
    public LocalBoardReplyDTO read(Long rno){

        Optional<LocalBoardReply> optionalLocalBoardReply = localreplyRepository.findById(rno);
        //repository에서 rno값을 찾아서 OptionalLocalBoardReply에 넣는다.

        LocalBoardReply reply = optionalLocalBoardReply.orElseThrow();
        //rno값이 있다면 entity에 값을 넣는다.

        return modelMapper.map(reply, LocalBoardReplyDTO.class);
        //entity를 DTO로 변환한 것을 반환한다.



    }

    /*=======================================▼ 댓글 수정==============================================================*/

    @Override //수정은 반환을 필요로 하지않음, LocalBoardReplyDTO를 가져와야함
    public void modify(LocalBoardReplyDTO localBoardReplyDTO){

        Optional<LocalBoardReply> optionalLocalBoardReply = localreplyRepository.findById(localBoardReplyDTO.getRno());
        //수정을 하는 것이니까 DTO로 기존의 값을 가지고와야함

       LocalBoardReply localBoardReply = optionalLocalBoardReply.orElseThrow();
       //DTO에서 rno값이 있으면 localBoardReply에 담는다.

       localBoardReply.changereply(localBoardReplyDTO.getReplytext());
       //localBoardReplyDTO에서 기존의 reply를 갖고와서 changereply메서드로 변경을 해준다.

       localreplyRepository.save(localBoardReply);
       //변경된 값을 repository로 저장을 해서 entity에 넣어준다.



   }

    /*=======================================▼ 댓글 삭제==============================================================*/

   @Override
    public void remove(Long rno) {
        localreplyRepository.deleteById(rno);

   }

    /*=======================================▼ 댓글 목록화와 페이징 ==============================================================*/



    @Override
    public PageResponseDTO<LocalBoardReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO){

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()<=0? 0: pageRequestDTO.getPage()-1,
                //page의 인덱스가 0보다 작거나 0과 같으면 0으로 넣고, 0보다 크면(1이라면) -1을 해서 0으로 맞춰줌
                pageRequestDTO.getSize(), Sort.by("rno").ascending());
                //rno의 값을 기준으로 순서대로 정렬시킨다.
                //이것을 Pageable로 담는다.
               //Pageable은 PageRequest.of로 getPage, getSize를 설정할 수 있다.



        Page<LocalBoardReply> result = localreplyRepository.listOfBoard(bno, pageable);
        //특정 게시물을 갖고와서 pageable화 된 entity를 Page화 시킴


        List<LocalBoardReplyDTO> dtoList = result.getContent().stream()
                .map(reply-> modelMapper.map(reply, LocalBoardReplyDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<LocalBoardReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }


}
