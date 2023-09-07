package com.teami.banham.repository;

import com.teami.banham.entity.CommentEntity;
import com.teami.banham.entity.ProudBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    @Modifying
    @Query(value="update CommentEntity c set c.delete_ck=1 where c.proudBoardEntity.bno=:bno")
    void deleteCommentById(Long bno);

    List<CommentEntity> findAllByProudBoardEntityOrderByCnoDesc(ProudBoardEntity proudBoardEntity);
}
