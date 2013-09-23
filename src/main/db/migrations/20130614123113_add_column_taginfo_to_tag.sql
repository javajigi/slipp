alter table tag add column group_id varchar(100) after parent;
alter table tag add column description varchar(1000) after group_id;