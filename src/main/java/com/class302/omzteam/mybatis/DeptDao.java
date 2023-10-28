package com.class302.omzteam.mybatis;

import com.class302.omzteam.organization.model.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeptDao {




    @Select("select dept FROM dept WHERE dept_no = #{dept_no}")
    String deptByno(int dept_no);


    @Select("select * FROM dept")
    List<Dept> deptAll();


}
