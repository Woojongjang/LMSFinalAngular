lmsApp.factory("borrowerService", function($http, borrowerConstants){
	var userIdStore = {};
	
	var setData = function(data, key){
		userIdStore[key] = data;
	};
	var getData = function(key){
		return userIdStore[key];
	};
	return{
		setData: setData,
		getData: getData,
		
		initBookLoanService: function(){
			var initData = {};
			return $http({
				url: borrowerConstants.INIT_BOOKLOAN_URL,
				method: "GET"
			}).success(function(data){
				initData = data;
			}).then(function(){
				return initData;
			})
		},
		
		authenticateUserService: function(cardId) {
			var getBranchData = {};
			return $http({
				url: borrowerConstants.BORROWER_URL+cardId,
				method: "GET"
			}).then(function successCallback(response) {
			    // this callback will be called asynchronously
			    // when the response is available
				//alert("success");
				//alert(JSON.stringify(response.data));
				//borrowerService.setData(response.data.borrowerId, "userIdSt");
				return response.data;
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
//				  alert("error");
//				alert(JSON.stringify(response.data));
				  return response.data;
			  });
		},
		
		getUserService: function(cardId){
			var getUserData = {};
			return $http({
				url: borrowerConstants.BORROWER_URL+cardId,
				method: "GET"
			}).success(function(data){
				getUserData = data;
			}).then(function(){
				return getUserData;
			})
		},
		
		getUserLoansService: function(cardId){
			var getLoansData = {};
			return $http({
				url: borrowerConstants.BORROWER_URL+cardId+"/BookLoans",
				method: "GET"
			}).success(function(data){
				getLoansData = data;
			}).then(function(){
				return getLoansData;
			})
		},
		
		turnInBookService: function(cardId, loan) {
			var respString = "";
			return $http({
				url: borrowerConstants.BORROWER_URL+cardId+"/BookLoans",
				method: "PUT",
				data: loan
			}).success(function(data){
				respString = data;
			}).then(function(){
				return respString;
			})
		},
		
		borrowBookService: function(cardId, loan){
			var retString = "";
			return $http({
				url: borrowerConstants.BORROWER_URL+cardId+"/BookLoans",
				method: "POST",
				data: loan
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		deleteLoanService: function(book, branch, card, date) {
//			alert(book);
//			alert(branch);
//			alert(card);
//			alert(date);
			var resturnStr = "";
			return $http({
				url: borrowerConstants.BOOK_LOAN_URL,
				method: "DELETE",
				params: {bookId: book,
						branchId: branch,
						cardNo: card, 
						dateOut: date}
			}).success(function(data){
				resturnStr = data;
			}).then(function(){
				return resturnStr;
			})
		},
		
		getAllLoansService: function() {
			var allLoans = {};
			return $http({
				url: borrowerConstants.BOOK_LOAN_URL,
				method: "GET"
			}).success(function(data){
				allLoans = data;
			}).then(function(){
				return allLoans;
			})
		},
		
		updateLoansService: function(loan) {
			var retUpdate = "";
			return $http({
				url: borrowerConstants.BOOK_LOAN_URL,
				method: "PUT",
				data: loan
			}).success(function(data){
				retUpdate = data;
			}).then(function(){
				return retUpdate;
			})
		},
		
		initBorrowerService: function(){
			var initData = {};
			return $http({
				url: borrowerConstants.INIT_BORROWER_URL,
				method: "GET"
			}).success(function(data){
				initData = data;
			}).then(function(){
				return initData;
			})
		},
		
		addBorrowerService: function(borrower){
			var retString = "";
			return $http({
				url: borrowerConstants.ADMIN_BORROWERS_URL,
				method: "POST",
				data: borrower
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		editBorrowerService: function(borrower){
			var retString = "";
			return $http({
				url: borrowerConstants.ADMIN_BORROWERS_URL,
				method: "PUT",
				data: borrower
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		deleteBorrowerService: function(borrowerId){
			var retString = "";
			return $http({
				url: borrowerConstants.ADMIN_BORROWERS_URL+borrowerId,
				method: "DELETE"
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		getAllBorrowersService: function(){
			var getData = {};
			return $http({
				url: borrowerConstants.ADMIN_BORROWERS_URL,
				method: "GET"
			}).success(function(data){
				getData = data;
			}).then(function(){
				return getData;
			})
		}
	}
})