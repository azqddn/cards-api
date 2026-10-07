IF DB_ID('TESTDB') IS NULL
    CREATE DATABASE TESTDB;
GO

USE TESTDB;
GO

IF OBJECT_ID('dbo.cards', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.cards (
        card_id       UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID(),
        card_number   VARCHAR(16)   NOT NULL,
        card_holder   VARCHAR(100)  NOT NULL,
        card_type     VARCHAR(20)   NOT NULL,
        credit_limit  DECIMAL(18,2) NULL,
        card_status        VARCHAR(20)   NOT NULL,
        created_date  DATETIME2     NULL,
        updated_date  DATETIME2     NULL,
        deleted_date  DATETIME2     NULL,

        CONSTRAINT PK_cards PRIMARY KEY (card_id),
        CONSTRAINT CK_cards_card_type CHECK (card_type IN ('CREDIT', 'DEBIT', 'PREPAID')),
        CONSTRAINT CK_cards_status    CHECK (card_status    IN ('ACTIVE', 'INACTIVE', 'BLOCKED', 'CLOSED'))
    );
END
GO

-- unique only CardNumber among non-deleted cards
IF NOT EXISTS (SELECT 1 FROM sys.indexes
               WHERE name = 'UX_cards_card_number' AND object_id = OBJECT_ID('dbo.cards'))
    CREATE UNIQUE INDEX UX_cards_card_number
        ON dbo.cards (card_number)
        WHERE deleted_date IS NULL;
GO