-- This command will alter the data type of the 'date' column to 'timestamp'.
ALTER TABLE "diaryrecord"
ALTER COLUMN "date" TYPE timestamp USING "date"::timestamp;

-- This command will alter the 'name' column to NOT NULL
ALTER TABLE "diaryrecord"
ALTER COLUMN "name" SET NOT NULL;

-- This command will alter the 'created_at' column to NOT NULL
ALTER TABLE "diaryrecord"
ALTER COLUMN "created_at" SET NOT NULL;

-- This command will alter the 'updated_at' column to NOT NULL
ALTER TABLE "diaryrecord"
ALTER COLUMN "updated_at" SET NOT NULL;
