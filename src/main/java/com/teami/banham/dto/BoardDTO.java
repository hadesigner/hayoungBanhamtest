package com.teami.banham.dto;

import com.teami.banham.entity.CommentEntity;
import com.teami.banham.entity.ProudBoardEntity;
import com.teami.banham.entity.ProudBoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    //게시판 관련 DTO
    private Long bno; // 게시글 고유 번호
    private String title; // 게시글 제목
    private String contents; // 게시글 내용
    private int hits; // 게시글 조회 횟수
    private String writer; //글쓴 사람 닉네임
    private String memberId; // 글쓴사람 ID
    private LocalDateTime createDate; //만든
    private LocalDateTime updateDate;
    private int delete_ck; // 게시글 삭제 여부 = 0 : 삭제 안함 , 1 : 삭제상태
    //파일첨부 관련 DTO
    private int hasFile; // 파일 있는지 여부 0 : 파일첨부 없음 , 1 : 파일첨부 있음
    private List<String> originalFileName; //원본 파일 이름
    private List<String> repositoryFileName; //서버 저장용 파일 이름
    private List<MultipartFile> fileList; //게시글에 첨부된 파일 목록
    private List<CommentDTO> commentDTOList;

    public BoardDTO(Long bno,String title, String contents, int hits, String writer, String memberId, LocalDateTime createDate,LocalDateTime updateDate,int hasFile,List<ProudBoardFileEntity> boardFileEntityList,List<CommentEntity> commentEntityList){
        this.bno=bno;
        this.title=title;
        this.contents=contents;
        this.hits=hits;
        this.writer=writer;
        this.memberId=memberId;
        this.createDate=createDate;
        this.updateDate=updateDate;
        this.hasFile=hasFile;
        if (hasFile== 0) {
            this.setHasFile(hasFile);
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> repositoryFileNameList = new ArrayList<>();
            this.setHasFile(hasFile);
            for (ProudBoardFileEntity FileEntity : boardFileEntityList) {
                originalFileNameList.add(FileEntity.getOriginalFileName());
                repositoryFileNameList.add(FileEntity.getRepositoryFileName());
            }
            this.setOriginalFileName(originalFileNameList);
            this.setRepositoryFileName(repositoryFileNameList);
        }

        if(commentEntityList.isEmpty()){
            this.commentDTOList=null;
        } else{
            List<CommentDTO> dtoList = new ArrayList<>();
            for(int i = 0;i<commentEntityList.size();i++){
                dtoList.add(CommentDTO.toCommentDTO(commentEntityList.get(i)));
            }
            this.commentDTOList=dtoList;
        }

    }

    public static BoardDTO toBoardDTO(ProudBoardEntity proudBoardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBno(proudBoardEntity.getBno());
        boardDTO.setTitle(proudBoardEntity.getTitle());
        boardDTO.setContents(proudBoardEntity.getContents());
        boardDTO.setHits(proudBoardEntity.getHits());
        boardDTO.setWriter(proudBoardEntity.getWriter());
        boardDTO.setMemberId(proudBoardEntity.getMemberId());
        boardDTO.setCreateDate(proudBoardEntity.getCreateDate());
        boardDTO.setUpdateDate(proudBoardEntity.getUpdateDate());

        if(proudBoardEntity.getHasFile()==0){
            boardDTO.setHasFile(proudBoardEntity.getHasFile());
        } else {
            boardDTO.setHasFile(proudBoardEntity.getHasFile());
            List<String> originalFileName=new ArrayList<>();
            List<String> repositoryFileName=new ArrayList<>();
            for(ProudBoardFileEntity proudBoardFileEntity : proudBoardEntity.getProudBoardFileEntityList()){
                originalFileName.add(proudBoardFileEntity.getOriginalFileName());
                repositoryFileName.add(proudBoardFileEntity.getRepositoryFileName());
            }
            boardDTO.setOriginalFileName(originalFileName);
            boardDTO.setRepositoryFileName(repositoryFileName);
        }
        return boardDTO;
    }
}
