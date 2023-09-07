package com.teami.banham.service;

import com.teami.banham.dto.BoardDTO;
import com.teami.banham.dto.CommentDTO;
import com.teami.banham.entity.CommentEntity;
import com.teami.banham.entity.ProudBoardEntity;
import com.teami.banham.repository.BoardRepository;
import com.teami.banham.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO){
        // 게시글 엔티티 조회
        Optional<ProudBoardEntity> optionalProudBoardEntity=boardRepository.findByBno(commentDTO.getBno());
        if(optionalProudBoardEntity.isPresent()){
            ProudBoardEntity proudBoardEntity = optionalProudBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toProudBoardSaveEntity(commentDTO,proudBoardEntity);
            return commentRepository.save(commentEntity).getCno();
        }else {
            return null;
        }
    }

    @Transactional
    public List<CommentDTO> findAll(Long bno){
        ProudBoardEntity proudBoardEntity = boardRepository.findById(bno).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByProudBoardEntityOrderByCnoDesc(proudBoardEntity);

        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity commentEntity : commentEntityList){
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    @Transactional
    public CommentDTO proudCommentFindById(Long Cno) {
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(Cno);
        if (optionalCommentEntity.isPresent()) {
            CommentEntity commentEntity = optionalCommentEntity.get();
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity);
            System.out.println("commentDTO findById======>>>>>    " + commentDTO);
            return commentDTO;
        } else {
            return null;
        }
    }

    //-- 댓글 수정 기능 메소드 구현 --
    @Transactional
    public CommentDTO proudCommentUpdate(CommentDTO commentDTO){
        Optional<ProudBoardEntity> optionalProudBoardEntity=boardRepository.findByBno(commentDTO.getBno());
        if(optionalProudBoardEntity.isPresent()){
            ProudBoardEntity proudBoardEntity = boardRepository.findById(commentDTO.getBno()).get();
        CommentEntity commentEntity = CommentEntity.toProudBoardSaveEntity(commentDTO,proudBoardEntity);
        commentRepository.save(commentEntity);
        return proudCommentFindById(commentDTO.getCno());
    } else {
            return null;
        }
    }

    public void delete(Long bno){
        commentRepository.deleteCommentById(bno);
    }
}
