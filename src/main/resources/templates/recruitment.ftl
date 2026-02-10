<!DOCTYPE html>
<html>
<head>
<title>Recruitment & Interview</title>

<style>
body {
font-family: Arial;
background:#f4f6f9;
padding:20px;
}

.card {
background:white;
padding:20px;
border-radius:8px;
margin-bottom:25px;
box-shadow:0 2px 8px #ddd;
}

.grid {
display:grid;
grid-template-columns:1fr 1fr;
gap:12px;
}

input {
padding:8px;
border:1px solid #ccc;
border-radius:5px;
width:100%;
}

button {
padding:6px 10px;
border:none;
border-radius:5px;
cursor:pointer;
font-size:13px;
}

.add { background:#28a745; color:white; }
.short { background:#ffc107; }
.sel { background:#007bff; color:white; }
.rej { background:#dc3545; color:white; }

table {
width:100%;
border-collapse:collapse;
font-size:13px;
}

th,td {
padding:6px;
border-bottom:1px solid #ddd;
text-align:left;
}

th { background:#f0f0f0; }
</style>
</head>

<body>

<!-- ADD CANDIDATE -->
<div class="card">
<h2>Add Candidate</h2>

<form onsubmit="addCandidate(event)">
<div class="grid">
<input id="name" placeholder="Name" required>
<input id="email" placeholder="Email">
<input id="phone" placeholder="Phone">
<input id="position" placeholder="Position">
<input id="experience" placeholder="Experience">
<input id="skills" placeholder="Skills">
<input id="interviewDate" type="datetime-local">
</div>
<br>
<button class="add">Add Candidate</button>
</form>
</div>

<!-- LIST -->
<div class="card">
<h2>Candidate List</h2>

<table>
<thead>
<tr>
<th>Name</th>
<th>Position</th>
<th>Skills</th>
<th>Interview</th>
<th>Status</th>
<th>Action</th>
</tr>
</thead>
<tbody id="tbl">
<tr><td colspan="6">Loading...</td></tr>
</tbody>
</table>
</div>

<!-- VERY IMPORTANT FOR FTL -->
<#noparse>
<script>
const BASE="/api/recruitment";

/* ADD */
function addCandidate(e){
e.preventDefault();

fetch(BASE+"/add",{
method:"POST",
headers:{"Content-Type":"application/json"},
body:JSON.stringify({
name:document.getElementById("name").value,
email:document.getElementById("email").value,
phone:document.getElementById("phone").value,
position:document.getElementById("position").value,
experience:document.getElementById("experience").value,
skills:document.getElementById("skills").value,
interviewDate: document.getElementById("interviewDate").value
})
})
.then(()=>{
alert("Candidate Added!");
load();  // reload list without refresh
});
}

/* LOAD LIST */
function load(){
fetch(BASE+"/list")
.then(r=>r.json())
.then(data=>{
let tbl=document.getElementById("tbl");

if(!data || data.length===0){
tbl.innerHTML="<tr><td colspan='6'>No Candidates</td></tr>";
return;
}

let rows="";
data.forEach(c=>{
rows+=`
<tr>
<td>${c.name}</td>
<td>${c.position || '-'}</td>
<td>${c.skills || '-'}</td>
<td>${c.interviewDate || '-'}</td>
<td>${c.status || 'NEW'}</td>
<td>
<button class="short" onclick="status(${c.id},'SHORTLISTED')">Short</button>
<button class="sel" onclick="status(${c.id},'SELECTED')">Select</button>
<button class="rej" onclick="status(${c.id},'REJECTED')">Reject</button>
</td>
</tr>`;
});

tbl.innerHTML=rows;
});
}

/* STATUS */
function status(id, st){
fetch(BASE+"/status/"+id+"/"+st,{method:"PUT"}).then(load);
}

window.onload=load;
</script>
</#noparse>

</body>
</html>