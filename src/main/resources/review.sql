CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_id BIGINT NOT NULL,
    menu_version BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    rating INT NOT NULL,
    content VARCHAR(1000),
    created_at DATETIME NOT NULL,
    CONSTRAINT uk_review_menu_email
        UNIQUE (menu_id, email)

);