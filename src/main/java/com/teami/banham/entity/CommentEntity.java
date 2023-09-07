package com.teami.banham.entity;

import com.teami.banham.dto.CommentDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Many;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="comment_table")
@SequenceGenerator(
        name="COMMENT_SEQ_GEN", //시퀀스 제네레이터 이름
        sequenceName = "SEQ_COMMENT", //시퀀스 이름
        initialValue = 1, //시퀀스 시작값
        allocationSize = 1 //할당할 범위 사이즈
)
public class CommentEntity extends BoardBaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "COMMENT_SEQ_GEN")
    private Long cno; // 댓글 고유 번호

    @Column
    private String commentContents;

    @Column
    private String writer;

    @Column
    private String memberId;

    @Column
    private int delete_ck;

    /*  Board:Comment 1:다수 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProudBoardEntity proudBoardEntity;

    public static CommentEntity toProudBoardSaveEntity(CommentDTO commentDTO,ProudBoardEntity proudBoardEntity){
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCno(commentDTO.getCno());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setWriter(commentDTO.getWriter());
        commentEntity.setMemberId(commentDTO.getMemberId());
        commentEntity.setDelete_ck(commentDTO.getDelete_ck());
        commentEntity.setProudBoardEntity(proudBoardEntity);

        return commentEntity;
    }

}
