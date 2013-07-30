alter table tag add column owner bigint after parent;
alter table tag add column group_id varchar(100) after owner;
alter table tag add column description varchar(1000) after group_id;

alter table tag 
    add index fk_tag_owner (owner), 
    add constraint fk_tag_owner 
    foreign key (owner) 
    references social_user (id);