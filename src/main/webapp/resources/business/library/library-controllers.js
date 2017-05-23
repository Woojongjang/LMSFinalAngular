lmsApp.controller("libraryController",
		function($scope, $http, $window, $location, bookService,
		$filter, Pagination, authorService, publisherService, genreService, libraryService) {
//	this.keys = {
//		    "getBranch": "getBranch"
//		}
	if($location.$$path === "/librarian"){
		libraryService.getAllLibrariesService().then(function(backendBranchList){
			$scope.libraries = backendBranchList;
			$scope.branch = $scope.libraries[0];
		});
	}else if($location.$$path === "/viewlibrarybooks"){
		$scope.gotId = libraryService.getData("branchSt");
		libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
			$scope.branch = branchBookList;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
			document.getElementById("viewHeader").innerHTML = "Books In "+$scope.branch.branchName+" Library";
		});
		bookService.getAllBooksService().then(function(backendBooksList){
			$scope.allBooks = backendBooksList;
		});
	}else if($location.$$path === "/viewlibraries"){
		libraryService.getAllLibrariesService().then(function(backendBranchList){
			$scope.libraries = backendBranchList;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.libraries.length / $scope.pagination.perPage);
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
		$scope.branch.books = $filter('orderBy')($scope.branch.books, 'title');
	}
	
	$scope.sortBranch = function() {
		$scope.libraries = $filter('orderBy')($scope.libraries, 'branchName');
	}
	
	$scope.closeModal = function(){
		$scope.editModal = false;
		$scope.setCount = undefined;
	}
	
	$scope.addBook = function() {
		bookService.initBookService().then(function(data){
			$scope.book = data;
			$scope.add = true;
			document.getElementById("modalTitle").innerHTML = "Adding New Book Entry for Branch";
			document.getElementById("modalSave").innerHTML = "Add";
			document.getElementById("modalSave").setAttribute("class", "btn btn-success");
			$scope.book = $scope.allBooks[0];
			$scope.editModal = true;
		});
	}
	
	$scope.showEditModal = function(book){
		$scope.book = book;
		$scope.add = false;
		document.getElementById("modalTitle").innerHTML = "Adding Book Count Entry for Branch";
		document.getElementById("modalSave").innerHTML = "Update";
		document.getElementById("modalSave").setAttribute("class", "btn btn-primary");
		$scope.editModal = true;
	}
	
	$scope.updateDetails = function(){
		if($scope.add) {
			if($scope.setCount === "" || $scope.setCount === undefined || $scope.setCount === null){
				alert("book count is empty");
				$scope.editModal = true;
			}
			else {
//				alert(JSON.stringify($scope.book));
				libraryService.setBookCountService($scope.gotId, $scope.book.bookId, $scope.setCount).then(function(data){
					alert(data);
					libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
						$scope.branch = branchBookList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
		else {
			if($scope.setCount === "" || $scope.setCount === undefined || $scope.setCount === null){
				alert("book count is empty");
				$scope.editModal = true;
			}
			else {
				libraryService.setBookCountService($scope.gotId, $scope.book.bookId, $scope.setCount).then(function(data){
					alert(data);
					libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
						$scope.branch = branchBookList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
	}
	
	//	$scope.dataSearch = function(){
	//	bookService.searchService($scope.searchString).then(function(data){
	//		$scope.books = data;
	//		$scope.pagination = Pagination.getNew(10);
	//		$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
	//	});
	//}
	
	$scope.dataDelete = function(bookId) {
		libraryService.deleteCountService($scope.gotId, bookId).then(function(data){
			alert(data);
			libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
				$scope.branch = branchBookList;
				$scope.pagination = Pagination.getNew(10);
				$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
			});
		});
	}
	
	//-----ADMIN LIBRARY PAGE-----//
	
	$scope.addLibrary = function() {
		$scope.add = true;
		libraryService.initLibraryService().then(function(data) {
			$scope.library = data;
			document.getElementById("modalTitle").innerHTML = "Adding New Library for LMS";
			document.getElementById("modalSave").innerHTML = "Add";
			document.getElementById("modalSave").setAttribute("class", "btn btn-success");
			$scope.editModal = true;
		});
	}
	
	$scope.updateButton = function(library) {
//		alert(library);
		libraryService.initLibraryService().then(function(data) {
			$scope.library = data;
			$scope.library.branchId = library.branchId;
			$scope.library.branchName = library.branchName;
			$scope.library.branchAddress = library.branchAddress;
			$scope.add = false;
			document.getElementById("modalTitle").innerHTML = "Updating Library for LMS";
			document.getElementById("modalSave").innerHTML = "Update";
			document.getElementById("modalSave").setAttribute("class", "btn btn-primary");
			$scope.editModal = true;
		});
	}
	
	$scope.deleteButton = function(library) {
//		alert(library);
		libraryService.deleteLibraryService(library.branchId).then(function(data){
			alert(data);
			libraryService.getAllLibrariesService().then(function(backendBranchList){
				$scope.libraries = backendBranchList;
				$scope.pagination = Pagination.getNew(10);
				$scope.pagination.numPages = Math.ceil($scope.libraries.length / $scope.pagination.perPage);
			});
		});
	}
	
	$scope.updateLibrary = function() {
		if($scope.add) {
			if($scope.library.branchAddress === "" || $scope.library.branchAddress === undefined || $scope.library.branchAddress === null
					|| $scope.library.branchName === "" || $scope.library.branchName === undefined || $scope.library.branchName === null){
				alert("Required Fields are Empty");
				$scope.editModal = true;
			}
			else {
				libraryService.addLibraryService($scope.library).then(function(data){
					alert(data);
					libraryService.getAllLibrariesService().then(function(backendBranchList){
						$scope.libraries = backendBranchList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.libraries.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
		else {
			if($scope.library.branchAddress === "" || $scope.library.branchAddress === undefined || $scope.library.branchAddress === null
					|| $scope.library.branchName === "" || $scope.library.branchName === undefined || $scope.library.branchName === null){
				alert("Required Fields are Empty");
				$scope.editModal = true;
			}
			else {
//				alert(JSON.stringify($scope.library));
				libraryService.editLibraryService($scope.library).then(function(data){
					alert(data);
					libraryService.getAllLibrariesService().then(function(backendBranchList){
						$scope.libraries = backendBranchList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.libraries.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
	}
	
})