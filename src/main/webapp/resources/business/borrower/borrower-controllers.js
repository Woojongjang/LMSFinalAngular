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
	}else if($location.$$path === "/choosebranch") {
		$scope.user = borrowerService.getData("cardNoSt");
		libraryService.getAllLibrariesService().then(function(backendBranchList){
			$scope.libraries = backendBranchList;
			$scope.branch = $scope.libraries[0];
		});
	}else if($location.$$path === "/borrowlibrarybooks"){
		$scope.user = borrowerService.getData("cardNoSt");
		$scope.gotId = libraryService.getData("branchSt");
		libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
			$scope.branch = branchBookList;
//			$scope.pagination = Pagination.getNew(10);
//			$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
			document.getElementById("viewHeader").innerHTML = "Books In "+$scope.branch.branchName+" Library For User "+$scope.user.borrowerName;
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
//		alert(JSON.stringify(loan));
		var current = new Date().toISOString().slice(0, 19).replace('T', ' ');
		alert(current);
		loan.dateIn = current;
//		alert(JSON.stringify(loan));
//		alert($scope.user.borrowerId);
		borrowerService.turnInBookService($scope.user.borrowerId, loan).then(function(response){
			alert(response);
//			$scope.loans = bookLoansList;
		});
	}
	
	$scope.goToBranch = function(branchId) {
		$location.path('/borrowlibrarybooks');
		libraryService.setData(branchId, "branchSt");
	}
	
	$scope.sort = function(){
		$scope.branch.books = $filter('orderBy')($scope.branch.books, 'title');
	}
	
	$scope.borrow = function(book) {
		borrowerService.initBookLoanService().then(function(data){
			$scope.loan = data;
//			alert(JSON.stringify($scope.loan));
			var current = new Date().toISOString().slice(0, 19).replace('T', ' ');
//			alert($scope.gotId);
//			alert(book.bookId);
//			alert($scope.user.borrowerId);
			$scope.loan.branch.branchId = $scope.gotId;
			$scope.loan.book.bookId = book.bookId;
			$scope.loan.borrower.borrowerId = $scope.user.borrowerId;
			$scope.loan.dateChecked = current.toString();
//			alert(JSON.stringify($scope.loan));
			borrowerService.borrowBookService($scope.user.borrowerId, $scope.loan).then(function(response){
				alert(response);
				libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
					$scope.branch = branchBookList;
				});
			});
		});
//		alert(JSON.stringify($scope.loan));
//		alert($scope.gotId);
//		alert(book.bookId);
//		alert($scope.user.borrowerId);
//		alert(current);
//		borrowerService.borrowBookService($scope.user.borrowerId, loan).then(function(response){
//			alert(response);
////			$scope.loans = bookLoansList;
//		});
//		libraryService.setBookCountService($scope.gotId, book.bookId, count).then(function(data){
//			alert(data);
//			libraryService.getBranchBooksService($scope.gotId).then(function(branchBookList){
//				$scope.branch = branchBookList;
//			});
//		});
	}
	
	$scope.dataSearch = function(){
		bookService.searchService($scope.searchString).then(function(data){
			$scope.books = data;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
		});
	}
	
})