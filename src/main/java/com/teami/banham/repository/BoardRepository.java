package com.teami.banham.repository;

import com.teami.banham.entity.ProudBoardEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<ProudBoardEntity,Long> {

    @Modifying // update, delete를 위한 어노테이션
    @Query(value = "update ProudBoardEntity b set b.hits=b.hits+1 where b.bno=:bno")
    void updateHits(@Param("bno") Long bno);

    @Query(value= "select pb from ProudBoardEntity pb where pb.delete_ck=0")
    Page<ProudBoardEntity> findAll(Pageable pageable);

    @Modifying
    @Query(value="update ProudBoardEntity b set b.delete_ck=1 where b.bno=:bno")
    void deleteById(@Param("bno") Long bno);

    Optional<ProudBoardEntity> findByBno(Long bno);
}
