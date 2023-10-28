package com.class302.omzteam.controller;


import com.class302.omzteam.Message.dto.FileDto;
import com.class302.omzteam.Message.dto.Message;
import com.class302.omzteam.Message.dto.MessageMem;
import com.class302.omzteam.member.model.MemberDto;
import com.class302.omzteam.mybatis.MemberDao;
import com.class302.omzteam.mybatis.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MemberDao memberDao;

    private final int pageRow = 7;
    private final int pagePerPage = 5;

    @PostMapping("/getUnread")
    @ResponseBody
    public Map<String, Object> unreadForIcon(@RequestParam int mem_no){
        Map<String, Object> map = new HashMap<>();
        int unreadCount = messageDao.countUnread(mem_no);
            map.put("success", true);
            map.put("unreadCount", unreadCount);
        return map;
    }

    @PostMapping("/getProfile")
    @ResponseBody
    public Map<String, Object> memberProfile(@RequestParam int mem_no){
        Map<String, Object> map = new HashMap<>();
        MemberDto member = memberDao.selectOneMem(String.valueOf(mem_no));
        if(member == null) {
            map.put("success", false);
        } else {
            map.put("success", true);
            map.put("mem_name", member.getMem_name());
            map.put("mem_dept", member.getDept());
        }
        return map;
    }


    @GetMapping("/main")
    public void main(Model model, @RequestParam(required = false, defaultValue = "1") Integer pageNum, @RequestParam(required = false) Integer startPage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalCount = messageDao.countUnread(Integer.parseInt(user.getUsername()));
        int endPage = ((int)Math.ceil((double)pageNum / (double)pagePerPage)) * pagePerPage;
        int totalPage = totalCount / pageRow + (totalCount % pageRow >0 ? 1 :0);
        if (totalPage < endPage) {
            endPage = totalPage;
        }
        if (startPage == null) {
            startPage = endPage - pagePerPage + 1;
        }
        if (startPage < 1) {
            startPage = 1;
        }
        model.addAttribute("total_count", totalCount);
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("total_page", totalPage);
        model.addAttribute("pageNum", pageNum);
        int startRow = (pageNum-1) * pageRow + 1;
        int endRow = startRow + pageRow - 1;
        model.addAttribute("list", messageDao.main(Integer.parseInt(user.getUsername()), startRow, endRow));
    }

    @GetMapping("/readAll")
    public String readAll(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        messageDao.readAll(Integer.parseInt(user.getUsername()));
        return "redirect:/message/main";
    }

    @GetMapping("/view")
    public void view(Model model, @RequestParam int mno) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        model.addAttribute("fileList", messageDao.selectMessageFiles(mno));
        messageDao.checked(mno, Integer.parseInt(user.getUsername()));
        Message msg = messageDao.read(mno);
        model.addAttribute("msg", msg);
    }

    @GetMapping("/send")
    public void sendGet(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<MessageMem> list = messageDao.searchMem();
        model.addAttribute("list",list);

    }

    @PostMapping("/send")
    public String sendPost(int[] listener, String mTitle, String mContent, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        for(int listMem : listener) {
            messageDao.send(Integer.parseInt(user.getUsername()), listMem, mTitle, mContent);
            int mno = messageDao.lastInsertedMno();

            String tempDirectoryPath = "C:/Users/admin/Documents/files/message_" + mno + "/"; //메시지 별로 임시 디렉토리 생성해야 오류 안 남.
            File tempDirectory = new File(tempDirectoryPath);
            if (!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }

            List<MultipartFile> copiedFiles = new ArrayList<>(files);
            for (MultipartFile fl : copiedFiles) {
                if(!fl.isEmpty()) {
                    String originalFileName = fl.getOriginalFilename();
                    UUID uuid = UUID.randomUUID();
                    String contType = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String savedFileName = uuid.toString() + contType;
                    String savedPath = tempDirectoryPath+ savedFileName;
                    int fileSize = (int) fl.getSize();

                    Path path =  Paths.get(savedPath).toAbsolutePath();
                    Files.copy(fl.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    FileDto file = new FileDto(mno, originalFileName, savedFileName, savedPath, fileSize);
                    messageDao.uploadFile(file);
                }
            }
        }
        return "redirect:/message/my_sent";
    }

    @GetMapping("/selectedSend")
    public void selectedSendGet(Model model, @RequestParam String mem_no, @RequestParam String mem_nameDept) {
        model.addAttribute("mem_no", mem_no);
        model.addAttribute("mem_nameDept",mem_nameDept);
        List<MessageMem> list = messageDao.searchMem();
        model.addAttribute("list",list);

    }

    @GetMapping("/my_sent")
    public void my_sent(Model model, @RequestParam(required = false, defaultValue = "1") Integer pageNum, @RequestParam(required = false) Integer startPage){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalCount = messageDao.countMySent(Integer.parseInt(user.getUsername()));
        int endPage = ((int)Math.ceil((double)pageNum / (double)pagePerPage)) * pagePerPage;
        int totalPage = totalCount / pageRow + (totalCount % pageRow >0 ? 1 :0);
        if (totalPage < endPage) {
            endPage = totalPage;
        }
        if (startPage == null) {
            startPage = endPage - pagePerPage + 1;
        }
        if (startPage < 1) {
            startPage = 1;
        }
        model.addAttribute("total_count", totalCount);
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("total_page", totalPage);
        model.addAttribute("pageNum", pageNum);
        int startRow = (pageNum-1) * pageRow + 1;
        int endRow = startRow + pageRow - 1;
        model.addAttribute("sentList", messageDao.my_sent(Integer.parseInt(user.getUsername()), startRow, endRow));
    }

    @GetMapping("/selected_my_sent")
    public void my_sentBySO(Model model, @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                            @RequestParam(required = false) Integer startPage, @RequestParam String mem_no,
                            @RequestParam String mem_nameDept){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalCount = messageDao.countSelMySent(mem_no, Integer.parseInt(user.getUsername()));
        int endPage = ((int)Math.ceil((double)pageNum / (double)pagePerPage)) * pagePerPage;
        int totalPage = totalCount / pageRow + (totalCount % pageRow >0 ? 1 :0);
        if (totalPage < endPage) {
            endPage = totalPage;
        }
        if (startPage == null) {
            startPage = endPage - pagePerPage + 1;
        }
        if (startPage < 1) {
            startPage = 1;
        }
        model.addAttribute("mem_no",mem_no);
        model.addAttribute("mem_nameDept",mem_nameDept);
        model.addAttribute("total_count", totalCount);
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("total_page", totalPage);
        model.addAttribute("pageNum", pageNum);
        int startRow = (pageNum-1) * pageRow + 1;
        int endRow = startRow + pageRow - 1;
        model.addAttribute("sentList", messageDao.my_sentBySO(Integer.parseInt(user.getUsername()), startRow, endRow, mem_no));
    }

    @GetMapping("/my_received")
    public void my_received(Model model, @RequestParam(required = false, defaultValue = "1") Integer pageNum, @RequestParam(required = false) Integer startPage){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalCount = messageDao.countMyRec(Integer.parseInt(user.getUsername()));
        int totalPage = totalCount / pageRow + (totalCount % pageRow >0 ? 1 :0);
        int endPage = ((int)Math.ceil((double)pageNum / (double)pagePerPage)) * pagePerPage;
        if (totalPage < endPage) {
            endPage = totalPage;
        }
        if (startPage == null) {
            startPage = endPage - pagePerPage + 1;
        }
        if (startPage < 1) {
            startPage = 1;
        }
        model.addAttribute("total_count", totalCount);
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("total_page", totalPage);
        model.addAttribute("pageNum", pageNum);
        int startRow = (pageNum-1) * pageRow + 1;
        int endRow = startRow + pageRow - 1;
        model.addAttribute("recList", messageDao.my_received(Integer.parseInt(user.getUsername()),startRow, endRow));
    }

    @GetMapping("/selected_my_received")
    public void my_recBySO(Model model, @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false) Integer startPage, @RequestParam String mem_no,
                           @RequestParam String mem_nameDept){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalCount = messageDao.countSelMyRec(Integer.parseInt(user.getUsername()), mem_no);
        int totalPage = totalCount / pageRow + (totalCount % pageRow >0 ? 1 :0);
        int endPage = ((int)Math.ceil((double)pageNum / (double)pagePerPage)) * pagePerPage;
        if (totalPage < endPage) {
            endPage = totalPage;
        }
        if (startPage == null) {
            startPage = endPage - pagePerPage + 1;
        }
        if (startPage < 1) {
            startPage = 1;
        }
        model.addAttribute("mem_no",mem_no);
        model.addAttribute("mem_nameDept",mem_nameDept);
        model.addAttribute("total_count", totalCount);
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("total_page", totalPage);
        model.addAttribute("pageNum", pageNum);
        int startRow = (pageNum-1) * pageRow + 1;
        int endRow = startRow + pageRow - 1;
        model.addAttribute("recList", messageDao.my_recBySO(Integer.parseInt(user.getUsername()),startRow, endRow, mem_no));
    }

    @GetMapping("/my_bin")
    public void my_bin(Model model, @RequestParam(required = false, defaultValue = "1") Integer pageNum, @RequestParam(required = false) Integer startPage){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("countB", messageDao.countMyBin(Integer.parseInt(user.getUsername())));

        int totalCount = messageDao.countMyBin(Integer.parseInt(user.getUsername()));
        int totalPage = totalCount / pageRow + (totalCount % pageRow >0 ? 1 :0);
        int endPage = ((int)Math.ceil((double)pageNum / (double)pagePerPage)) * pagePerPage;
        if (totalPage < endPage) {
            endPage = totalPage;
        }
        if (startPage == null) {
            startPage = endPage - pagePerPage + 1;
        }
        if (startPage < 1) {
            startPage = 1;
        }
        model.addAttribute("total_count", totalCount);
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("total_page", totalPage);
        model.addAttribute("pageNum", pageNum);
        int startRow = (pageNum-1) * pageRow + 1;
        int endRow = startRow + pageRow - 1;
        model.addAttribute("binList", messageDao.showBin(Integer.parseInt(user.getUsername()), startRow, endRow));
    }

    @PostMapping("/delete")
    public String deleteMsg(@RequestParam int[] mno){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        for(int a : mno){
            messageDao.deleteMsg(a, Integer.parseInt(user.getUsername()));
        }
        return "redirect:/message/my_bin";
    }

    @PostMapping("/revive")
    public String reviveMsg(@RequestParam int[] mno){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        for(int a : mno){
            messageDao.reviveMsg(a, Integer.parseInt(user.getUsername()));
        }
        return "redirect:/message/my_bin";
    }

    @PostMapping("/deleteForever")
    public String deleteMsgForever(@RequestParam int[] mno){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        for(int a : mno){
            messageDao.deleteMsgForever(a, Integer.parseInt(user.getUsername()));
        }
        return "redirect:/message/my_bin";
    }

    @PostMapping("/deleteAllForever")
    public String deleteAllMsgForever(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        messageDao.deleteAllMsgForever(Integer.parseInt(user.getUsername()));
        return "redirect:/message/my_bin";
    }


    @GetMapping("/downloadFile")
    @ResponseBody
    public StreamingResponseBody downloadFile(@RequestParam int fno, HttpServletResponse response) throws IOException {
        FileDto file = messageDao.selectOneFile(fno);

        File newFile = new File(file.getFile_path());
        FileInputStream fileInputStream = new FileInputStream(newFile);
        String encodedFileName = URLEncoder.encode(file.getOgFile_name(), StandardCharsets.UTF_8);

        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName+"\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Length", String.valueOf(file.getFile_size()));
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = fileInputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
            fileInputStream.close();
        };
    }
}
