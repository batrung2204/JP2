CREATE DATABASE HeroGame;
USE HeroGame;

CREATE TABLE National (
    NationalId INT AUTO_INCREMENT PRIMARY KEY,
    NationalName VARCHAR(100)
);

CREATE TABLE Player (
    PlayerId INT AUTO_INCREMENT,
    NationalId INT,
    PlayerName VARCHAR(100),
    HighScore INT,
    Level INT,
    PRIMARY KEY (PlayerId, NationalId),
    FOREIGN KEY (NationalId) REFERENCES National(NationalId)
);
INSERT INTO National (NationalName) VALUES ('Vietnam'), ('Japan'), ('USA');
