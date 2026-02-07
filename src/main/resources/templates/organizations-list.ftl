<!DOCTYPE html>
<html>
<head>
<title>Organizations</title>

<style>
body { font-family: Arial; background: #f2f4f8; }
.container { width: 90%; margin: 30px auto; }

.header {
display: flex;
align-items: center;
justify-content: space-between;
position: relative;
}
.header h2 {
position: absolute;
left: 50%;
transform: translateX(-50%);
margin: 0;
}

button {
padding: 8px 14px;
border: none;
border-radius: 5px;
cursor: pointer;
}
.btn-add { background: #28a745; color: white; }
.btn-view { background: #007bff; color: white; }

table {
width: 100%;
border-collapse: collapse;
background: white;
margin-top: 20px;
}
th, td {
padding: 12px;
border-bottom: 1px solid #ddd;
}
th { background: #343a40; color: white; }

.modal-bg {
display: none;
position: fixed;
inset: 0;
background: rgba(0,0,0,0.45);
backdrop-filter: blur(3px);
z-index: 999;
}
.modal-box {
width: 700px;
max-height: 85vh;
overflow-y: auto;
background: #ffffff;
border-radius: 12px;
margin: 60px auto;
padding: 25px 30px;
box-shadow: 0 10px 40px rgba(0,0,0,0.2);
animation: popIn .25s ease;
}
@keyframes popIn {
from {transform: scale(.9); opacity:0;}
to {transform: scale(1); opacity:1;}
}
.details-grid {
display: grid;
grid-template-columns: 1fr 1fr;
gap: 14px 25px;
margin-top: 15px;
}
.details-item {
background: #f7f9fc;
padding: 10px 12px;
border-radius: 6px;
font-size: 14px;
}
.details-item b {
color: #333;
}
.close-btn {
margin-top: 20px;
width: 100%;
padding: 10px;
background: #007bff;
color: white;
border: none;
border-radius: 6px;
cursor: pointer;
}
@media(max-width:720px){
.modal-box { width: 95%; }
.details-grid { grid-template-columns: 1fr; }
}

</style>
</head>

<body>
<div class="container">
<div class="header">
    <a href="/organizations/create">
        <button class="btn-add">+ Create Organization</button>
    </a>
    <h2>Organization List</h2>
    <div style="width:150px;"></div>
</div>

<table>
<thead>
<tr>
<th>ORG ID</th>
<th>Name</th>
<th>Email</th>
<th>Phone</th>
<th>Status</th>
<th>Industry</th>
<th>City</th>
<th>Created</th>
<th>Action</th>
</tr>
</thead>
<tbody id="orgTable"></tbody>
</table>
</div>

<#noparse>
<script>
const BASE = "/api/organizations";
window.onload = loadOrganizations;

    function setValue(id, value) {
    const el = document.getElementById(id);
        if (el) el.innerText = value || '';
    }

        function loadOrganizations() {
        fetch(BASE + "/list", { headers: { "Cache-Control": "no-cache" } })
        .then(r => r.json())
            .then(data => {
            let rows = "";
            data.forEach(o => {
            rows += `
            <tr>
            <td>${o.organizationId || ''}</td>
                <td>${o.organizationName || ''}</td>
                <td>${o.email || ''}</td>
                <td>${o.phone || ''}</td>
                <td>${o.status || ''}</td>
                <td>${o.industryType || ''}</td>
                <td>${o.city || ''}</td>
                <td>${o.createdDate ? formatDate(o.createdDate) : ''}</td>
            <td>
            <button class="btn-view" onclick="viewOrg(${o.id})">View</button>
            </td>
            </tr>`;
            });
            document.getElementById("orgTable").innerHTML = rows;
            });
        }

        function viewOrg(id) {
        fetch(BASE + "/fetch/" + id)
        .then(r => r.json())
        .then(o => {

            setValue("mId", o.organizationId);
            setValue("mName", o.organizationName);
            setValue("mEmail", o.email);
            setValue("mPhone", o.phone);
            setValue("mAddress", o.address);
            setValue("mStatus", o.status);
            setValue("mIndustryType", o.industryType);
            setValue("mIndustry", o.industry);
            setValue("mRegistrationNumber", o.registrationNumber);
            setValue("mPanTaxId", o.panTaxId);
            setValue("mFoundedYear", o.foundedYear);
            setValue("mFoundedBy", o.foundedBy);
            setValue("mCity", o.city);
            setValue("mCountry", o.country);
            setValue("mWebsite", o.website);
            setValue("mCreatedDate", o.createdDate ? formatDate(o.createdDate) : '');
            document.getElementById("orgModal").style.display = "block";
            });
        }

    function closeModal() {
    document.getElementById("orgModal").style.display = "none";
    }

    function formatDate(dateString) {
    const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Month is 0-based
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
    }
</script>
</#noparse>

    <!-- POPUP MODAL -->
    <div id="orgModal" class="modal-bg">
<div class="modal-box">

<h3>Organization Details</h3>

        <div class="details-grid">
            <div class="details-item"><b>Org ID:</b> <span id="mId"></span></div>
                <div class="details-item"><b>Name:</b> <span id="mName"></span></div>
                <div class="details-item"><b>Email:</b> <span id="mEmail"></span></div>
                <div class="details-item"><b>Phone:</b> <span id="mPhone"></span></div>
                <div class="details-item"><b>Address:</b> <span id="mAddress"></span></div>
                <div class="details-item"><b>Status:</b> <span id="mStatus"></span></div>
                <div class="details-item"><b>Industry Type:</b> <span id="mIndustryType"></span></div>
                <div class="details-item"><b>Industry:</b> <span id="mIndustry"></span></div>
                <div class="details-item"><b>Registration No:</b> <span id="mRegistrationNumber"></span></div>
                <div class="details-item"><b>PAN / TAX:</b> <span id="mPanTaxId"></span></div>
                <div class="details-item"><b>Founded Year:</b> <span id="mFoundedYear"></span></div>
                <div class="details-item"><b>Founded By:</b> <span id="mFoundedBy"></span></div>
                <div class="details-item"><b>City:</b> <span id="mCity"></span></div>
                <div class="details-item"><b>Country:</b> <span id="mCountry"></span></div>
                <div class="details-item"><b>Website:</b> <span id="mWebsite"></span></div>
                <div class="details-item"><b>Created Date:</b> <span id="mCreatedDate"></span></div>
            </div>
            <button class="close-btn" onclick="closeModal()">Close</button>
            </div>
        </div>
</body>
</html>