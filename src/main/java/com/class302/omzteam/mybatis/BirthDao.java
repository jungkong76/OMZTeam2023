package com.class302.omzteam.mybatis;

import com.class302.omzteam.member.model.BirthDay;
import lombok.Setter;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BirthDao {

        @Select("select mem_name,birth from member")
        List<BirthDay> birthDay();

}
