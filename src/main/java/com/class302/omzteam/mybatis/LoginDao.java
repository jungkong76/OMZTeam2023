package com.class302.omzteam.mybatis;

import com.class302.omzteam.member.model.MemberDto;
import org.apache.ibatis.annotations.Param;

public interface LoginDao {

    public void register(@Param("memberDto") MemberDto memberDto) throws Exception;

    MemberDto getLoginId(@Param("mem_no") Long mem_no);

    MemberDto getLoginPw(@Param("mem_pw") String mem_pw);

    Long getNextNum();

    String getPw();

    void updatePassword(@Param("mem_no") Long mem_no,
                        @Param("mem_pw") String mem_pw,
                        @Param("is_initial_login") boolean isInitialLogin);

    String getName(@Param("mem_name") String mem_name);

    Integer getDeptno(Long mem_no);


//    void save(MemberDto memberDto);

    int resetPassword(Long mem_no, String mem_pw);
}
