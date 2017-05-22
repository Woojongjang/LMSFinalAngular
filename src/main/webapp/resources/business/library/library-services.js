lmsApp.factory("libraryService", function($http, libraryConstants){
	var branchStore = {};
	
	var setData = function(data, key){
		branchStore[key] = data;
	};
	var getData = function(key){
		return branchStore[key];
	};
	return{
		setData: setData,
		getData: getData,
		getAllLibrariesService: function(){
			var getBranchData = {};
			return $http({
				url: libraryConstants.LIBRARIES_URL,
				method: "GET"
			}).success(function(data){
				getBranchData = data;
			}).then(function(){
				return getBranchData;
			})
		},
		
		getBranchBooksService: function(branchId){
			var getBranchBookData = {};
			return $http({
				url: libraryConstants.LIBRARIES_URL+branchId+"/Books",
				method: "GET"
			}).success(function(data){
				getBranchBookData = data;
			}).then(function(){
				return getBranchBookData;
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
		}
	}
})