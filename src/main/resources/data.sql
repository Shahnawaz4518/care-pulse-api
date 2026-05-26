-- data.sql: Seed data loaded automatically by Spring Boot on startup.
-- Controlled by: spring.sql.init.mode=always and spring.sql.init.data-locations in application.properties
-- This runs AFTER Hibernate creates the schema (spring.jpa.defer-datasource-initialization=true)

INSERT INTO patient (name, gender, birth_date, email, blood_group)
VALUES
     ('Amit Sharma',  'Male',   '1990-05-14', 'amit.sharma@gmail.com',  'B_POSITIVE'),
     ('Priya Verma',  'Female', '1995-05-14', 'priya.verma@gmail.com',  'A_POSITIVE'),
     ('Rahul Singh',  'Male',   '1988-12-03', 'rahul.singh@gmail.com',  'O_POSITIVE'),
     ('Sneha Patil',  'Female', '1992-03-17', 'sneha.patil@gmail.com',  'AB_POSITIVE'),
     ('Vikram Joshi', 'Male',   '1985-07-09', 'vikram.joshi@gmail.com', 'O_NEGATIVE'),
     ('Neha Gupta',   'Female', '1998-11-25', 'neha.gupta@gmail.com',   'A_NEGATIVE'),
     ('Rohan Mehta',  'Male',   '1993-02-10', 'rohan.mehta@gmail.com',  'B_NEGATIVE'),
     ('Kavita Nair',  'Female', '1993-02-10', 'kavita.nair@gmail.com',  'AB_NEGATIVE'),
     ('Aarav Sharma', 'Male',   '1991-06-21', 'aarav.sharma@gmail.com', 'A_NEGATIVE');
-- FIX: The original file had a semicolon after row 8 ('Kavita Nair') which ended
-- the INSERT early, making row 9 ('Aarav Sharma') a dangling/orphaned SQL fragment.
-- Also fixed: Aarav's email was a duplicate of Kavita's (email has UNIQUE constraint).

INSERT INTO doctor (name, specialization, email)
VALUES
    ('Dr. Rajesh Yadav', 'Cardiology', 'rajeshyadav@email.com'),
    ('Dr. Sneha Gupta', 'Dermatology', 'snehaGupta@email.com'),
    ('Dr. Amit Sharma', 'Neurology', 'amitSharma@email.com'),
    ('Dr. Virat Kohli', 'Orthopaedics', 'viratKohli@email.com');