package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.*;
import org.zerock.b01.dto.Images.LocalBoardImageDTO;
import org.zerock.b01.dto.Images.LocalBoardListAllDTO;
import org.zerock.b01.dto.Reply.LocalBoardReplyCountDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;


public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl(){
        super(Board.class);
    }
    @Override
    public Page<Board> search1(Pageable pageable){
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        query.where(board.title.contains("1"));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable){
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if( (types != null && types.length >0 ) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String type : types){
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "l":
                        booleanBuilder.or(board.local.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<Trade> tradesearchAll(String[] types, String keyword, Pageable pageable){
        QTrade trade = QTrade.trade;
        JPQLQuery<Trade> query = from(trade);

        if( (types != null && types.length >0 ) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String type : types){
                switch (type) {
                    case "t":
                        booleanBuilder.or(trade.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(trade.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(trade.writer.contains(keyword));
                        break;
                    case "l":
                        booleanBuilder.or(trade.local.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(trade.bno.gt(0L));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Trade> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);
    }



    @Override
    public Page<LocalBoardReplyCountDTO> searchWithReplyCount(String[] types, String keyword,
                                                              Pageable pageable){

        QBoard board = QBoard.board;
        QLocalBoardReply reply = QLocalBoardReply.localBoardReply;
        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);

        if( (types != null && types.length >0 ) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String type : types){
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "l":
                        booleanBuilder.or(board.local.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L));
        JPQLQuery<LocalBoardReplyCountDTO> dtoQuery
                = query.select(Projections.bean(LocalBoardReplyCountDTO.class,
                board.bno, board.title, board.writer, board.regDate, board.local,
                reply.count().as("replyCount"))); //프로젝션설정

        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<LocalBoardReplyCountDTO> list = dtoQuery.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public Page<TradeBoardReplyCountDTO> searchWithTradeReplyCount(String[] types, String keyword,
                                                                   Pageable pageable){

        QTrade trade = QTrade.trade;
        QTradeBoardReply tradeBoardReply = QTradeBoardReply.tradeBoardReply;
        JPQLQuery<Trade> query = from(trade);
        query.leftJoin(tradeBoardReply).on(tradeBoardReply.trade.eq(trade));
        query.groupBy(trade);

        if( (types != null && types.length >0 ) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String type : types){
                switch (type) {
                    case "t":
                        booleanBuilder.or(trade.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(trade.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(trade.writer.contains(keyword));
                        break;
                    case "l":
                        booleanBuilder.or(trade.local.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(trade.bno.gt(0L));
        JPQLQuery<TradeBoardReplyCountDTO> dtoQuery
                = query.select(Projections.bean(TradeBoardReplyCountDTO.class,
                trade.bno, trade.title, trade.writer, trade.regDate, trade.local,
                tradeBoardReply.count().as("replyCount"))); //프로젝션설정

        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<TradeBoardReplyCountDTO> list = dtoQuery.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public Page<LocalBoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QLocalBoardReply localBoardReply = QLocalBoardReply.localBoardReply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(localBoardReply).on(localBoardReply.board.eq(board));

        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "l":
                        booleanBuilder.or(board.local.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }

        query.groupBy(board);
        getQuerydsl().applyPagination(pageable, query); //query문을 페이징 시켜 로딩을 줄여줌

        JPQLQuery<Tuple> tupleJPQLQuery = query.select(board, localBoardReply.countDistinct()); //중복된 댓글수를 제외하고 select함
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<LocalBoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board); //tuple은 다른 테이블들을 하나의 레코드화 시켜줌
            long replyCount = tuple.get(1, Long.class);

            LocalBoardListAllDTO dto = LocalBoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .local(board1.getLocal())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            List<LocalBoardImageDTO> boardImageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> LocalBoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build() //entity를 DTO로 변환하는 과정이다.
                    ).collect(Collectors.toList());

            dto.setBoardImages(boardImageDTOS); //썸네일을 보여주기 위해 list에 이미지를 넣는다.

            /*즉, LocalBoardListAllDTO를 객체화하고,
            entity를 DTO로 변환하여 LocalBoardListAllDTO에 imageDTO를 넣어준다.
            왜냐하면 썸네일을 위해서! 이것을 dtoList에 담는다.*/

            return dto;

        }).collect(Collectors.toList());

        long totalCount = query.fetchCount();
        return new PageImpl<>(dtoList, pageable, totalCount);
        //페이징할 때 썸네일 이미지와 page를 보여준다.

    }


}
