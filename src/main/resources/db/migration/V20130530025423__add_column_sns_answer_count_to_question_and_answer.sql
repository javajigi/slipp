alter table question add column sns_answer_count integer not null default 0;
alter table answer add column sns_answer_count integer not null default 0;