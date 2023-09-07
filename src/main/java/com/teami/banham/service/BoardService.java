package com.teami.banham.service;

import com.teami.banham.dto.BoardDTO;
import com.teami.banham.entity.BoardBaseEntity;
import com.teami.banham.entity.ProudBoardEntity;
import com.teami.banham.entity.ProudBoardFileEntity;
import com.teami.banham.repository.BoardFileRepository;
import com.teami.banham.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.teami.banham.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    //자랑 게시판 게시글 작성시 저장 메소드
    public Long proudSave(BoardDTO boardDTO) throws IOException {
        System.out.println("serviceDTO = " + boardDTO);

        if (boardDTO.getFileList().stream().anyMatch(MultipartFile::isEmpty)) {
            // 파일 첨부 없을시
            ProudBoardEntity proudBoardEntity = ProudBoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(proudBoardEntity);
            return boardRepository.save(proudBoardEntity).getBno();
        } else {
            // 파일 첨부 있을시
            ProudBoardEntity proudBoardEntity = ProudBoardEntity.toSaveFileEntity(boardDTO);
            Long saveId = boardRepository.save(proudBoardEntity).getBno();
            ProudBoardEntity proudBoard = boardRepository.findById(saveId).get();
            for (MultipartFile proudBoardFileList : boardDTO.getFileList()) {
                String originalFileName = proudBoardFileList.getOriginalFilename();
                String repositoryFileName = System.currentTimeMillis() + "" + ((int) (Math.random() * 1000)) + "_" + originalFileName;
                String savePath = "C:/banham_img/" + repositoryFileName;
                proudBoardFileList.transferTo(new File(savePath)); // 경로에 이름변경한 파일을 저장

                ProudBoardFileEntity proudBoardFileEntity = ProudBoardFileEntity.toBoardFileEntity(proudBoard, originalFileName, repositoryFileName);
                boardFileRepository.save(proudBoardFileEntity);
            }
            return saveId;
        }
    }

    @Transactional
    public Page<BoardDTO> proudFindAll(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //spring JPA에서 page는 0부터 시작하기때문
        int pageLimit = 12; // 한페이지에 보여줄 글 갯수
        // 한 페이지 당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<ProudBoardEntity> proudBoardEntities =                                                   //Entity에 들어있는 값 기준
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "bno")));
        Page<BoardDTO> boardDTOS = proudBoardEntities.map(board -> new BoardDTO(board.getBno(), board.getTitle(), board.getContents(), board.getHits(), board.getWriter(), board.getMemberId(), board.getCreateDate(), board.getUpdateDate(), board.getHasFile(), board.getProudBoardFileEntityList(),board.getCommentEntityList()));
        return boardDTOS;
    }

    @Transactional
    public void proudUpdateHits(Long bno) {
        boardRepository.updateHits(bno);
    }

    //게시글 상세 조회
    @Transactional
    public BoardDTO proudFindById(Long bno) {
        Optional<ProudBoardEntity> optionalProudBoardEntity = boardRepository.findById(bno);
        if (optionalProudBoardEntity.isPresent()) {
            ProudBoardEntity proudBoardEntity = optionalProudBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(proudBoardEntity);
            System.out.println("findById======>>>>>    " + boardDTO);
            return boardDTO;
        } else {
            return null;
        }
    }


    //게시글 수정
    @Transactional
    public BoardDTO proudUpdate(BoardDTO boardDTO) throws IOException {
        System.out.println("service update DTO =====>  " + boardDTO);
        BoardDTO board = proudFindById(boardDTO.getBno());
        if (board.getHasFile()==0) { // 수정하려는 게시글에 기존 첨부파일이 없을시
            if (boardDTO.getFileList().stream().anyMatch(MultipartFile::isEmpty)) {
                // 수정할때 파일 첨부 없을시
                ProudBoardEntity proudBoardEntity = ProudBoardEntity.toSaveEntity(boardDTO);
                boardRepository.save(proudBoardEntity);
                return proudFindById(boardDTO.getBno());
            } else {
                // 수정할때 파일 첨부 있을시
                ProudBoardEntity proudBoardEntity = ProudBoardEntity.toSaveFileEntity(boardDTO);
                Long saveId = boardRepository.save(proudBoardEntity).getBno();
                ProudBoardEntity proudBoard = boardRepository.findById(saveId).get();
                for (MultipartFile proudBoardFileList : boardDTO.getFileList()) {
                    String originalFileName = proudBoardFileList.getOriginalFilename();
                    String repositoryFileName = System.currentTimeMillis() + "" + ((int) (Math.random() * 1000)) + "_" + originalFileName;
                    String savePath = "C:/banham_img/" + repositoryFileName;
                    proudBoardFileList.transferTo(new File(savePath)); // 경로에 이름변경한 파일을 저장

                    ProudBoardFileEntity proudBoardFileEntity = ProudBoardFileEntity.toBoardFileEntity(proudBoard, originalFileName, repositoryFileName);
                    boardFileRepository.save(proudBoardFileEntity);
                }
                return proudFindById(saveId);
            }
        } else { // 수정하려는 게시글에 기존 첨부파일이 있었을시
            boardFileRepository.deleteProudBoardFileEntitiesByBno(boardDTO.getBno());
            String path="C:/banham_img/";
            if (boardDTO.getFileList().stream().anyMatch(MultipartFile::isEmpty)) {
                // 수정할때 파일 첨부 없을시
                ProudBoardEntity proudBoardEntity = ProudBoardEntity.toSaveEntity(boardDTO);
                boardRepository.save(proudBoardEntity);
                return proudFindById(boardDTO.getBno());
            } else {
                // 수정할때 파일 첨부 있을시
                ProudBoardEntity proudBoardEntity = ProudBoardEntity.toSaveFileEntity(boardDTO);
                Long saveId = boardRepository.save(proudBoardEntity).getBno();
                ProudBoardEntity proudBoard = boardRepository.findById(saveId).get();
                for (MultipartFile proudBoardFileList : boardDTO.getFileList()) {
                    String originalFileName = proudBoardFileList.getOriginalFilename();
                    String repositoryFileName = System.currentTimeMillis() + "" + ((int) (Math.random() * 1000)) + "_" + originalFileName;
                    String savePath = "C:/banham_img/" + repositoryFileName;
                    proudBoardFileList.transferTo(new File(savePath)); // 경로에 이름변경한 파일을 저장

                    ProudBoardFileEntity proudBoardFileEntity = ProudBoardFileEntity.toBoardFileEntity(proudBoard, originalFileName, repositoryFileName);
                    boardFileRepository.save(proudBoardFileEntity);
                }
                return proudFindById(saveId);
            }
        }
    }

    @Transactional
    public void proudDelete(Long bno){
        boardRepository.deleteById(bno);
    }
}
