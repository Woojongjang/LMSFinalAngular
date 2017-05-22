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
		
		getSearchAuthorService: function(search) {
			var getAuthorSearch = {};
			return $http({
				url: authorConstants.GET_AUTHORS,
				method: "GET",
			    params: {searchString: search}
			}).success(function(data){
				getAuthorSearch = data;
			}).then(function(){
				return getAuthorSearch;
			})
		},
		
		setBookCountService: function(branchId, bookId, count){
			var retString = "";
			return $http({
				url: libraryConstants.LIBRARIES_URL+branchId+'/'+bookId+'/'+count,
				method: "PUT",
				data: {}
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		deleteCountService: function(branchId, bookId) {
			var resturnStr = "";
			return $http({
				url: libraryConstants.LIBRARIES_URL+branchId+'/'+bookId,
				method: "DELETE"
			}).success(function(data){
				resturnStr = data;
			}).then(function(){
				return resturnStr;
			})
		}
	}
})