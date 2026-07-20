CREATE TABLE orders (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    email            VARCHAR(255),
    post_number      VARCHAR(6),
    address          VARCHAR(255),
    shipment_status  VARCHAR(10),
    order_status     VARCHAR(10),
    total_price      BIGINT NOT NULL,
    order_at         DATETIME,
    dispatch_at      DATETIME,
    delivered_at     DATETIME
);