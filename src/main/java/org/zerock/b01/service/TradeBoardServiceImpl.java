package org.zerock.b01.service;


import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Trade;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyCountDTO;
import org.zerock.b01.dto.TradeDTO;
import org.zerock.b01.repository.TradeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class TradeBoardServiceImpl implements TradeBoardService {

    private final ModelMapper modelMapper;
    private final TradeRepository tradeRepository;

    @Override
    public Long register(TradeDTO tradeDTO){

        Trade trade = modelMapper.map(tradeDTO, Trade.class);
        Long bno = tradeRepository.save(trade).getBno();
        return bno;

    }


    @Override
    public PageResponseDTO<TradeDTO> list (PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Trade> result = tradeRepository.tradesearchAll(types, keyword, pageable);

        List<TradeDTO> dtoList = result.getContent().stream()
                .map(trade -> modelMapper.map(trade, TradeDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<TradeDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }


    @Override
    public TradeDTO readOne(Long bno){
        Optional<Trade> result = tradeRepository.findById(bno);
        Trade trade = result.orElseThrow();
        TradeDTO tradeDTO = modelMapper.map(trade, TradeDTO.class);
        return tradeDTO;
    }



    @Override
    public void modify(TradeDTO tradeDTO){
        Optional<Trade> result = tradeRepository.findById(tradeDTO.getBno());
        Trade trade = result.orElseThrow();
        trade.tradechange(tradeDTO.getTitle(), tradeDTO.getContent());
        tradeRepository.save(trade);
    }


    @Override
    public void delete(Long bno){
        tradeRepository.deleteById(bno);
    }


    @Override
    public PageResponseDTO<TradeBoardReplyCountDTO> listWithTradeReplyCount(PageRequestDTO pageRequestDTO){
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable();
        Page<TradeBoardReplyCountDTO> result = tradeRepository.searchWithTradeReplyCount(types, keyword, pageable);

        return PageResponseDTO.<TradeBoardReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }





}
