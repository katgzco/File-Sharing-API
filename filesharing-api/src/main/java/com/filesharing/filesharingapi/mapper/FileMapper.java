package com.filesharing.filesharingapi.mapper;

import com.filesharing.filesharingapi.dto.FileDto;
import com.filesharing.filesharingapi.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    FileDto fileToFileDTO(File file);
    File fileDTOToFile(FileDto fileDto);
}
