<!DOCTYPE html>
<html>
<head><title>New Review</title></head>
<body>
<h2>Add Performance Review</h2>
<form action="/employees/${review.employee.ohr}/performance" method="post">
  Reviewer: <input type="text" name="reviewer"/><br/>
  Comments: <textarea name="comments"></textarea><br/>
  Rating: <input type="number" name="rating" min="1" max="5"/><br/>
  <button type="submit">Save</button>
</form>
</body>
</html>