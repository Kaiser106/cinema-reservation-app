create table user_sessions (
  id uuid primary key,
  session_token varchar(200) not null unique,
  user_id uuid not null references users(id),
  role varchar(20) not null,
  user_agent varchar(300),
  ip_address varchar(100),
  expires_at timestamptz not null,
  invalidated_at timestamptz,
  created_at timestamptz not null
);
