package com.teami.banham.controller;

import com.teami.banham.dto.CommentDTO;
import com.teami.banham.entity.ProudBoardEntity;
import com.teami.banham.service.BoardService;
import com.teami.banham.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO){
        System.out.println("commentDTO ======>>>>> "+commentDTO);
        Long saveResult=commentService.save(commentDTO);
        if(saveResult!=null){ //성공시 해당 게시글의 댓글목록 리턴
            List<CommentDTO> commentDTOList=commentService.findAll(commentDTO.getBno());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다..",HttpStatus.NOT_FOUND);
        }
    }


//    @PostMapping("/update/{Cno}")
//    public  ResponseEntity update(@PathVariable Long Cno,Model model){
//        CommentDTO comment = commentService.proudCommentUpdate(commentDTO);
//        if(comment!=null){ //성공시 해당 게시글의 댓글목록 리턴
//            List<CommentDTO> commentDTOList=commentService.findAll(commentDTO.getBno());
//            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
//        }else {
//            return new ResponseEntity<>("해당 댓글이 존재하지 않습니다..",HttpStatus.NOT_FOUND);
//        }
//    }
}
