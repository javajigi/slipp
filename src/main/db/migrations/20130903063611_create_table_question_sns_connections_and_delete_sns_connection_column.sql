drop table if exists question_sns_connections;

create table question_sns_connections (
    question_id bigint not null,
    post_id varchar(100),
    group_id varchar(100),
    sns_answer_count integer not null,
    sns_type enum ('facebook', 'twitter', 'google', 'slipp')
) ENGINE=InnoDB;

alter table question_sns_connections 
    add index fk_question_sns_connection_question_id (question_id), 
    add constraint fk_question_sns_connection_question_id 
    foreign key (question_id) 
    references question (question_id);

insert into question_sns_connections 
	(question_id, post_id, sns_answer_count, sns_type)
	select question_id, post_id, sns_answer_count, sns_type from question
	where post_id is not null;

alter table answer add column group_id varchar(100) after post_id;

alter table question drop column sns_type;
alter table question drop column post_id;
alter table question drop column sns_answer_count;