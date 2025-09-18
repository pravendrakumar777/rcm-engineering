<!DOCTYPE html>
<html>
<head>
<meta charset = "UTF-8">
    <title>Payslip</title>
    <style>
        body { font-family: Arial, sans-serif; font-size: 12px; }
        h2, h3 { margin: 5px 0; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 10px; }
        td, th { border: 1px solid #000; padding: 5px; }
.no-border td, .no-border th { border: none; }
.header { text-align: left; }
.center { text-align: center; }
.bold { font-weight: bold; }
</style>
</head>
<body>

<!-- Header -->
<table class="no-border">
        <tr>
            <td style="width:20%;">
                <#if logoPath??>
                    <img src="${logoPath}" alt="Company Logo" width="60" height="60"/>
                </#if>
            </td>
            <td style="width:80%;">
                <h2>RCM ENGINEERING & MANUFACTURING</h2>
                <p>
                    KH NO: 513/1, 513/2,<br/>
                    VILL BASAI, NEAR BASAI FLYOVER - GURUGRAM HR. 122001<br/>
                    Mob: 9639200584, 7819929402 | Email: cs29680881@gmail.com<br/>
                    GSTIN: 06ABCDE1234F1Z5 | CIN: U12345UP2020PTC123456
                </p>
            </td>
        </tr>
    </table>

    <!-- Payslip Title -->
    <#if records?size gt 0>
        <h3 class="center">Payslip for the Month of ${monthYear}</h3>
    </#if>
    <p>Payslip Issued Date: ${.now?string("dd-MM-yyyy")}</p>

    <!-- Employee Details -->
    <h3>Employee Details</h3>
    <table class="no-border">
        <tr>
            <td>
                <table>
                    <tr><th>EMP CODE</th><td>${emp.empCode}</td></tr>
                    <tr><th>Name</th><td>${emp.name}</td></tr>
                    <tr><th>Mobile</th><td>${emp.mobile}</td></tr>
                    <tr><th>PAN</th><td>${emp.panNumber}</td></tr>
                    <tr><th>DOB</th><td>${dob}</td></tr>
                    <tr><th>DOJ</th><td>${doj}</td></tr>
                </table>
            </td>
            <td>
                <table>
                    <tr><th>Department</th><td>${emp.department}</td></tr>
                    <tr><th>Designation</th><td>${emp.designation}</td></tr>
                    <tr><th>Manager</th><td>${emp.manager}</td></tr>
                    <tr><th>Bank Name</th><td>${emp.bankName}</td></tr>
                    <tr><th>A/C No.</th><td>${emp.bankAccountNumber}</td></tr>
                    <tr><th>IFSC</th><td>${emp.ifscCode}</td></tr>
                </table>
            </td>
        </tr>
    </table>

    <!-- Salary -->
    <table>
        <tr><th>Monthly Salary (₹)</th><td>₹${emp.salary?string("0.00")} /-</td></tr>
        <tr><th>Net Pay (₹)</th><td>₹${totalSalary?string("0.00")} /-</td></tr>
    </table>

    <!-- Attendance -->
    <h3>Attendance Summary</h3>
    <table>
        <thead>
            <tr>
                <th>Date</th>
                <th>Status</th>
                <th>Check-In</th>
                <th>Check-Out</th>
                <th>Working Hours</th>
            </tr>
        </thead>
        <tbody>
            <#assign presentCount=0 absentCount=0>
            <#list records as att>
                <tr>
                    <td>${att.date}</td>
                    <td>
                        ${att.status}
                        <#if att.status == "PRESENT">
                            <#assign presentCount++>
                        <#elseif att.status == "ABSENT">
                            <#assign absentCount++>
                        </#if>
                    </td>
                    <td>${att.checkIn}</td>
                    <td>${att.checkOut}</td>
                    <td>${att.totalHours}</td>
                </tr>
            </#list>

            <tr>
                <td><b>Status Summary</b></td>
                <td><b>P-${presentCount}d, A-${absentCount}d</b></td>
                <td></td>
                <td class="bold">Total Hours</td>
                <td class="bold">${totalWorkedHours}</td>
            </tr>
        </tbody>
    </table>

</body>
</html>