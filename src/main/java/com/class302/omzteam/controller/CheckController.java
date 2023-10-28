package com.class302.omzteam.controller;

import com.class302.omzteam.member.model.CheckDto;
import com.class302.omzteam.member.service.CheckService;
import com.class302.omzteam.mybatis.MemberDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/check")
@Log4j2
public class CheckController {

    @Autowired
    MemberDao memberDao;

    @Autowired
    CheckService checkService;

    @GetMapping
    public String check() {
        return "check/checkForm";
    }

    @PostMapping("/in")
    public ResponseEntity<Map<String, Object>> checkIn(@RequestBody Map<String, Long> requestBody, Model model, RedirectAttributes rattr) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String no = user.getUsername();

        int dateId = checkService.getDateId(no);

        if (dateId == 0) {
            checkService.insertDateId();
            dateId = checkService.getDateId(no);
        }

        if (checkService.hasCheckedIn(no, dateId) == 1) {
            Time getTime = checkService.getInTime(no, dateId);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("inTime", getTime.toString());
            responseData.put("msg", "CHK_IN_ALD");
            return ResponseEntity.ok(responseData);
        }

        int rowCnt = checkService.insertCheckIn(no, dateId);
        if (rowCnt == 1) {
            Time getTime = checkService.getInTime(no, dateId);

            // 응답 데이터를 JSON 형식으로 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("inTime", getTime.toString()); // 시간 데이터를 문자열로 변환해서 전달
            responseData.put("msg", "CHK_IN_OK");
            return ResponseEntity.ok(responseData);
        } else {
            rattr.addFlashAttribute("msg", "CHK_ERR");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/out")
    public ResponseEntity<Map<String, Object>> checkOut(@RequestBody Map<String, Long> requestBody, Model model, RedirectAttributes rattr) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String no = user.getUsername();

        int dateId = checkService.getDateId(no);

        Map<String, Object> responseData = new HashMap<>();

        if(checkService.hasCheckedIn(no, dateId) == 0) {
            responseData.put("outTime", "");
            responseData.put("msg", "CHK_IN_YET");
            return ResponseEntity.ok(responseData);
        }

        if (checkService.hasCheckedOut(no, dateId) == 1) {
            Time getTime = checkService.getOutTime(no, dateId);
            responseData.put("outTime", getTime.toString());
            responseData.put("msg", "CHK_OUT_ALD");
            return ResponseEntity.ok(responseData);
        }

        int rowCnt = checkService.updateCheckOut(no, dateId);
        if (rowCnt == 1) {
            Time getTime = checkService.getOutTime(no, dateId);

            // 응답 데이터를 JSON 형식으로 생성
            responseData.put("outTime", getTime.toString()); // 시간 데이터를 문자열로 변환해서 전달
            responseData.put("msg", "CHK_OUT_OK");
            return ResponseEntity.ok(responseData);
        } else {
            rattr.addFlashAttribute("msg", "CHK_ERR");
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/isCheckIn")
    public ResponseEntity<Map<String, Object>> isCheckIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String no = user.getUsername();
        System.out.println("user no: "+no);

        int dateId = checkService.getDateId(no);
        System.out.println("dateId: "+dateId);

        Map<String, Object> responseData = new HashMap<>();

        if (checkService.hasCheckedIn(no, dateId) != 1) {
            responseData.put("msg", "CHK_ERR");
            return ResponseEntity.ok(responseData);
        }
        Time getInTime = checkService.getInTime(no, dateId);
        System.out.println("getTime: "+getInTime);
        responseData.put("inTime", getInTime.toString());
        responseData.put("msg", "CHK_IN_ALD");
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/isCheckOut")
    public ResponseEntity<Map<String, Object>> isCheckOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String no = user.getUsername();
        System.out.println("user no: "+no);

        int dateId = checkService.getDateId(no);
        System.out.println("dateId: "+dateId);

        Map<String, Object> responseData = new HashMap<>();

        if (checkService.hasCheckedOut(no, dateId) != 1) {
            responseData.put("msg", "CHK_ERR");
            return ResponseEntity.ok(responseData);
        }
        Time getOutTime = checkService.getOutTime(no, dateId);
        System.out.println("getTime: "+getOutTime);
        responseData.put("outTime", getOutTime.toString());
        responseData.put("msg", "CHK_OUT_ALD");
        return ResponseEntity.ok(responseData);
    }

}

