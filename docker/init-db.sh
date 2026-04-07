#!/bin/bash
set -e

create_db() {
  local db="$1"
  psql --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    SELECT 'CREATE DATABASE "$db"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$db')
    \gexec
EOSQL
}

create_db "skillsync-auth"
create_db "skillsync-groups"
create_db "skillsync-learners"
create_db "skillsync-mentors"
create_db "skillsync-payments"
create_db "skillsync-reviews"
create_db "skillsync-sessions"
create_db "skillsync-skills"
create_db "skillsync-users"
