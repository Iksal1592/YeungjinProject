package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query(value="select now()", nativeQuery = true)
    String getTime();


    @Query("select min(b.regDate) from Board b")
    LocalDateTime findMinDate();

    // 가장 최근 게시글의 날짜를 찾는 쿼리
    @Query("select max(b.regDate) from Board b")
    LocalDateTime findMaxDate();


    @Query("SELECT c FROM Board c WHERE c.regDate >= :startDate AND c.regDate <= :endDate")
    List<Board> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Board c WHERE c.regDate >= :startDate AND c.regDate <= :endDate")
    List<Board> findByDateRange1(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select img from Board img where img.bno=:bno")
    Optional<Board> findByIdWithImages(Long bno);
    

    }
