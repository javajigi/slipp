drop table if exists small_talk_comment;

create table small_talk_comment (
    small_talk_comment_id bigint not null auto_increment,
    small_talk bigint not null,
    created_date datetime not null,
    comments varchar(255) not null,
    updated_date datetime not null,
    writer bigint,
    primary key (small_talk_comment_id)
) ENGINE=InnoDB charset=utf8;

alter table small_talk_comment 
    add index fk_smalltalkcomment_writer (writer), 
    add constraint fk_smalltalkcomment_writer 
    foreign key (writer) 
    references social_user (id);
    
alter table small_talk_comment 
    add index fk_smalltalkcomment_parent_id (small_talk), 
    add constraint fk_smalltalkcomment_parent_id 
    foreign key (small_talk) 
    references small_talk (small_talk_id);
