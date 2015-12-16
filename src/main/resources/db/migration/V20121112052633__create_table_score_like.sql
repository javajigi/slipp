drop table if exists score_like;

create table score_like (
    id bigint not null auto_increment,
    like_type enum('ANSWER','QUESTION') not null,
    social_user_id bigint,
    target_id bigint,
    primary key (id)
) ENGINE=InnoDB;

alter table answer add column sum_like integer default 0 not null after writer;