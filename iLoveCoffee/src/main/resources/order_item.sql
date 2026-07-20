CREATE TABLE order_item (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT NOT NULL,
    menu_id    BIGINT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      BIGINT NOT NULL,
    quantity   INT NOT NULL,

    -- FK 제약조건 설정
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_menu  FOREIGN KEY (menu_id)  REFERENCES menu (id)
);