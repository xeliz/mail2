
CREATE TABLE MAIL2S (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    SENDER VARCHAR(100) NOT NULL,
    DATA TEXT NOT NULL
);

CREATE TABLE MAIL2_RECEIVERS (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    MAIL2_ID INTEGER NOT NULL,
    RECEIVER VARCHAR(100) NOT NULL,
    FOREIGN KEY(MAIL2_ID) REFERENCES MAIL2S(ID)
);

CREATE TABLE MAIL2_TOKENS (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    ADDRESS VARCHAR(100) NOT NULL,
    TOKEN TEXT UNIQUE NOT NULL
);
