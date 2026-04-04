<!DOCTYPE html>
<html>
<head><title>Performance Reviews</title></head>
<body>
<h2>Performance Reviews for ${employee.name} (OHR: ${employee.ohr})</h2>
<a href="/employees/${employee.ohr}/performance/new">Add Review</a>
<table border="1">
  <tr><th>Date</th><th>Reviewer</th><th>Comments</th><th>Rating</th></tr>
  <#list reviews as r>
    <tr>
      <td>${r.reviewDate}</td>
      <td>${r.reviewer}</td>
      <td>${r.comments}</td>
      <td>${r.rating}</td>
    </tr>
  </#list>
</table>
</body>
</html>