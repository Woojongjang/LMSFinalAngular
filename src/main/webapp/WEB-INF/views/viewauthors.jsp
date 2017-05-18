<%@ taglib prefix="gcit" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="include.html"%>

<div class="container" role="main">
	<div class="page-header">
		<h1>List of Authors in LMS</h1>
	</div>
	<div class="input-group">
      
      <form action="searchAuthors">
	     <input type="text" class="form-control" name="searchString" id="searchString" placeholder="Search for..." oninput="searchAuthors()">
      </form>
    </div>
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	<table class="table" id="authorsTable">
		<thead>
			<tr>
				<th>#</th>
				<th>Author ID</th>
				<th>Author Name</th>
				<th>Edit</th>
				<th>Delete</th>
			</tr>
		</thead>
		<tbody>
			<gcit:forEach items="${authors}" var="a" varStatus="myIndex">
				<tr>
				<td>${myIndex.index + 1}</td>
				<td>${a.authorId}</td>
				<td>${a.authorName}</td>
				<td><button type="button" class="btn btn-primary"
						data-toggle="modal" data-target="#editAuthorModal"
						href="editauthor.jsp?authorId='${a.authorId}'">Update</button></td>
				<td><button type="button" class="btn btn-danger"
						href="deleteAuthor?authorId='${a.authorId}'">Delete</button></td>
				</tr>
			</gcit:forEach>
		</tbody>
	</table>
</div>

<div class="modal fade bs-example-modal-lg" id="editAuthorModal"
	tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<script>
$(document).ready(function()
{
    $('.modal').on('hidden.bs.modal', function(e)
    { 
        $(this).removeData();
    }) ;
});
</script>