CREATE TABLE menu (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        version BIGINT NOT NULL DEFAULT 1,
        name VARCHAR(100) NOT NULL,
        description VARCHAR(1000),
        image_url VARCHAR(255),
        price BIGINT NOT NULL,
        stock INT NOT NULL,
        status VARCHAR(20) NOT NULL,
        created_at DATETIME,
        updated_at DATETIME,
        deleted_at DATETIME
);