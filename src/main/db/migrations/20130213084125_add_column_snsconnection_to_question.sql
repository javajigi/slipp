alter table question add column connected bit default false not null;
alter table question add column sns_type enum ('facebook', 'twitter', 'google');
alter table question add column post_id varchar(30);
