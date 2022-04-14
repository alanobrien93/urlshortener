drop table if exists TBL_URL;

create table TBL_URL (
  id                    VARCHAR(10),
  full_url               VARCHAR(2083),
  primary key(id)
);