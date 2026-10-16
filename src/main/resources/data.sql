-- Sample data, loaded only on first startup (guarded by the ems_seed table).

INSERT INTO departments (name)
SELECT v.name FROM (VALUES ('Engineering'), ('Human Resources'), ('Finance'), ('Sales')) AS v(name)
WHERE NOT EXISTS (SELECT 1 FROM ems_seed);

INSERT INTO employees (first_name, last_name, email, phone, job_title, salary, hire_date, department_id)
SELECT v.first_name, v.last_name, v.email, v.phone, v.job_title, v.salary, v.hire_date, d.id
FROM (VALUES
  ('Aarav',  'Sharma', 'aarav.sharma@example.com', '9876543210', 'Senior Developer',  1450000, DATE '2021-03-15', 'Engineering'),
  ('Priya',  'Reddy',  'priya.reddy@example.com',  '9876500011', 'Tech Lead',         2100000, DATE '2019-07-01', 'Engineering'),
  ('Rahul',  'Verma',  'rahul.verma@example.com',  '9876500022', 'QA Engineer',        850000, DATE '2023-01-09', 'Engineering'),
  ('Sneha',  'Iyer',   'sneha.iyer@example.com',   '9876500033', 'DevOps Engineer',   1200000, DATE '2022-05-23', 'Engineering'),
  ('Kavya',  'Nair',   'kavya.nair@example.com',   '9876500044', 'HR Manager',        1100000, DATE '2020-02-17', 'Human Resources'),
  ('Arjun',  'Patel',  'arjun.patel@example.com',  '9876500055', 'Recruiter',          650000, DATE '2024-04-01', 'Human Resources'),
  ('Meera',  'Joshi',  'meera.joshi@example.com',  '9876500066', 'Financial Analyst',  950000, DATE '2021-11-30', 'Finance'),
  ('Vikram', 'Singh',  'vikram.singh@example.com', '9876500077', 'Accountant',         700000, DATE '2022-08-08', 'Finance'),
  ('Ananya', 'Gupta',  'ananya.gupta@example.com', '9876500088', 'Sales Manager',     1300000, DATE '2020-10-12', 'Sales'),
  ('Rohan',  'Das',    'rohan.das@example.com',    '9876500099', 'Account Executive',  600000, DATE '2025-06-02', 'Sales')
) AS v(first_name, last_name, email, phone, job_title, salary, hire_date, dept)
JOIN departments d ON d.name = v.dept
WHERE NOT EXISTS (SELECT 1 FROM ems_seed);

INSERT INTO ems_seed (seeded_at)
SELECT CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ems_seed);
