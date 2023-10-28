package com.class302.omzteam.controller;


import com.class302.omzteam.dept_board.model.Comment;
import com.class302.omzteam.dept_board.model.Dept_board;
import com.class302.omzteam.dept_board.model.Page;
import com.class302.omzteam.dept_board.service.Dept_BoardService;
import com.class302.omzteam.dept_board.service.PageService;
import com.class302.omzteam.mybatis.CommentDao;
import com.class302.omzteam.mybatis.DeptDao;
import com.class302.omzteam.mybatis.Dept_boardDao;
import com.class302.omzteam.mybatis.MemberDao;
import com.class302.omzteam.organization.model.UserCommand;
import com.class302.omzteam.organization.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/deptboard")
public class DeptBoardController {

    @Autowired
    MemberDao memberDao;

    @Autowired
    UserService userService;

    @Autowired
    DeptDao deptDao;

    @Autowired
    Dept_boardDao deptBoardDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    PageService pageService;

    @Autowired
    Dept_BoardService deptBoardService;



    @GetMapping("/board")
    public  void board(@RequestParam(name = "bod_no")int bod_no,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userin =(User)authentication.getPrincipal();
        UserCommand user = userService.user(Integer.parseInt(userin.getUsername()));
        Dept_board deptBoard = deptBoardDao.boardOne(bod_no);
        List<Comment>commentList = commentDao.commentAll(bod_no);
        boolean authority = false;
        if (user.getMem_no() == deptBoard.getMem_no()){
            authority = true;
        }else if (user.getJob_no()<=3 && user.getJob_no() < userService.user(deptBoard.getMem_no()).getJob_no()){
            authority =true;
        }
        model.addAttribute("board",deptBoard);//글
        model.addAttribute("user",user);//사용자
        model.addAttribute("comment",commentList);//코맨트
        model.addAttribute("authority",authority);//권한
    }
    @GetMapping("/boardupdate")
    public  void update(@RequestParam(name = "bod_no")int bod_no,Model model){
        Dept_board deptBoard = deptBoardDao.boardOne(bod_no);
        model.addAttribute("board",deptBoard);
    }
    @GetMapping("/update")
    public  String updatein(@RequestParam(value = "notification",defaultValue = "0")int notification,Dept_board deptBoard, Model model){
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@한글안되냐@@@"+deptBoard);
        deptBoard.setIs_notice(notification);
        System.out.println(deptBoard);
        deptBoardDao.updateDb(deptBoard);
    return "redirect:/deptboard/list";
    }

    @GetMapping("/boardinsert")
    public  void insert(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userin =(User)authentication.getPrincipal();
        UserCommand user = userService.user(Integer.parseInt(userin.getUsername()));
        model.addAttribute("user",user);
    }
    @GetMapping("/insert")
    public String insertin(@RequestParam(value = "notification",defaultValue = "0")int notification,
                          Dept_board deptBoard){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userin =(User)authentication.getPrincipal();
        UserCommand user = userService.user(Integer.parseInt(userin.getUsername()));
                deptBoard.setDept_no(user.getDept_no());
                deptBoard.setMem_no(user.getMem_no());
                deptBoard.setWriter(user.getMem_name());
                deptBoard.setIs_notice(notification);
        deptBoardDao.insertDb(deptBoard);
        return "redirect:/deptboard/list";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam(value = "bod_no")int bod_no){
       deptBoardDao.deleteDb(bod_no);
        return "redirect:/deptboard/list";
    }
    @GetMapping ("/comment")
    public String comment(Comment comment){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userin =(User)authentication.getPrincipal();
        UserCommand user = userService.user(Integer.parseInt(userin.getUsername()));
        comment.setMem_no(user.getMem_no());
        comment.setMem_name(user.getMem_name());
        comment.setJob(user.getJob());
        commentDao.commentinsert(comment);
        return "redirect:/deptboard/board?bod_no="+comment.getBod_no();
    }
    @GetMapping ("/commentdelete")
    public String commentdelete(@RequestParam(value = "comment_no")int comment_no,
                                @RequestParam(value = "bod_no")int bod_no){

        commentDao.deleteComment(comment_no);
        return "redirect:/deptboard/board?bod_no="+bod_no;
    }

    @RequestMapping("/list")
    public void list2(@RequestParam(required = false,defaultValue = "1") int pageNo,
                      @RequestParam(name = "select",defaultValue = "1")int select,
                      @RequestParam(name = "value",defaultValue = "")String value,
                      Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userin =(User)authentication.getPrincipal();
        UserCommand user = userService.user(Integer.parseInt(userin.getUsername()));
        List<Dept_board> list = deptBoardService.lostSet(user.getDept_no(),value,select,pageNo);
        Page page = deptBoardService.pageSet(user.getDept_no(), value,select,pageNo);

        model.addAttribute("list",list);
        model.addAttribute("page",page);
        model.addAttribute("value",value);
        model.addAttribute("select",select);
    }


}