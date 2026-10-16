-- Optional sample data. Run after schema.sql.

INSERT INTO departments (name) VALUES ('Engineering');
INSERT INTO departments (name) VALUES ('Human Resources');
INSERT INTO departments (name) VALUES ('Finance');
INSERT INTO departments (name) VALUES ('Sales');

INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Aarav', 'Sharma', 'aarav.sharma@example.com', '9876543210', 'Senior Developer', 1450000, DATE '2021-03-15',
        (SELECT id FROM departments WHERE name = 'Engineering'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Priya', 'Reddy', 'priya.reddy@example.com', '9876500011', 'Tech Lead', 2100000, DATE '2019-07-01',
        (SELECT id FROM departments WHERE name = 'Engineering'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Rahul', 'Verma', 'rahul.verma@example.com', '9876500022', 'QA Engineer', 850000, DATE '2023-01-09',
        (SELECT id FROM departments WHERE name = 'Engineering'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Sneha', 'Iyer', 'sneha.iyer@example.com', '9876500033', 'DevOps Engineer', 1200000, DATE '2022-05-23',
        (SELECT id FROM departments WHERE name = 'Engineering'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Kavya', 'Nair', 'kavya.nair@example.com', '9876500044', 'HR Manager', 1100000, DATE '2020-02-17',
        (SELECT id FROM departments WHERE name = 'Human Resources'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Arjun', 'Patel', 'arjun.patel@example.com', '9876500055', 'Recruiter', 650000, DATE '2024-04-01',
        (SELECT id FROM departments WHERE name = 'Human Resources'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Meera', 'Joshi', 'meera.joshi@example.com', '9876500066', 'Financial Analyst', 950000, DATE '2021-11-30',
        (SELECT id FROM departments WHERE name = 'Finance'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Vikram', 'Singh', 'vikram.singh@example.com', '9876500077', 'Accountant', 700000, DATE '2022-08-08',
        (SELECT id FROM departments WHERE name = 'Finance'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Ananya', 'Gupta', 'ananya.gupta@example.com', '9876500088', 'Sales Manager', 1300000, DATE '2020-10-12',
        (SELECT id FROM departments WHERE name = 'Sales'));
INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
VALUES ('Rohan', 'Das', 'rohan.das@example.com', '9876500099', 'Account Executive', 600000, DATE '2025-06-02',
        (SELECT id FROM departments WHERE name = 'Sales'));

COMMIT;
