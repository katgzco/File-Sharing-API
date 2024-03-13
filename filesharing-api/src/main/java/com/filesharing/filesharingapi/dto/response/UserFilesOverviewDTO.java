package com.filesharing.filesharingapi.dto.response;

import com.filesharing.filesharingapi.dto.response.FileResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserFilesOverviewDTO {
    public List<FileResponseDTO> owned;
    public List<FileResponseDTO> shared;
}
