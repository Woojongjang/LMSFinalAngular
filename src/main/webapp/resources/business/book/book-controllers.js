lmsApp.controller("bookController",
		function($scope, $http, $window, $location, bookService,
		$filter, Pagination, authorService, publisherService, genreService) {
	if($location.$$path === "/viewbooks"){
		bookService.getAllBooksService().then(function(backendBooksList){
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
		$scope.editModal = false;
	}
	
	$scope.addBook = function() {
		bookService.initBookService().then(function(data){
			$scope.book = data;
			$scope.add = true;
			document.getElementById("modalTitle").innerHTML = "Add New Book Details";
			document.getElementById("modalSave").innerHTML = "Add";
			document.getElementById("modalSave").setAttribute("class", "btn btn-success");
			authorService.getAllAuthorsService().then(function(backendAuthorsList){
				$scope.authors = backendAuthorsList;
			});
			publisherService.getAllPublishersService().then(function(backendPublishersList){
				$scope.publishers = backendPublishersList;
			});
			genreService.getAllGenresService().then(function(backendGenresList){
				$scope.genres = backendGenresList;
			});
			$scope.editModal = true;
		});
	}
	
	$scope.showEditModal = function(book){
		$scope.book = book;
		$scope.add = false;
		document.getElementById("modalTitle").innerHTML = "Update Book Details";
		document.getElementById("modalSave").innerHTML = "Update";
		document.getElementById("modalSave").setAttribute("class", "btn btn-primary");
		authorService.getAllAuthorsService().then(function(backendAuthorsList){
			$scope.authors = backendAuthorsList;
		});
		publisherService.getAllPublishersService().then(function(backendPublishersList){
			$scope.publishers = backendPublishersList;
		});
		genreService.getAllGenresService().then(function(backendGenresList){
			$scope.genres = backendGenresList;
		});
		$scope.editModal = true;
	}
	
	$scope.updateDetails = function(){
		if($scope.add) {
			if($scope.book.title === "" || $scope.book.title === undefined || $scope.book.title === null){
				alert("book name is empty");
				$scope.editModal = true;
			}
			else {
//				alert(JSON.stringify($scope.book));
				bookService.addBookService($scope.book).then(function(data){
					alert(data);
					bookService.getAllBooksService().then(function(backendBooksList){
						$scope.books = backendBooksList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
		else {
			if($scope.book.title === "" || $scope.book.title === undefined || $scope.book.title === null){
				alert("book name is empty");
				$scope.editModal = true;
			}
			else {
				$http.put("http://localhost:8080/lms/Books/", $scope.book).success(function(){
					bookService.getAllBooksService().then(function(backendBooksList){
						$scope.books = backendBooksList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
	}
	
	$scope.dataSearch = function(){
		bookService.searchService($scope.searchString).then(function(data){
			$scope.books = data;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
		});
	}
	
	$scope.dataDelete = function(bookId) {
		bookService.deleteBookService(bookId).then(function(data){
			alert(data);
			bookService.getAllBooksService().then(function(backendBooksList){
				$scope.books = backendBooksList;
				$scope.pagination = Pagination.getNew(10);
				$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
			});
		});
	}
})