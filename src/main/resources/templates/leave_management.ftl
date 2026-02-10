<!DOCTYPE html>
<html>
<head>
<title>Leave Policy & Absence</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f4f6f9;
        margin: 0;
        padding: 20px;
    }

    h2 { margin-bottom: 15px; }

        .card {
            background: #fff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px 30px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }
        .form-group.full {
            grid-column: span 2;
        }

        label {
            font-weight: bold;
            margin-bottom: 6px;
        }

        input, select {
            padding: 10px;
            border-radius: 6px;
            border: 1px solid #ccc;
        }

        .btn {
            padding: 10px 16px;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            font-weight: bold;
        }

        .btn-apply {
            background: #28a745;
            color: white;
            margin-top: 20px;
        }
        .btn-approve {
            background: #007bff;
            color: white;
        }
        .btn-reject {
            background: #dc3545;
            color: white;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }
        th {
            background: #f0f0f0;
        }

        .status-approved {
            color: green;
            font-weight: bold;
        }
        .status-rejected {
            color: red;
            font-weight: bold;
        }
        .status-pending {
            color: orange;
            font-weight: bold;
        }
</style>
</head>

<body>

<!-- APPLY LEAVE -->
<div class="card">
<h2>Apply Leave</h2>

<form onsubmit="applyLeave(event)">
    <div class="form-grid">

    <div class="form-group">
    <label>OHR ID</label>
    <input type="text" id="ohrId" required>
    </div>

    <div class="form-group">
    <label>Employee Name</label>
    <input type="text" id="empName" required>
    </div>

    <div class="form-group">
    <label>Leave Type</label>
    <select id="leaveType">
    <option>Casual</option>
    <option>Sick</option>
    <option>Earned</option>
    <option>WFH</option>
    </select>
    </div>

    <div class="form-group">
    <label>From Date</label>
    <input type="date" id="fromDate" required>
    </div>

    <div class="form-group">
    <label>To Date</label>
    <input type="date" id="toDate" required>
    </div>

    <div class="form-group full">
    <label>Reason</label>
    <input type="text" id="reason">
    </div>

    </div>

    <button class="btn btn-apply" type="submit">Apply Leave</button>
    </form>
    </div>

        <!-- LEAVE LIST -->
        <div class="card">
        <h2>Leave Requests</h2>
            <table>
            <thead>
            <tr>
                <th>OHR</th>
                <th>Name</th>
                <th>Type</th>
                <th>From</th>
                <th>To</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody id="leaveTable"></tbody>
            </table>
        </div>

<#noparse>
<script>
    const BASE = "/api/leaves";

    function applyLeave(e){
            e.preventDefault();

            const payload = {
                ohrId: document.getElementById("ohrId").value,
                employeeName: document.getElementById("empName").value,
                leaveType: document.getElementById("leaveType").value,
                fromDate: document.getElementById("fromDate").value,
                toDate: document.getElementById("toDate").value,
                reason: document.getElementById("reason").value
            };

        fetch(BASE + "/apply", {
            method: "POST",
            headers: {"Content-Type":"application/json"},
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(() => {
                alert("Leave Applied Successfully!");
                document.getElementById("ohrId").value="";
                document.getElementById("empName").value="";
                document.getElementById("reason").value="";
                loadLeaves();
            });
        }

        function loadLeaves(){
            fetch(BASE + "/list")
            .then(r => r.json())
            .then(data => {

            if(!data || data.length===0){
                document.getElementById("leaveTable").innerHTML =
                "<tr><td colspan='7'>No Leaves Found</td></tr>";
            return;
        }

    let rows="";

        data.forEach(l=>{
            let statusClass="status-pending";
            if(l.status==="APPROVED") statusClass="status-approved";
            if(l.status==="REJECTED") statusClass="status-rejected";

        rows+=`
        <tr>
            <td>${l.ohrId}</td>
            <td>${l.employeeName}</td>
            <td>${l.leaveType}</td>
            <td>${l.fromDate}</td>
            <td>${l.toDate}</td>
            <td class="${statusClass}">${l.status}</td>
            <td>
            <button onclick="approve(${l.id})">Approve</button>
            <button onclick="reject(${l.id})">Reject</button>
            </td>
        </tr>`;
        });

        document.getElementById("leaveTable").innerHTML=rows;
        });
    }

        function approve(id){
            fetch(BASE+"/approve/"+id,{method:"PUT"}).then(loadLeaves);
        }

        function reject(id){
            fetch(BASE+"/reject/"+id,{method:"PUT"}).then(loadLeaves);
        }

    window.onload=loadLeaves;
</script>
</#noparse>
</body>
</html>