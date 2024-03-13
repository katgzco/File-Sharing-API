package com.filesharing.filesharingapi.mapper;

import com.filesharing.filesharingapi.dto.FileShareDto;
import com.filesharing.filesharingapi.entity.FileShare;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FileShareMapper {
    FileShareMapper INSTANCE = Mappers.getMapper(FileShareMapper.class);
    FileShareDto fileShareToFileShareDTO(FileShare fileShare);
    FileShare fileShareDTOToFileShare(FileShareDto fileShareDto);
}
