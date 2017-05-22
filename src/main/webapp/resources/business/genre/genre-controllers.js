lmsApp.controller("genreController", function($scope, $http, $window, $location, bookService, $filter, Pagination, authorService){
	if($location.$$path === "/viewbooks"){
		bookService.getAllBookssService().then(function(backendBooksList){
			$scope.books = backendBooksList;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
		});
	}else if($location.$$path === "/addauthor"){
		$http.get("http://localhost:8080/lms/initAuthor").success(function(backendBooksList){
			$scope.author = backendBooksList;
		});
	}
	
	$scope.sort = function(){
		$scope.books = $filter('orderBy')($scope.books, 'title');
	}
	
	$scope.closeModal = function(){
		$scope.editAuthorModal = false;
	}
	
	$scope.addBook = function() {
		bookService.initBookService().then(function(data){
			$scope.book = data;
			$scope.add = true;
			document.getElementById("modalTitle").innerHTML = "Add New Book Details";
			authorService.getAllAuthorsService().then(function(backendAuthorsList){
				$scope.authors = backendAuthorsList;
			});
			
			$scope.editModal = true;
		});
	}
	
	$scope.updateDetails = function(){
		if($scope.add) {
			if($scope.book.title === ""){
				alert("book name is empty");
				$scope.editModal = true;
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
				$scope.editModal = false;
			}
		}
		else {
			$http.put("http://localhost:8080/lms/Authors/", $scope.author).success(function(){
				authorService.getAllAuthorsService().then(function(backendAuthorsList){
					$scope.authors = backendAuthorsList;
					$scope.pagination = Pagination.getNew(10);
					$scope.pagination.numPages = Math.ceil($scope.authors.length / $scope.pagination.perPage);
				});
			});
			$scope.editModal = false;
		}
//		$scope.editAuthorModal = false;
	}
})