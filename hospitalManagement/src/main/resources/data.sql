-- Truncate tables and restart identity sequences to ensure clean state and predictable IDs
TRUNCATE TABLE appointment RESTART IDENTITY CASCADE;
TRUNCATE TABLE insurance RESTART IDENTITY CASCADE;
TRUNCATE TABLE doctor RESTART IDENTITY CASCADE;
TRUNCATE TABLE patient RESTART IDENTITY CASCADE;

-- Insert sample patients
INSERT INTO patient (name, gender, email, blood_group, birth_date, created_at, updated_at)
VALUES ('Amit Sharma', 'Male', 'amit.sharma@example.com', 'O_POSITIVE', '1990-05-15', NOW(), NOW()) ON CONFLICT (email) DO NOTHING;
INSERT INTO patient (name, gender, email, blood_group, birth_date, created_at, updated_at)
VALUES ('Priya Singh', 'Female', 'priya.singh@example.com', 'A_NEGATIVE', '1992-08-22', NOW(), NOW()) ON CONFLICT (email) DO NOTHING;
INSERT INTO patient (name, gender, email, blood_group, birth_date, created_at, updated_at)
VALUES ('Rahul Verma', 'Male', 'rahul.verma@example.com', 'B_POSITIVE', '1988-11-30', NOW(), NOW()) ON CONFLICT (email) DO NOTHING;

-- Insert sample doctors
INSERT INTO doctor (name, specialization, email)
VALUES ('Dr. Anjali Singh', 'Cardiologist', 'anjali.singh@example.com') ON CONFLICT (email) DO NOTHING;
INSERT INTO doctor (name, specialization, email)
VALUES ('Dr. Vikram Kumar', 'Pediatrician', 'vikram.kumar@example.com') ON CONFLICT (email) DO NOTHING;
INSERT INTO doctor (name, specialization, email)
VALUES ('Dr. Pooja Sharma', 'Dermatologist', 'pooja.sharma@example.com') ON CONFLICT (email) DO NOTHING;

-- Add sample appointments (removed ON CONFLICT DO NOTHING as it's not for FK violations)
INSERT INTO appointment (appointment_time, reason, status, patient_id, doctor_id)
VALUES (NOW() + INTERVAL '1 day', 'Routine Checkup', 'SCHEDULED', 1, 1);
INSERT INTO appointment (appointment_time, reason, status, patient_id, doctor_id)
VALUES (NOW() + INTERVAL '2 day', 'Fever and Cold', 'SCHEDULED', 2, 2);
INSERT INTO appointment (appointment_time, reason, status, patient_id, doctor_id)
VALUES (NOW() + INTERVAL '3 day', 'Skin Rash', 'SCHEDULED', 3, 3);
INSERT INTO appointment (appointment_time, reason, status, patient_id, doctor_id)
VALUES (NOW() + INTERVAL '4 day', 'Heart Palpitations', 'SCHEDULED', 1, 1);