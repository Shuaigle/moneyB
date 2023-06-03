CREATE TABLE DiaryRecord (
  id BIGSERIAL PRIMARY KEY,
  date VARCHAR(255) NOT NULL,
  icon VARCHAR(255),
  type VARCHAR(255),
  name VARCHAR(255),
  price DECIMAL(10, 2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  updated_by BIGINT,
  FOREIGN KEY (updated_by) REFERENCES MoneyUser(id)
);

CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
   RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_diaryrecord_modtime
BEFORE UPDATE ON DiaryRecord
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();
