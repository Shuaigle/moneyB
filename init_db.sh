#!/usr/bin/env bash
set -x
set -eo pipefail

# Check if a custom user has been set, otherwise default to 'postgres'
DB_USER=${POSTGRES_USER:=postgres}
# Check if a custom password has been set, otherwise default to 'password'
DB_PASSWORD="${POSTGRES_PASSWORD:=password}"
# Check if a custom database name has been set, otherwise default to 'money'
DB_NAME="${POSTGRES_DB:=money}"
# Check if a custom port has been set, otherwise default to '5432'
DB_PORT="${POSTGRES_PORT:=5432}"
# Check if a custom host has been set, otherwise default to 'localhost'
DB_HOST="${POSTGRES_HOST:=localhost}"
# Launch postgres using Docker
docker run \
-e POSTGRES_USER=${DB_USER} \
-e POSTGRES_PASSWORD=${DB_PASSWORD} \
-e POSTGRES_DB=${DB_NAME} \
-p "${DB_PORT}":5432 \
--mount source=money,target=/var/lib/postgresql/data \
--name money-db \
-d postgres \
postgres -N 1000
# ^ Increased maximum number of connections for testing purposes

# Check Postgres is up and running
</dev/tcp/${DB_HOST}/${DB_PORT}
if [ "$?" -ne 0 ]; then
  echo "Connection to ${DB_HOST} on port ${DB_PORT} failed, Postgres is still unavailable"
  exit 1
else
  echo "Postgres is up and running on ${DB_HOST}, port ${DB_PORT}"
fi

>&2 echo "Postgres has been migrated, ready to go!"
