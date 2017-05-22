lmsApp.controller("libraryController",
		function($scope, $http, $window, $location, bookService,
		$filter, Pagination, authorService, publisherService, genreService, libraryService) {
	this.keys = {
		    "getBranch": "getBranch"
		}
	if($location.$$path === "/librarian"){
		libraryService.getAllLibrariesService().then(function(backendBranchList){
			$scope.libraries = backendBranchList;
			$scope.branch = $scope.libraries[0];
		});
	}else if($location.$$path === "/viewlibrarybooks"){
		var gotId = libraryService.getData("branchSt");
		libraryService.getBranchBooksService(gotId).then(function(branchBookList){
			$scope.branch = branchBookList;
//			$scope.pagination = Pagination.getNew(10);
//			$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
			document.getElementById("viewHeader").innerHTML = "Books In "+$scope.branch.branchName+" Library";
		});
	}
	
	$scope.goToBranch = function(branchId) {
//		alert(branchId);
//		libraryService.setData(branchId, keys.getBranch);
//		libraryService.setData(44, keys.getBranch);
		$location.path('/viewlibrarybooks');
		libraryService.setData(branchId, "branchSt");
		
//		libraryService.getBranchBooksService(branchId).then(function(branchBookList){
//			alert("branch book list inside");
//			$scope.branch = branchBookList;
//			var branch = branchBookList;
//			alert(JSON.stringify($scope.branch));
//			alert(JSON.stringify($scope.branch));
//			document.getElementById("viewHeader").innerHTML = "Books In "+$scope.branch.branchName+" Library";
//			alert($scope.text);
//			alert($scope.branch.booksCount["2"]);
//			$scope.count = [];
//			$scope.branch.books.forEach(function(book) {
//				$scope.count = [{"title":book.title,$scope.branch.booksCount[book.bookId]: 2]
//			});
//			$scope.count = 
//		});
//		$scope.branch = branch;
//		libraryService.getAllBranchBooksService().then(function(branchBookList){
//			$scope.books = branchBookList;
//		});
	}
	
	$scope.sort = function(){
		$scope.books = $filter('orderBy')($scope.books, 'title');
	}
	
	$scope.closeModal = function(){
		$scope.editModal = false;
		$scope.addCount = undefined;
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
		document.getElementById("modalTitle").innerHTML = "Adding Book Entry for Branch";
		document.getElementById("modalSave").innerHTML = "Update";
		document.getElementById("modalSave").setAttribute("class", "btn btn-primary");
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