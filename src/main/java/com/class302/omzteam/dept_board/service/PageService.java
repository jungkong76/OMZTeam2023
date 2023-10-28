package com.class302.omzteam.dept_board.service;

import com.class302.omzteam.dept_board.model.Page;
import org.springframework.stereotype.Service;

@Service
public class PageService {
    public Page Pagein(int pageNo, int count){
        Page page = new Page(pageNo,count);
        return page;
    }


}
