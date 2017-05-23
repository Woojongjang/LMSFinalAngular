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
		
		initLibraryService: function(){
			var initData = {};
			return $http({
				url: libraryConstants.INIT_BRANCH_URL,
				method: "GET"
			}).success(function(data){
				initData = data;
			}).then(function(){
				return initData;
			})
		},
		
		addLibraryService: function(branch){
			var retString = "";
			return $http({
				url: libraryConstants.LIBRARIES_URL,
				method: "POST",
				data: branch
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		editLibraryService: function(branch){
			var retString = "";
			return $http({
				url: libraryConstants.LIBRARIES_URL,
				method: "PUT",
				data: branch
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		deleteLibraryService: function(branchId){
			var retString = "";
			return $http({
				url: libraryConstants.LIBRARIES_URL+branchId,
				method: "DELETE"
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
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