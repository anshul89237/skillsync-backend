#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE "skillsync-auth";
    CREATE DATABASE "skillsync-groups";
    CREATE DATABASE "skillsync-learners";
    CREATE DATABASE "skillsync-mentors";
    CREATE DATABASE "skillsync-reviews";
    CREATE DATABASE "skillsync-sessions";
    CREATE DATABASE "skillsync-skills";
    CREATE DATABASE "skillsync-users";
EOSQL
