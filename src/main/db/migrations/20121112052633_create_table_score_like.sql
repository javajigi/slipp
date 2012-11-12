drop table if exists score_like;

create table score_like (
    id bigint not null auto_increment,
    like_type enum('ANSWER','QUESTION') not null,
    social_user_id bigint,
    target_id bigint,
    primary key (id)
) ENGINE=InnoDB;

alter table answer add column sum_like (11) default 0 null after writer;