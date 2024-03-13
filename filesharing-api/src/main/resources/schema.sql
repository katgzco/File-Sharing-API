CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE,
                       email VARCHAR(255) UNIQUE ,
                       role VARCHAR(10),
                       password_hash VARCHAR(255),
                       created_at TIMESTAMP,
                       created_by VARCHAR(255),
                       updated_at TIMESTAMP
);

CREATE TABLE files (
                       file_id SERIAL PRIMARY KEY,
                       user_id_fk INTEGER NOT NULL,
                       file_name_stored VARCHAR(255) NOT NULL,
                       file_name_original VARCHAR(255) NOT NULL,
                       file_path TEXT NOT NULL,
                       file_size BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP,
                       created_by VARCHAR(255),
                       FOREIGN KEY (user_id_fk) REFERENCES users(user_id)
                        ON DELETE CASCADE
);

CREATE TABLE file_shares (
                             share_id SERIAL PRIMARY KEY,
                             file_id_fk INTEGER NOT NULL,
                             owner_id_fk INTEGER NOT NULL,
                             shared_with_user_id_fk INTEGER NOT NULL,
                             permissions VARCHAR(50) NOT NULL,
                             created_at TIMESTAMP,
                             updated_at TIMESTAMP,
                             created_by VARCHAR(255),
                             FOREIGN KEY (file_id_fk) REFERENCES files(file_id)
                             ON DELETE CASCADE,
                             FOREIGN KEY (owner_id_fk) REFERENCES users(user_id)
                            ON DELETE CASCADE,
                             FOREIGN KEY (shared_with_user_id_fk) REFERENCES users(user_id)
                            ON DELETE CASCADE
);
