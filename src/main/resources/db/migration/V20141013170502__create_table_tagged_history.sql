drop table if exists tagged_history;

create table tagged_history (
    history_id bigint not null auto_increment,
    created_date datetime not null,
    question_id bigint not null,
    user_id bigint not null,
    tag_id bigint not null,
    primary key (history_id)
) ENGINE=InnoDB;

create index idx_tagged_history_tag on tagged_history (tag_id);

create index idx_tagged_history_question on tagged_history (question_id);