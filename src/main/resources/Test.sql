CREATE TABLE `customers` (
  `customer_id`   BIGINT       NOT NULL AUTO_INCREMENT,
  `provider_id`   VARCHAR(100) NOT NULL,
  `provider_type` VARCHAR(20)  NOT NULL,   -- 예: KAKAO, GOOGLE
  `email`         VARCHAR(250) NULL,
  `phone_number`  VARCHAR(50)  NOT NULL,
  `birth`         DATE         NOT NULL,
  `name`          VARCHAR(50)  NOT NULL,
  `gender`        VARCHAR(20)  NOT NULL,   -- 예: MALE, FEMALE
  `pin`           INT          NOT NULL,   -- 고객만 추가
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
                                 ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted_at`    DATETIME(3)  NULL,
  PRIMARY KEY (`customer_id`),

  UNIQUE KEY `uq_customers_provider` (`provider_type`, `provider_id`),
  UNIQUE KEY `uq_customers_email`    (`email`),
  UNIQUE KEY `uq_customers_phone`    (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `owners` (
  `owner_id`      BIGINT       NOT NULL AUTO_INCREMENT,
  `provider_id`   VARCHAR(100) NOT NULL,
  `provider_type` VARCHAR(20)  NOT NULL,
  `email`         VARCHAR(250) NULL,
  `phone_number`  VARCHAR(50)  NOT NULL,
  `birth`         DATE         NOT NULL,
  `name`          VARCHAR(50)  NOT NULL,
  `gender`        VARCHAR(20)  NOT NULL,
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
                                 ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted_at`    DATETIME(3)  NULL,
  PRIMARY KEY (`owner_id`),

  UNIQUE KEY `uq_owners_provider` (`provider_type`, `provider_id`),
  UNIQUE KEY `uq_owners_email`    (`email`),
  UNIQUE KEY `uq_owners_phone`    (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
