CREATE TABLE vehicles (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          manufacturer VARCHAR(255) NOT NULL,
                          model VARCHAR(255) NOT NULL,
                          price FLOAT NOT NULL,
                          type VARCHAR(255) NOT NULL,
                          "year" INT NOT NULL CHECK ("year" >= 1886 AND "year" <= 2100), -- Escaped keyword
                          description VARCHAR(500)
);
