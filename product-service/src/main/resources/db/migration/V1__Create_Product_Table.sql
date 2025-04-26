CREATE TABLE product
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(50)    NOT NULL UNIQUE,
    name         VARCHAR(100)   NOT NULL,
    description  VARCHAR(500),
    price        DECIMAL(10, 2) NOT NULL,
    stock        INT            NOT NULL,
    created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP      NULL ON UPDATE CURRENT_TIMESTAMP
);

/* Create full-text index */
ALTER TABLE product
    ADD FULLTEXT INDEX idx_product_name_fulltext (name);

/* Create product code index */
CREATE INDEX idx_product_code ON product (product_code);
