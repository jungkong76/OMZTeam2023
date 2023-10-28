package com.class302.omzteam.Message.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class FileDto {
    private int fno;
    private int mno;
    private String ogFile_name;
    private String file_name;
    private String file_path;
    private int file_size;
    private String created_date;

    public FileDto(int mno, String ogFile_name, String file_name, String file_path, int file_size) {
        this.mno = mno;
        this.ogFile_name = ogFile_name;
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_size = file_size;
    }
}
