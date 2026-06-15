INSERT INTO patient (name, gender, email, blood_group, birth_date, created_at, updated_at)
VALUES ('Amit Sharma', 'Male', 'amit.sharma@example.com', 'O+', '1990-05-15', NOW(), NOW()) ON CONFLICT (email) DO NOTHING;
INSERT INTO patient (name, gender, email, blood_group, birth_date, created_at, updated_at)
VALUES ('Priya Singh', 'Female', 'priya.singh@example.com', 'A-', '1992-08-22', NOW(), NOW()) ON CONFLICT (email) DO NOTHING;
INSERT INTO patient (name, gender, email, blood_group, birth_date, created_at, updated_at)
VALUES ('Rahul Verma', 'Male', 'rahul.verma@example.com', 'B+', '1988-11-30', NOW(), NOW()) ON CONFLICT (email) DO NOTHING;

INSERT INTO doctor (name, specialization, email)
VALUES ('Dr. Anjali Singh', 'Cardiologist', 'anjali.singh@example.com') ON CONFLICT (email) DO NOTHING;
INSERT INTO doctor (name, specialization, email)
VALUES ('Dr. Vikram Kumar', 'Pediatrician', 'vikram.kumar@example.com') ON CONFLICT (email) DO NOTHING;
INSERT INTO doctor (name, specialization, email)
VALUES ('Dr. Pooja Sharma', 'Dermatologist', 'pooja.sharma@example.com') ON CONFLICT (email) DO NOTHING;

