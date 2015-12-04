drop table if exists small_talk;

create table small_talk (
    small_talk_id bigint not null auto_increment,
    created_date datetime not null,
    talk varchar(255) not null,
    updated_date datetime not null,
    writer bigint,
    primary key (small_talk_id)
) ENGINE=InnoDB;

alter table small_talk 
    add index fk_smalltalk_writer (writer), 
    add constraint fk_smalltalk_writer 
    foreign key (writer) 
    references social_user (id);