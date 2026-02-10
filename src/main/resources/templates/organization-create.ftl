<!DOCTYPE html>
<html>
<head>
<title>Create Organization</title>

<link rel="stylesheet"
href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

<style>
* { box-sizing: border-box; }

body {
font-family: 'Segoe UI', Arial;
background: linear-gradient(135deg,#e3f2fd,#f8f9fa);
padding: 30px;
}

.card {
background: white;
max-width: 900px;
margin: auto;
padding: 30px;
border-radius: 12px;
box-shadow: 0 8px 30px rgba(0,0,0,0.1);
}

h3 {
text-align: center;
margin-bottom: 25px;
}

.grid {
display: grid;
grid-template-columns: 1fr 1fr;
gap: 15px;
}

.input-group {
position: relative;
}

.input-group i {
position: absolute;
top: 12px;
left: 12px;
color: #777;
}

input, select {
width: 100%;
padding: 12px 12px 12px 38px;
border: 1px solid #ccc;
border-radius: 6px;
outline: none;
}

input:focus, select:focus {
border-color: #007bff;
}

.buttons {
margin-top: 25px;
display: flex;
gap: 10px;
}

button {
flex: 1;
padding: 12px;
border: none;
border-radius: 6px;
cursor: pointer;
font-size: 15px;
}

.btn-save { background:#28a745; color:white; }
.btn-back { background:#6c757d; color:white; }

@media(max-width:700px){
.grid { grid-template-columns: 1fr; }
}
</style>
</head>

<body>

<div class="card">

<h3><i class="bi bi-building-add"></i> Create Organization</h3>

<div class="grid">

<div class="input-group">
<i class="bi bi-building"></i>
<input id="organizationName" placeholder="Organization">
</div>

<div class="input-group">
<i class="bi bi-envelope"></i>
<input id="email" placeholder="Email">
</div>

<div class="input-group">
<i class="bi bi-telephone"></i>
<input id="phone" placeholder="Phone">
</div>

<div class="input-group">
<i class="bi bi-geo"></i>
<input id="address" placeholder="Address">
</div>

<div class="input-group">
<i class="bi bi-diagram-3"></i>
<select id="industryType">
<option value="">Industry Type</option>
    <option>IT & Software Services</option>
    <option>Mechanical & Engineering</option>
    <option>Consulting & Corporate</option>
    <option>Finance Banking & Fintech</option>
    <option>Healthcare & Pharma, Biotech</option>
    <option>Manufacturing & Industrial</option>
    <option>Construction & Real Estate</option>
    <option>Investment & Venture Capital</option>
    <option>Retail & E-Commerce</option>
    <option>Logistics & Supply Chain</option>
    <option>Media & Digital Platforms</option>
    <option>Telecommunications</option>
    <option>Aerospace & Defense</option>
    <option>Automobile & EV</option>
    <option>Energy & Power</option>
    <option>Insurance</option>
</select>
</div>

<div class="input-group">
<i class="bi bi-tags"></i>
<input id="industry" placeholder="Industry">
</div>

<div class="input-group">
<i class="bi bi-credit-card"></i>
<input id="panTaxId" placeholder="PAN / TAX ID">
</div>

<div class="input-group">
<i class="bi bi-calendar"></i>
<input id="foundedYear" placeholder="Founded Year">
</div>

<div class="input-group">
<i class="bi bi-person"></i>
<input id="foundedBy" placeholder="Founded By">
</div>

<div class="input-group">
<i class="bi bi-building"></i>
<input id="city" placeholder="City">
</div>

<div class="input-group">
<i class="bi bi-globe"></i>
<input id="country" placeholder="Country">
</div>

<div class="input-group">
<i class="bi bi-link-45deg"></i>
<input id="website" placeholder="Website">
</div>

</div>

<div class="buttons">
<button class="btn-save" onclick="saveOrganization()">Save</button>
<button class="btn-back" onclick="goBack()">Back</button>
</div>

</div>

<script>
const BASE = "/api/organizations";

function saveOrganization() {

const org = {
organizationName: val("organizationName"),
email: val("email"),
phone: val("phone"),
address: val("address"),
industryType: val("industryType"),
industry: val("industry"),
registrationNumber: val("registrationNumber"),
panTaxId: val("panTaxId"),
foundedYear: val("foundedYear"),
foundedBy: val("foundedBy"),
city: val("city"),
country: val("country"),
website: val("website"),
status: "ACTIVE"
};

fetch(BASE + "/create", {
method:"POST",
headers:{"Content-Type":"application/json"},
body: JSON.stringify(org)
})
.then(r=>r.json())
.then(()=>{
alert("Organization Created Successfully");
window.location.href="/organizations";
});
}

function val(id){ return document.getElementById(id).value; }

function goBack(){
window.location.href="/organizations";
}
</script>
</body>
</html>