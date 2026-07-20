CREATE TABLE menu_version (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_id BIGINT NOT NULL,
    version BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    price BIGINT NOT NULL,
    stock INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    archived_at DATETIME NOT NULL
);