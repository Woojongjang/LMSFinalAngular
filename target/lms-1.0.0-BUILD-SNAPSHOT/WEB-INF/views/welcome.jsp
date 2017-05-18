<%@include file="include.html" %>
<div class="container">
<div class="jumbotron">
	<h1>GCIT Library Management System</h1>
	<p>Welcome to GCIT Library Management System.</p>
</div>
<div class='row'>
	<div class="col-sm-4">
		<div class="panel panel-default">
			<div class="panel-heading">
			<a href="admin" style="text-decoration: none">
				<h3 class="panel-title">Administrator Services</h3>
				</a>
			</div>
			<div class="panel-body">Manage Library Branches, Books, Authors,
									Publishers, Borrowers, Genres
			</div>
		</div>
	</div>
	<div class="col-sm-4">
		<div class="panel panel-success">
			<div class="panel-heading">
			<a href="librarianlogin.jsp" style="text-decoration: none">
				<h3 class="panel-title">Librarian Services</h3>
				</a>
			</div>
			<div class="panel-body">Manage Library Branches, Add/Remove Book Copies</div>
		</div>
	</div>
	<div class="col-sm-4">
		<div class="panel panel-warning">
				<div class="panel-heading">
					<a href="borrowerlogin.jsp" style="text-decoration: none">
						<h3 class="panel-title">Borrower Services</h3>
					</a>
				</div>
			<div class="panel-body">Borrow Books, Return Books</div>
		</div>
	</div>
</div>
</div>