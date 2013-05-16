alter table social_user add column email varchar(255) after user_id;
alter table social_user add column password varchar(255) after email;

alter table social_user modify column provider_id enum ('facebook', 'twitter', 'google', 'slipp');