lmsApp.controller("borrowerController",
		function($scope, $http, $window, $location, bookService,
		$filter, Pagination, authorService, borrowerService, genreService, libraryService) {
//	this.keys = {
//		    "getBranch": "getBranch"
//		}
	if($location.$$path === "/borrower"){
		$scope.user = borrowerService.getData("cardNoSt");
//		alert(JSON.stringify($scope.user));
//		alert($scope.user);
		document.getElementById("welcomeId").innerHTML = "Welcome to GCIT Library Management System, User "+$scope.user.borrowerName;
	}else if($location.$$path === "/viewbooksloans"){
		$scope.user = borrowerService.getData("cardNoSt");
//		alert(JSON.stringify($scope.user));
//		alert($scope.user.borrowerId);
		borrowerService.getUserLoansService($scope.user.borrowerId).then(function(bookLoansList){
			$scope.loans = bookLoansList;
//			$scope.pagination = Pagination.getNew(10);
//			$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
//			document.getElementById("viewHeader").innerHTML = "Book Loans For "+$scope.user.borrowerName;
		});
		bookService.getAllBooksService().then(function(backendBooksList){
			$scope.allBooks = backendBooksList;
		});
	}
	
	$scope.goToLogIn = function(cardId) {
		if($scope.cardId === "" || $scope.cardId === undefined || $scope.cardId === null){
			alert("User Card Number Is Empty");
		}
		else{
			borrowerService.authenticateUserService(cardId).then(function(response) {
				if(response.error === "" || response.error === undefined || response.error === null) {
//					alert(JSON.stringify(response));
					$location.path('/borrower');
					borrowerService.setData(response, "cardNoSt");
				}
				else {
					alert("No User With That Card Number");
				}
			});
		}
	}
	
	$scope.checkIn = function(loan){
		alert(JSON.stringify(loan));
		borrowerService.turnInBookService($scope.user.borrowerId).then(function(bookLoansList){
			$scope.loans = bookLoansList;
		});
	}
	
	$scope.sort = function(){
		$scope.branch.books = $filter('orderBy')($scope.branch.books, 'title');
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
		libraryService.deleteCountService($scope.gotId, bookId).then(function(data){
			alert(data);
			libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
				$scope.branch = branchBookList;
			});
		});
	}
})