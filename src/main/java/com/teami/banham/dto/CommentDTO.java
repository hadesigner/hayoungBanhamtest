package com.teami.banham.dto;

import com.teami.banham.entity.CommentEntity;
import com.teami.banham.entity.ProudBoardEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long cno; // 댓글 고유 번호
    private String commentContents; // 댓글 내용
    private String writer; // 작성자 닉네임
    private String memberId; // 작성자 아이디
    private int delete_ck; // 삭제 여부 0:삭제안함 1:삭제함
    private Long bno; // 게시글 고유 번호
    private LocalDateTime commentCreatedTime;
    private LocalDateTime commentUpdateTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity){
        CommentDTO commentDTO=new CommentDTO();
        commentDTO.setCno(commentEntity.getCno());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setWriter(commentEntity.getWriter());
        commentDTO.setMemberId(commentEntity.getMemberId());
        commentDTO.setDelete_ck(commentEntity.getDelete_ck());
        commentDTO.setCommentCreatedTime(commentEntity.getCreateDate());
        commentDTO.setCommentUpdateTime(commentEntity.getUpdateDate());

        return commentDTO;

    }
}
