package com.teami.banham.controller;


import com.teami.banham.dto.BoardDTO;
import com.teami.banham.dto.CommentDTO;
import com.teami.banham.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.teami.banham.service.BoardService;
import com.teami.banham.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor //final 필드에 대한 생성자 자동 생성 (lombok)
@RequestMapping("/board/*")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    // 자랑 게시판 목록 (9/4)
    @GetMapping("/proud")
    public String findAll(@PageableDefault(page=1) Pageable pageable, Model model){
        int nowPage=pageable.getPageNumber();
        Page<BoardDTO> boardDTOPage = boardService.proudFindAll(pageable);
        int blockLimit = 5;
        int startPage=(((int)(Math.ceil((double)nowPage/blockLimit)))-1)*blockLimit+1; //1,6,11 ...
        int endPage=((startPage+blockLimit-1)<boardDTOPage.getTotalPages()) ? startPage+blockLimit-1:boardDTOPage.getTotalPages();
        model.addAttribute("boardList",boardDTOPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "ProudBoard";
    }

    // 자랑 게시판 글쓰기 (9/4)
    @GetMapping("/proud/save")
    public String proudSaveForm() {
        return "ProudSave";
    }

    // 자랑 게시판 글쓰기 (9/4)
    @PostMapping("/proud/save")
    public String proudSave(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("Proud Save ===>>> " +boardDTO);
        boardService.proudSave(boardDTO);
        return "redirect:/board/proud";
    }

    // 자랑게시판 게시글 보기 (9/4)
    @GetMapping("/proud/{bno}")
    public String findById(@PathVariable Long bno, Model model, @PageableDefault(page=1) Pageable pageable){
        boardService.proudUpdateHits(bno);
        BoardDTO boardDTO=boardService.proudFindById(bno);
        commentService.findAll(bno);
        List<CommentDTO> commentDTOList=commentService.findAll(bno);
        model.addAttribute("commentList",commentDTOList);
        model.addAttribute("board",boardDTO);
        model.addAttribute("page",pageable.getPageNumber());
        return "ProudView";
    }

    // 업데이트 구현 완료(9/5)
    @GetMapping("/proud/modify/{bno}")
    public String proudUpdateForm(@PathVariable Long bno, Model model){
        BoardDTO boardDTO=boardService.proudFindById(bno);
        model.addAttribute("boardUpdate",boardDTO);
        return "ProudModify";
    }

    // 업데이트 구현 완료(9/5)
    @PostMapping("/proud/modify")
    public String update(@ModelAttribute BoardDTO boardDTO,Model model) throws IOException {
        BoardDTO board= boardService.proudUpdate(boardDTO);
        model.addAttribute("board",board);
        return "redirect:/board/proud/"+boardDTO.getBno();
    }

    // 삭제 구현 완료(9/5)
    @GetMapping("/proud/delete/{bno}")
    public String proudDeleteForm(@PathVariable Long bno){
        boardService.proudDelete(bno);
        return "redirect:/board/proud";
    }
}
