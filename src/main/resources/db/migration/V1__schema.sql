-- 1. 메뉴 테이블
CREATE TABLE IF NOT EXISTS menu (
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 메뉴 버전 히스토리 테이블
CREATE TABLE IF NOT EXISTS menu_version (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_id BIGINT NOT NULL,
    version BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    price BIGINT NOT NULL,
    stock INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    archived_at DATETIME NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    post_number VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    shipment_status VARCHAR(20) NOT NULL,
    order_status VARCHAR(20) NOT NULL,
    total_price BIGINT NOT NULL,
    order_at DATETIME,
    dispatch_at DATETIME,
    delivered_at DATETIME
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 주문 상세 항목 테이블
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    menu_version BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price BIGINT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_order_item_order
    FOREIGN KEY (order_id)
    REFERENCES orders(id)
    ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--5. 리뷰 테이블
CREATE TABLE IF NOT EXISTS review (
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