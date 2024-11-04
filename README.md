User Roles and System Capabilities:
1. All Users:
○ Users must log in to the system using their unique hospital ID and a default
password ("password").
○ The system should validate login credentials and provide role-specific access to
the HMS.
○ Users can change their password after their initial login.
○ Users will have roles such as Patient, Doctor, Pharmacist or Administrator.
2. Patient: Information Access:
○ Patients can view their own medical record, which consists of: - Patient ID, Name, Date of Birth, Gender
- Contact Information (e.g., phone number, email address)
- Blood Type
- Past Diagnoses and Treatments
○ Patients can update non-medical personal information such as email address and contact number.
○ Patients are not allowed to modify the past diagnoses, prescribed medications, treatments or blood type..

Appointment Management:
○ Patients can:

● View available appointment slots with doctors.
●Schedule Appointments: Choose a doctor, date, and available time slot
to schedule an appointment.
● Reschedule Appointments: Change an existing appointment to a new
slot, ensuring no conflicts. Upon rescheduling, slot availability will be
updated automatically.
● Cancel Appointments: Cancel an existing appointment. Upon successful
cancellation, slot availability will be updated automatically.
○ Patients can view the status of their scheduled appointments.
○ The status of the appointment will be updated according based on whether the
doctor accepts of decline the appointment requests (eg: confirmed, canceled,
completed)
○ Patients can also view their Appointment Outcome Records of past
appointments.

3. Doctor:

Medical Record Management:
○ Doctors can view the medical records of patients under their care.
○ Doctors can update the medical records of patients by adding new diagnoses,
prescriptions, and treatment plans.

Appointment Management:
○ Doctors can view their personal schedule and set their availability for appointments.
○ Doctors can accept or decline appointment requests.
○ Doctors can view the list of their upcoming appointments.
○ Appointment Outcome Record: After each completed appointment, the doctor
will record the:
● Date of Appointment
● Type of service provided (e.g., consultation, X-ray, blood test etc).
● Any prescribed medications:
- medication name
- status (default is pending)
● Consultation notes

4. Pharmacist:

Prescription Management:
○ Pharmacists can view the Appointment Outcome Record to fulfill medication prescriptions orders from doctors.
○ Pharmacists can update the status of prescription in the Appointment Outcome Record (e.g., pending to dispensed).
○ Pharmacists can monitor the inventory of medications, including tracking stock levels.
○ Pharmacists can submit replenishment requests to administrators when stock levels are low.
5. Administrator: Staff Management:
○ Manage hospital staff (doctors and pharmacists) by adding, updating, or removing staff members.
○ Display a list of staff filtered by role, gender, age, etc.

Appointment Management:
○Administrators can access real-time updates of scheduled appointments.
○ Appointment details should include:
- Patient ID
- Doctor ID
- Appointment status (e.g., confirmed, canceled, completed).
- Date and time of appointment
- Appointment Outcome Record (for completed appointments)
Inventory Management:
○ Administrators can view and manage the inventory of medication including, adding, removing or updating stock levels.
○ Administrators can update the low stock level alert line of each medicine.
○ Administrators can approve replenishment requests from pharmacists. Once the
request is approved, the stock level will be updated automatically.
System initialization:
○ The initial staff list can be imported from a file.
○ The initial patient list can be imported from a file.
○ The initial inventory, including medicine name, initial stock, low stock level alert line can be imported from a file.
User Menus
Each user in the HMS will have a specific menu with options relevant to their role upon log in. Below are the menu options for each user role in the system:
Patient Menu:
● View Medical Record
● Update Personal Information
● View Available Appointment Slots
● Schedule an Appointment
● Reschedule an Appointment
● Cancel an Appointment
● View Scheduled Appointments
● View Past Appointment Outcome Records
● Logout
Doctor Menu:
● View Patient Medical Records
● Update Patient Medical Records
● View Personal Schedule
● Set Availability for Appointments
● Accept or Decline Appointment Requests
● View Upcoming Appointments
● Record Appointment Outcome
● Logout
Pharmacist Menu:
● View Appointment Outcome Record
● Update Prescription Status
● View Medication Inventory
● Submit Replenishment Request
● Logout
Administrator Menu:
● View and Manage Hospital Staff
● View Appointments details
● View and Manage Medication Inventory
● Approve Replenishment Requests
● Logout
