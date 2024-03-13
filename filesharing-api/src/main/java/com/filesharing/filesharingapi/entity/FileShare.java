package com.filesharing.filesharingapi.entity;


import java.time.LocalDateTime;

import com.filesharing.filesharingapi.enums.FileSharePermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "file_shares")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class FileShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="share_id")
    private Long shareId;
    @ManyToOne
    @JoinColumn(name = "file_id_fk", nullable = false)
    private File file;
    @ManyToOne
    @JoinColumn(name = "owner_id_fk", nullable = false)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "shared_with_user_id_fk", nullable = false)
    private User sharedWithUser;
    @Enumerated(EnumType.STRING)
    private FileSharePermission permissions;
}
