drop table if exists notification;

create table notification (
    notification_id bigint not null auto_increment,
    readed bit not null,
    notifiee bigint,
    notifier bigint,
    question bigint,
    primary key (notification_id)
) ENGINE=InnoDB;

alter table notification 
    add index fk_notification_notifiee (notifiee), 
    add constraint fk_notification_notifiee 
    foreign key (notifiee) 
    references social_user (id);
    
alter table notification 
    add index fk_notification_notifier (notifier), 
    add constraint fk_notification_notifier 
    foreign key (notifier) 
    references social_user (id);
    
alter table notification 
    add index fk_notification_question (question), 
    add constraint fk_notification_question 
    foreign key (question) 
    references question (question_id);