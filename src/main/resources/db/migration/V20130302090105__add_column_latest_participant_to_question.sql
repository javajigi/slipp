alter table question add column latest_participant bigint default 1 not null after writer;

alter table question 
    add index fk_question_latest_participant (latest_participant), 
    add constraint fk_question_latest_participant 
    foreign key (latest_participant) 
    references social_user (id);
