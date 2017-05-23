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
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.loans.length / $scope.pagination.perPage);
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
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
			document.getElementById("viewHeader").innerHTML = "Books In "+$scope.branch.branchName+" Library For User "+$scope.user.borrowerName;
		});
	}else if($location.$$path === "/adminbookloans"){
		borrowerService.getAllLoansService().then(function(allLoansList){
			$scope.loans = allLoansList;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.loans.length / $scope.pagination.perPage);
		});
	}else if($location.$$path === "/viewborrowers"){
		borrowerService.getAllBorrowersService().then(function(allBackendList){
			$scope.borrowers = allBackendList;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.borrowers.length / $scope.pagination.perPage);
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
					$scope.pagination = Pagination.getNew(10);
					$scope.pagination.numPages = Math.ceil($scope.branch.books.length / $scope.pagination.perPage);
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
	
	//------ADMIN BOOKLOAN PAGE CONTROLLER-----//
	
	$scope.updateDueDate = function(loan) {
//		alert(JSON.stringify(loan));
		$scope.loan = loan;
//		$scope.add = false;
		$scope.editModal = true;
	}
	
	$scope.closeModal = function(){
		$scope.editModal = false;
	}
	
	$scope.updateDetails = function(){
		if($scope.loan.dateIn === "" || $scope.loan.dateIn === undefined || $scope.loan.dateIn === null
			|| $scope.loan.dateDue === "" || $scope.loan.dateDue === undefined || $scope.loan.dateDue === null){
			alert("Date Field has Wrong Input");
			$scope.editModal = true;
		}
		else {
			alert(JSON.stringify($scope.loan));
			borrowerService.updateLoansService($scope.loan).then(function(response){
				alert(response);
				borrowerService.getAllLoansService().then(function(allLoansList){
					$scope.loans = allLoansList;
					$scope.pagination = Pagination.getNew(10);
					$scope.pagination.numPages = Math.ceil($scope.loans.length / $scope.pagination.perPage);
				});
			});
			$scope.editModal = false;
		}
	}
	
//	$scope.dataSearch = function(){
//		bookService.searchService($scope.searchString).then(function(data){
//			$scope.books = data;
//			$scope.pagination = Pagination.getNew(10);
//			$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
//		});
//	}
	
	$scope.dataDelete = function(loan) {
		var dateOutStr = encodeURI(loan.dateChecked.toString());
//		alert(loan.book.bookId);
//		alert(loan.branch.branchId);
//		alert(loan.borrower.borrowerId);
//		alert(dateOutStr);
		borrowerService.deleteLoanService(loan.book.bookId, loan.branch.branchId, loan.borrower.borrowerId, dateOutStr).then(function(data){
			alert(data);
			borrowerService.getAllLoansService().then(function(allLoansList){
				$scope.loans = allLoansList;
				$scope.pagination = Pagination.getNew(10);
				$scope.pagination.numPages = Math.ceil($scope.loans.length / $scope.pagination.perPage);
			});
		});
	}
	
	//------ADMIN BORROWER PAGE CONTROLLER-----//
	
	$scope.sortBorrowers = function(){
		$scope.borrowers = $filter('orderBy')($scope.borrowers, 'borrowerName');
	}
	
	$scope.addBorrower = function() {
		$scope.add = true;
		borrowerService.initBorrowerService().then(function(data) {
			$scope.borrower = data;
			document.getElementById("modalTitle").innerHTML = "Adding New Borrower for LMS";
			document.getElementById("modalSave").innerHTML = "Add";
			document.getElementById("modalSave").setAttribute("class", "btn btn-success");
			$scope.editModal = true;
		});
	}
	
	$scope.updateButton = function(borrower) {
//		alert(library);
		borrowerService.initBorrowerService().then(function(data) {
			$scope.borrower = data;
			$scope.borrower.borrowerId = borrower.borrowerId;
			$scope.borrower.borrowerName = borrower.borrowerName;
			$scope.borrower.borrowerAddress = borrower.borrowerAddress;
			$scope.borrower.borrowerPhone = borrower.borrowerPhone;
			$scope.add = false;
			document.getElementById("modalTitle").innerHTML = "Updating Borrower for LMS";
			document.getElementById("modalSave").innerHTML = "Update";
			document.getElementById("modalSave").setAttribute("class", "btn btn-primary");
			$scope.editModal = true;
		});
	}
	
	$scope.deleteButton = function(borrower) {
//		alert(library);
		borrowerService.deleteBorrowerService(borrower.borrowerId).then(function(data){
			alert(data);
			borrowerService.getAllBorrowersService().then(function(backendList){
				$scope.borrowers = backendList;
				$scope.pagination = Pagination.getNew(10);
				$scope.pagination.numPages = Math.ceil($scope.borrowers.length / $scope.pagination.perPage);
			});
		});
	}
	
	$scope.updateData = function() {
		if($scope.add) {
			if($scope.borrower.borrowerName === "" 
				|| $scope.borrower.borrowerName === undefined
				|| $scope.borrower.borrowerName === null) {
					alert("Required Fields are Empty");
					$scope.editModal = true;
			}
			else {
				borrowerService.addBorrowerService($scope.borrower).then(function(data){
					alert(data);
					borrowerService.getAllBorrowersService().then(function(backendList){
						$scope.borrowers = backendList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.borrowers.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
		else {
			if($scope.borrower.borrowerName === "" 
				|| $scope.borrower.borrowerName === undefined
				|| $scope.borrower.borrowerName === null){
					alert("Required Fields are Empty");
					$scope.editModal = true;
			}
			else {
//				alert(JSON.stringify($scope.library));
				borrowerService.editBorrowerService($scope.borrower).then(function(data){
					alert(data);
					borrowerService.getAllBorrowersService().then(function(backendList){
						$scope.borrowers = backendList;
						$scope.pagination = Pagination.getNew(10);
						$scope.pagination.numPages = Math.ceil($scope.borrowers.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
	}
	
})