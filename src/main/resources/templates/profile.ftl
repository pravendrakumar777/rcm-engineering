<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta charset="UTF-8"/>
    <title>Employee Profile</title>
    <style>
        body { font-family: Arial, sans-serif; }
        h1 { color: #2d2d2d; }
table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { padding: 8px 12px; border: 1px solid #ccc; text-align: left; }
th { background-color: #f4f4f4; }
.section-title { background-color: #e0e0e0; font-weight: bold; text-transform: uppercase; padding: 8px; }
.no-border td { border: none; }
.logo { margin-right: 10px; }
</style>
</head>
<body>

<!-- Company Header Section -->
<table class="no-border">
    <tr>
        <td style="width:20%; vertical-align: top;">
            <img src="data:image/png;base64,${logoBase64}" alt="Company Logo" class="logo" width="150" height="150"/>
        </td>
        <td style="width:80%;">
            <h2>RCM ENGINEERING &amp; MANUFACTURING</h2>
            <p>
                KH NO: 513/1, 513/2,<br/>
                VILL BASAI, NEAR BASAI FLYOVER - GURUGRAM HR. 122001<br/>
                Mob: 9639200584, 7819929402 | Email: cs29680881@gmail.com<br/>
                GSTIN: 06ABCDE1234F1Z5 | CIN: U12345UP2020PTC123456
            </p>
        </td>
    </tr>
</table>

<hr/>

<!-- Existing Content -->
<h1>Employee Profile</h1>
<p><strong>Generated on:</strong> ${generatedOn?html}</p>

<table>
    <tr><th class="section-title" colspan="2">Basic Details</th></tr>
    <tr><th>Employee Code</th><td>${employee.empCode?html}</td></tr>
    <tr><th>Name</th><td>${employee.name?html}</td></tr>
    <tr><th>Gender</th><td>${employee.gender?html}</td></tr>
    <tr><th>Email</th><td>${employee.email?html}</td></tr>
    <tr><th>Mobile</th><td>${employee.mobile?html}</td></tr>
    <tr><th>Date of Birth</th><td>${dob?html}</td></tr>
    <tr><th>Manager</th><td>${employee.manager?html}</td></tr>

    <tr><th class="section-title" colspan="2">Address</th></tr>
    <tr><th>Address</th><td>${employee.address?html}</td></tr>
    <tr><th>City</th><td>${employee.city?html}</td></tr>
    <tr><th>State</th><td>${employee.state?html}</td></tr>
    <tr><th>Postal Code</th><td>${employee.postalCode?html}</td></tr>
    <tr><th>Country</th><td>${employee.country?html}</td></tr>

    <tr><th class="section-title" colspan="2">Job Details</th></tr>
    <tr><th>Department</th><td>${employee.department?html}</td></tr>
    <tr><th>Designation</th><td>${employee.designation?html}</td></tr>
    <tr><th>Date of Joining</th><td>${doj?html}</td></tr>

    <tr><th class="section-title" colspan="2">Financial Details</th></tr>
    <tr><th>PAN Number</th><td>${employee.panNumber?html}</td></tr>
    <tr><th>Aadhaar Number</th><td>${employee.aadhaarNumber?html}</td></tr>
    <tr><th>Bank Name</th><td>${employee.bankName?html}</td></tr>
    <tr><th>Account Number</th><td>${employee.bankAccountNumber?html}</td></tr>
    <tr><th>IFSC Code</th><td>${employee.ifscCode?html}</td></tr>
    <tr><th>Salary</th><td>${employee.salary?string("0.00")}</td></tr>
</table>

</body>
</html>