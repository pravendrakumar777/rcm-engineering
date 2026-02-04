<html>
<head>
<style>
body { font-family: Arial, sans-serif; font-size: 10pt; }
.header-table { width: 100%; margin-bottom: 10px; }
.company-info { font-size: 8pt; }
h2 { margin: 0; }
table { border-collapse: collapse; width: 100%; margin-top: 10px; table-layout: fixed; }
th, td { border: 1px solid black; padding: 5px; font-size: 9pt; word-wrap: break-word; }
th { font-weight: bold; text-align: center; }
td { text-align: center; }
.right { text-align: right; }
.left { text-align: left; }
.section-title { font-weight: bold; margin-top: 15px; }
.notes { font-size: 9pt; margin-top: 5px; }
.logo { width: 120px; height: 120px; object-fit: contain; }
</style>
</head>
<body>

<!-- Header with Logo and Company Name -->
<table style="width:100%; margin-bottom:10px; border-collapse: collapse; border: none;">
    <tr>
        <td style="width:20%; vertical-align: top; border: none; padding: 0;">
            <img src="data:image/png;base64,${logoBase64}"
                 alt="Company Logo"
                 style="width:150px; height:150px; object-fit: contain;"/>
        </td>
        <td style="width:80%; vertical-align: top; text-align:center; border: none; padding: 0;">
            <h2 style="margin:0;">Niiran Software Solutions</h2>
            <div style="font-size:8pt; line-height: 1.2;">
                KH NO: 513/1, 513/2, VILL BASAI, NEAR BASAI FLYOVER - GURUGRAM HR. 122001<br>
                Mob: 9639200584, 7819929402 | Email: cs29680881@gmail.com<br>
                GSTIN: 06JUUPS6385B1ZT | CIN: U12345UP2020PTC123456
            </div>
        </td>
    </tr>
</table>

<p><b>Challan No :</b> ${challan.challanNo}</p>
<p><b>Challan Ref No :</b> ${challan.refChNo}</p>
<p><b>Issued Date :</b> ${formattedDate}</p>
<p><b>Last Modified Date :</b> ${modifiedFormatted}</p>
<p><b>Vendor Partner’s :</b> ${challan.customerName}</p>

<table>
        <thead>
            <tr>
                <th style="width:6%;">S. No</th>
                <th style="width:12%;">Item Name</th>
                <th style="width:17%; white-space: nowrap;">Process (Job Work)</th>
                <th style="width:12%;">Added At</th>
                <th style="width:6%;">HSN</th>
                <th style="width:6%;">Unit</th>
                <th style="width:10%; white-space: nowrap;">Weight</th>
                <th style="width:10%; white-space: nowrap;">Pcs/Kg</th>
                <th style="width:6%;">Rate/Pcs</th>
                <th style="width:11%; white-space: nowrap;">Total Pcs</th>
                <th style="width:10%;">Amount</th>
            </tr>
        </thead>

    <tbody>
        <#list challan.items as item>
        <tr>
            <td>${item_index + 1}</td>
            <td class="left">${item.description!""}</td>
            <td class="left">${item.process!""}</td>
            <td class="right">${item.formattedAddedAt!""}</td>
            <td>${item.hsnCode!""}</td>
            <td>${item.unit!""}</td>
            <td class="right">${item.weight!""}</td>
            <td class="right">${item.piecesPerKg}</td>
            <td class="right">${item.ratePerPiece?string("0.00")}</td>
            <td class="right">${item.totalPieces}</td>
            <td class="right">${item.totalAmount?string("0.00")}</td>
        </tr>
        </#list>
        <tr>
            <td colspan="10" class="right"><b>Grand Total</b></td>
            <td class="right"><b>${grandTotal?string("0.00")}</b></td>
        </tr>
    </tbody>
</table>

<!-- Transport / Dispatch Details -->
<p class="section-title">Transport / Dispatch Details:</p>
<p><b>Transporter:</b> ${challan.transporter!""}</p>
<p><b>Vehicle No.:</b> ${challan.vehicleNo!""}</p>
<p><b>Dispatch From:</b> RCM ENGINEERING, GURUGRAM</p>
<p><b>Dispatch To:</b> ${challan.customerName!""}</p>

<!-- Notes -->
<p class="section-title">Notes:</p>
<div class="notes">
    1. This challan is issued for job work purpose only.<br>
    2. The goods belong to ${challan.customerName!""}.<br>
    3. RCM ENGINEERING has carried out machining processes (Tapping, Turning, Learning, Drilling, Chamfering, etc.).<br>
    4. No transfer of ownership of goods is involved.
</div>

<br><br>
<p>Prepared By: _____________________</p>
<p>Authorised Signatory (RCM): ____________________</p>
<p style="text-align: right;"><b>For : RCM ENGINEERING</b></p>

</body>
</html>