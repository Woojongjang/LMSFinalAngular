lmsApp.controller("authorController", function($scope, $http, $window, $location, authorService, $filter, Pagination){
	if($location.$$path === "/viewauthors"){
		authorService.getAllAuthorsService().then(function(backendAuthorsList){
			$scope.authors = backendAuthorsList;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.authors.length / $scope.pagination.perPage);
		});
	}else if($location.$$path === "/addauthor"){
		$http.get("http://localhost:8080/lms/initAuthor").success(function(backendAuthorsList){
			$scope.author = backendAuthorsList;
		});
	}
	
	$scope.addAuthor = function() {
//		$location.path('/addauthor');
		authorService.initAuthorService().then(function(data){
			$scope.author = data;
//			$scope.author.authorName = "";
			$scope.add = true;
			document.getElementById("authorModalTitle").innerHTML = "Add New Author Details";
			document.getElementById("modalSave").innerHTML = "Add";
			document.getElementById("modalSave").setAttribute("class", "btn btn-success");
			$scope.editAuthorModal = true;
		});
	}
	
	$scope.authorDelete = function(authorId) {
//		var response = "";
		authorService.deleteAuthorService(authorId).then(function(data){
			alert(data);
			//response = data;
			authorService.getAllAuthorsService().then(function(backendAuthorsList){
				$scope.authors = backendAuthorsList;
				$scope.pagination = Pagination.getNew(10);
				$scope.pagination.numPages = Math.ceil($scope.authors.length / $scope.pagination.perPage);
			});
		});
	}
	
	$scope.saveAuthor = function(){
		$http.post("http://localhost:8080/lms/addAuthor", $scope.author).success(function(){
			$window.location.href = "#/viewauthors";
		});
	}
	
	$scope.sort = function(){
		$scope.authors = $filter('orderBy')($scope.authors, 'authorName');
	}
	
	$scope.showEditAuthorModal = function(authorId){
		authorService.getAuthorByPKService(authorId).then(function(data){
			$scope.add = false;
			$scope.author = data;
			document.getElementById("authorModalTitle").innerHTML = "Update Author Details";
			document.getElementById("modalSave").innerHTML = "Update";
			document.getElementById("modalSave").setAttribute("class", "btn btn-primary");
			$scope.editAuthorModal = true;
		});
	}
	
//	$scope.showEditAuthorModal = function(author){
//		$scope.author = author;
//		$scope.editAuthorModal = true;
//	}
	
	$scope.closeModal = function(){
		$scope.editAuthorModal = false;
	}
	
	$scope.updateAuthor = function(){
		if($scope.add) {
			if($scope.author.authorName === "" || $scope.author.authorName === undefined || $scope.author.authorName === null){
				alert("author name is empty");
				$scope.editAuthorModal = true;
			}
			else {
				authorService.addAuthorService($scope.author).then(function(data){
					alert(data);
					authorService.getAllAuthorsService().then(function(backendAuthorsList){
						$scope.authors = backendAuthorsList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.authors.length / $scope.pagination.perPage);
					});
				});
				$scope.editAuthorModal = false;
			}
		}
		else {
			if($scope.author.authorName === "" || $scope.author.authorName === undefined || $scope.author.authorName === null){
				alert("author name is empty");
				$scope.editAuthorModal = true;
			}
			else {
				$http.put("http://localhost:8080/lms/Authors/", $scope.author).success(function(){
					authorService.getAllAuthorsService().then(function(backendAuthorsList){
						$scope.authors = backendAuthorsList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.authors.length / $scope.pagination.perPage);
					});
				});
				$scope.editAuthorModal = false;
			}
		}
//		$scope.editAuthorModal = false;
	}
	
	$scope.searchAuthors = function(){
		authorService.getSearchAuthorService($scope.searchString).then(function(data){
			$scope.authors = data;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.authors.length / $scope.pagination.perPage);
		});
		//$scope.authors.
//		var filtered = [];
//		searchText = searchText.toLowerCase();
//		alert("testsearchAuth");
//		angular.forEach($scope.authors, function(author) {
//		    if( author.authorName.toLowerCase().indexOf(searchText) >= 0 ) { // search for author name
//		    	filtered.push(author);
//		    }
//		    else if (true) { //search for other things
//		    	filtered.push(author);
//		    }
//		});
//		alert("testsearchAuth");
//		$scope.authors = filtered;
	}
	
//	$scope.filter = function(){
//		$http.get("http://localhost:8080/lms/searchAuthors/"+$scope.searchString).success(function(data){
//			$scope.authors = data;
//			$scope.pagination = Pagination.getNew(10);
//			$scope.pagination.numPages = Math.ceil($scope.authors.length / $scope.pagination.perPage);
//		});
//	}
	
//	$scope.$watch('search', function(term) {
	//ADD filterFilter on controller
//	    // Create filtered 
//	    $scope.filtered = filterFilter($scope.authors, term);  
//
//	    // Then calculate noOfPages
//	    $scope.pagination.numPages = Math.ceil($scope.filtered.length/$scope.pagination.perPage);  
//	})
})