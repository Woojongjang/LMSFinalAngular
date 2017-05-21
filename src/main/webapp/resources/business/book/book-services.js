lmsApp.factory("bookService", function($http, authorConstants){
	return{
		initAuthorService: function(){
			var getAuthorData = {};
			return $http({
				url: authorConstants.INIT_AUTHOR_URL,
				method: "GET"
			}).success(function(data){
				getAuthorData = data;
			}).then(function(){
				return getAuthorData;
			})
		},
		
		addAuthorService: function(author) {
			var resturnStr = "";
			return $http({
				url: authorConstants.AUTHORS_URL,
				method: "POST",
				data: author
			}).success(function(data){
				resturnStr = data;
			}).then(function(){
				return resturnStr;
			})
		},
		
		getAllAuthorsService: function(){
			var getAuthorData = {};
			return $http({
				url: authorConstants.AUTHORS_URL,
				method: "GET"
			}).success(function(data){
				getAuthorData = data;
			}).then(function(){
				return getAuthorData;
			})
		},
	
		getAuthorByPKService: function(authorId){
			var getAuthorByPkData = {};
			return $http({
				url: authorConstants.AUTHORS_URL+authorId,
				method: "GET"
			}).success(function(data){
				getAuthorByPkData = data;
			}).then(function(){
				return getAuthorByPkData;
			})
		},
		
		getSearchAuthorService: function(search) {
			var getAuthorSearch = {};
			return $http({
				url: authorConstants.AUTHORS_URL,
				method: "GET",
			    params: {searchString: search}
			}).success(function(data){
				getAuthorSearch = data;
			}).then(function(){
				return getAuthorSearch;
			})
		},
		
		deleteAuthorService: function(authorId) {
			var resturnStr = "";
			return $http({
				url: authorConstants.AUTHORS_URL+authorId,
				method: "DELETE"
			}).success(function(data){
				resturnStr = data;
			}).then(function(){
				return resturnStr;
			})
		}
	}
})