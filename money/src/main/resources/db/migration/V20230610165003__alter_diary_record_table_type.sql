ALTER TABLE "diaryrecord"
ALTER COLUMN "date" TYPE date USING "date"::date;

ALTER TABLE "diaryrecord"
ALTER COLUMN "updated_at" DROP NOT NULL;

ALTER TABLE "diaryrecord"
ALTER COLUMN "updated_by" DROP NOT NULL;
