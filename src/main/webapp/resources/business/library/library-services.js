lmsApp.factory("libraryService", function($http, libraryConstants){
	return{
		getAllLibrariesService: function(){
			var getBranchData = {};
			return $http({
				url: libraryConstants.GET_AUTHORS
			}).success(function(data){
				getBranchData = data;
			}).then(function(){
				return getBranchData;
			})
		},
	
		getAuthorByPKService: function(authorId){
			var getAuthorByPkData = {};
			return $http({
				url: authorConstants.GET_AUTHORS+authorId
			}).success(function(data){
				getAuthorByPkData = data;
			}).then(function(){
				return getAuthorByPkData;
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