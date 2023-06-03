#!/bin/bash

# get the current datetime
datetime=$(date +%Y%m%d%H%M%S)

# get the filename from the user input
filename="$1"

# directory where your migration files are stored
migration_dir="src/main/resources/db/migration"

# complete file path
file="${migration_dir}/V${datetime}__${filename}.sql"

# create a new file
touch $file

# print out the created file
echo "Created: $file"
