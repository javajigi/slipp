alter table question drop column connected;

alter table answer add column sns_type enum ('facebook', 'twitter', 'google');
alter table answer add column post_id varchar(30);
