package com.class302.omzteam.mybatis;


import com.class302.omzteam.organization.model.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobDao {




    @Select("select job FROM job WHERE job_no = #{job_no}")
    String jobByno(int job_no);


    @Select("select * FROM job")
    List<Job> jobAll();


}
