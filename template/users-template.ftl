<!DOCTYPE html>
<html>
<head>
	<title>Demo Spring Boot Freemarker</title>
</head>
<body>
<table border = "1">
	<thead>
	<tr>
		<th>Email</th>
		<th>Full Name</th>
		<th>Username</th>
		<th>Address</th>
	</tr>
	</thead>
	<tbody>
	<#list users as user>
		<tr>
			<td>${user.email}</td>
			<td>${user.fullName}</td>
			<td>${user.username}</td>
			<td>${user.address}</td>
		</tr>
	</#list>
	</tbody>
</table>
</body>
</html>
