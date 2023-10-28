package com.class302.omzteam.dept_board.service;

import com.class302.omzteam.dept_board.model.Dept_board;
import com.class302.omzteam.dept_board.model.Page;
import com.class302.omzteam.mybatis.Dept_boardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Dept_BoardService {


    @Autowired
    PageService pageService;
    @Autowired
    Dept_boardDao deptBoardDao;


    public List<Dept_board> lostSet(int dept_no, String value, int select, int pageNo) {
        List<Dept_board> list = null;
        if (value.equals("")) {
            Page page = pageService.Pagein(pageNo, deptBoardDao.deptBoardCount(dept_no));
            list = deptBoardDao.lists(dept_no, page.getStartNo() - 1);
        } else {
            if (select == 1) {
                Page page = pageService.Pagein(pageNo, deptBoardDao.nameCount(dept_no, value));
                list = deptBoardDao.nameselect(dept_no, value, page.getStartNo() - 1);
            } else if (select == 2) {
                Page page = pageService.Pagein(pageNo, deptBoardDao.titleCount(dept_no, value));
                list = deptBoardDao.titleselect(dept_no, value, page.getStartNo() - 1);

            }
        }
        return list;
    }

    public Page pageSet(int dept_no, String value, int select, int pageNo) {
        Page page = null;
        if (select == 1) {
            page = pageService.Pagein(pageNo, deptBoardDao.nameCount(dept_no, value));
        } else if (select == 2) {
            page = pageService.Pagein(pageNo, deptBoardDao.titleCount(dept_no, value));
        }
    return page;
    }
}

