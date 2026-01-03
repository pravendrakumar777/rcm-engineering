<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta charset="UTF-8"/>
<title>Employee Profile</title>
<style>
    body { font-family: Arial, sans-serif; }
    h1 { color: #2d2d2d; text-align: center; }
h3 { margin-top: 20px; color: #333; }
table { width: 100%; border-collapse: collapse; margin-top: 10px; }
    th, td { padding: 8px 12px; border: 1px solid #ccc; text-align: left; }
th { background-color: #f9f9f9; width: 20%; }
td { width: 30%; }
.section-title { background-color: #e0e0e0; font-weight: bold; text-transform: uppercase; padding: 8px; text-align: left; }
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
            <h2>RCM ENGINEERING</h2>
            <p>
                KH NO: 513/1, 513/2,<br/>
                VILL BASAI, NEAR BASAI FLYOVER - GURUGRAM HR. 122001<br/>
                Mob: 9639200584, 7819929402 | Email: cs29680881@gmail.com<br/>
                GSTIN: 06JUUPS6385B1ZT | CIN: U12345UP2020PTC123456
            </p>
        </td>
    </tr>
</table>

<hr/>

<h1>Employee Profile</h1>
<p>Generated on: ${.now?string("dd-MM-yyyy")}</p>

<!-- Employee Information -->
<h3 class="section-title">Employee Information</h3>
<table>
    <tr>
        <th>Name</th>
        <td>${employee.name?html}</td>
        <th>OHR</th>
        <td>${employee.ohr?html}</td>
    </tr>
    <tr>
        <th>Gender</th>
        <td>${employee.gender?html}</td>
        <th>Email</th>
        <td>${employee.email?html}</td>
    </tr>
    <tr>
        <th>Mobile</th>
        <td>${employee.mobile?html}</td>
        <th>Manager</th>
        <td>${employee.manager?html}</td>
    </tr>
    <tr>
        <th>DOB</th>
        <td>${dob?html}</td>
        <th>DOJ</th>
        <td>${doj?html}</td>
    </tr>
</table>

<!-- Address -->
<h3 class="section-title">Address</h3>
<table>
    <tr>
        <th>Village</th>
        <td>${employee.address?html}</td>
        <th>City</th>
        <td>${employee.city?html}</td>
    </tr>
    <tr>
        <th>State</th>
        <td>${employee.state?html}</td>
        <th>Postal Code</th>
        <td>${employee.postalCode?html}</td>
    </tr>
    <tr>
        <th>Country</th>
        <td colspan="3">${employee.country?html}</td>
    </tr>
</table>

<!-- Job Details -->
<h3 class="section-title">Job Details</h3>
<table>
    <tr>
        <th>Department</th>
        <td>${employee.department?html}</td>
        <th>Designation</th>
        <td>${employee.designation?html}</td>
    </tr>
</table>

<!-- Financial Details -->
<h3 class="section-title">Financial Details</h3>
<table>
    <tr>
        <th>PAN</th>
        <td>${employee.panNumber?html}</td>
        <th>Aadhaar</th>
        <td>${employee.aadhaarNumber?html}</td>
    </tr>
    <tr>
        <th>Bank Name</th>
        <td>${employee.bankName?html}</td>
        <th>A/C No.</th>
        <td>${employee.bankAccountNumber?html}</td>
    </tr>
    <tr>
        <th>IFSC Code</th>
        <td>${employee.ifscCode?html}</td>
        <th>Salary</th>
        <td>₹ ${employee.salary?string("0.00")}</td>
    </tr>
</table>

</body>
</html>