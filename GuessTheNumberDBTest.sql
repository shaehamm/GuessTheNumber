drop database if exists GuessTheNumberDBTest;
create database GuessTheNumberDBTest;
use GuessTheNumberDBTest;

create table Game (
	GameId int primary key auto_increment,
    Answer char(4) not null,
    `Status` boolean default(false)
);

create table Round (
	RoundId int primary key auto_increment,
    GameId int not null,
    Guess char(4) not null,
    TimeOfGuess datetime not null,
    ExactMatches int not null,
    PartialMatches int not null,
    
    foreign key (GameId) references Game (GameId)
);