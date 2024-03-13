package com.filesharing.filesharingapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name = "Files")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class File extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long fileId;
    @ManyToOne
    @JoinColumn(name = "user_id_fk", nullable = false)
    private User user;
    @Column(name = "file_name_stored" , nullable = false)
    private String storedFileName;
    @Column(name = "file_name_original" , nullable = false)
    private String fileNameOriginal;
    @Column(name = "file_path" , nullable = false, columnDefinition = "TEXT")
    private String filePath;
    @Column(name = "file_size" , nullable = false)
    private Long fileSize;
}
