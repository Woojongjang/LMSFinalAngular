lmsApp.factory("genreService", function($http, genreConstants){
	return{
		initBookService: function(){
			var initData = {};
			return $http({
				url: genreConstants.INIT_BOOK_URL,
				method: "GET"
			}).success(function(data){
				initData = data;
			}).then(function(){
				return initData;
			})
		},
		
		getAllGenresService: function(){
			var getData = {};
			return $http({
				url: genreConstants.GENRES_URL,
				method: "GET"
			}).success(function(data){
				getData = data;
			}).then(function(){
				return getData;
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