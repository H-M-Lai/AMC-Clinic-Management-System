# Sample Data Setup

This repository excludes the main record files that contained personal-looking demo data in the local project. To run the application safely in a public clone, create the following files in `data/` with the header row shown below.

## Required Files

`Customer.txt`

```text
customerId,name,icNumber,address,contact,email,password
```

`doctor.txt`

```text
doctorId,name,icNumber,address,contact,email,specialty,password
```

`staff.txt`

```text
staffId,name,icNumber,address,contact,email,password
```

`manager.txt`

```text
managerId,name,icNumber,address,contact,email,password
```

`supermanager.txt`

```text
supermanagerId,name,icNumber,address,contact,email,password
```

`appointment.txt`

```text
appointmentId,staffId,customerId,doctorId,date,time,note,status,type
```

`payment.txt`

```text
paymentId,appointmentId,amount,paymentMethod,date,time,status
```

`feedback.txt`

```text
feedbackId,appointmentId,userType,rating,comment1,comment2
```

`chargeItems.txt`

```text
chargeItemId,appointmentId,serviceId,medicationId,quantity,unitPrice,totalAmount,description
```

## Included Lookup Files

These lookup files are already included in the repository:

- `specialty.txt`
- `serviceType.txt`
- `services.txt`
- `therapeuticClass.txt`
- `medications.txt`

## Recommendation

Use obviously fictional names, emails, phone numbers, IDs, and passwords when creating demo data for testing or screenshots.
