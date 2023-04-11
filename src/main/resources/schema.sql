CREATE TABLE route (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       uri VARCHAR(256) not null,
                       domain_id BIGINT not null,
                       route_id VARCHAR(256) not null,
                       metadata VARCHAR(256)
);

CREATE TABLE filterandrpredicate (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       route_id VARCHAR(256) not null,
                       is_filter BOOLEAN not null,
                       name VARCHAR(256) not null
);

CREATE TABLE args (

                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           route_id VARCHAR(256) not null,
                           parent_name VARCHAR(256) not null,
                           hash_key VARCHAR(256) not null,
                           hash_value VARCHAR(256)
);