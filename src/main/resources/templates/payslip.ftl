<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>Payslip</title>
    <style>
        body {
font-family: Arial, sans-serif;
margin: 30px;
}
.header {
text-align: center;
}
.header img {
height: 60px;
}
.title {
font-size: 22px;
font-weight: bold;
margin-top: 10px;
margin-bottom: 20px;
}
.section {
margin-bottom: 20px;
}
table {
width: 100%;
border-collapse: collapse;
margin-top: 8px;
}
th, td {
border: 1px solid #444;
padding: 6px;
text-align: center;
font-size: 14px;
}
th {
background: #f0f0f0;
}
.summary td {
text-align: left;
}
</style>
</head>
<body>
<div class="header">
    <img src="${logoPath}" alt="Company Logo"/>
    <div class="title">Payslip - ${monthYear}</div>
</div>

<div class="section">
    <h3>Employee Information</h3>
    <table>
        <tr>
            <td><b>Name</b></td>
            <td>${emp.name}</td>
        </tr>
        <tr>
            <td><b>Email</b></td>
            <td>${emp.email}</td>
        </tr>
        <tr>
            <td><b>Mobile</b></td>
            <td>${emp.mobile}</td>
        </tr>
        <tr>
            <td><b>Gender</b></td>
            <td>${emp.gender}</td>
        </tr>
        <tr>
            <td><b>Date of Birth</b></td>
            <td>${dob}</td>
        </tr>
        <tr>
            <td><b>Date of Joining</b></td>
            <td>${doj}</td>
        </tr>
    </table>
</div>

<div class="section">
    <h3>Attendance Records</h3>
    <table>
        <thead>
        <tr>
            <th>Date</th>
            <th>Status</th>
            <th>Check-In</th>
            <th>Check-Out</th>
            <th>Total Hours</th>
        </tr>
        </thead>
        <tbody>
        <#list records as rec>
            <tr>
                <td>${rec.formattedDate}</td>
                <td>${rec.status}</td>
                <td>${rec.formattedCheckIn}</td>
                <td>${rec.formattedCheckOut}</td>
                <td>${rec.formattedTotalHours}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<div class="section">
    <h3>Summary</h3>
    <table class="summary">
        <tr>
            <td><b>Present Days</b></td>
            <td>${presentDays}</td>
        </tr>
        <tr>
            <td><b>Absent Days</b></td>
            <td>${absentDays}</td>
        </tr>
        <tr>
            <td><b>Total Hours Worked</b></td>
            <td>${totalHours}</td>
        </tr>
        <tr>
            <td><b>Total Salary</b></td>
            <td>${totalSalary}</td>
        </tr>
        <tr>
            <td><b>Issued Date</b></td>
            <td>${issuedDate}</td>
        </tr>
    </table>
</div>

<div class="section" style="margin-top:40px;">
    <table style="border:none;">
        <tr style="border:none;">
            <td style="border:none; text-align:left;">
                <b>Employee Signature</b>
                <br/><br/>____________________
            </td>
            <td style="border:none; text-align:right;">
                <b>Authorized Signature</b>
                <br/><br/>____________________
            </td>
        </tr>
    </table>
</div>
</body>
</html>