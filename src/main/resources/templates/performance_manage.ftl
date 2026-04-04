<!DOCTYPE html>
<html>
<head><title>Performance Management</title></head>
<body>
<h2>Performance Management</h2>
<table border="1">
  <tr><th>OHR</th><th>Name</th><th>Department</th><th>Designation</th><th>Actions</th></tr>
  <#list employees as emp>
    <tr>
      <td>${emp.ohr}</td>
      <td>${emp.name}</td>
      <td>${emp.department}</td>
      <td>${emp.designation}</td>
      <td>
        <a href="/performances/${emp.ohr}/performance">View Reviews</a>
        <a href="/performances/${emp.ohr}/performance/new">Add Review</a>
      </td>
    </tr>
  </#list>
</table>
</body>
</html>