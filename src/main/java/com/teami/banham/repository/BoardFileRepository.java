package com.teami.banham.repository;

import com.teami.banham.entity.ProudBoardFileEntity;
import org.apache.ibatis.annotations.Param;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardFileRepository extends JpaRepository<ProudBoardFileEntity,Long> {

    //해당 게시글 파일 삭제
    @Modifying
    @Query("delete from ProudBoardFileEntity pd WHERE  pd.proudBoardEntity.bno=:bno")
    void deleteProudBoardFileEntitiesByBno(Long bno);

}
