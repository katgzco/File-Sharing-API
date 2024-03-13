package com.filesharing.filesharingapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
// @JsonSerialize(using = CustomDateSerializer.class) Revisar, para dejar campo, pero dejarlo sin null
public class FileResponseDTO {
    private String fileName;
    private Long fileSize;
    private LocalDateTime uploadDate;
    private LocalDateTime updateDate;
    private String owner;
}

