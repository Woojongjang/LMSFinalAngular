lmsApp.factory("bookService", function($http, bookConstants){
	return{
		initBookService: function(){
			var initData = {};
			return $http({
				url: bookConstants.INIT_BOOK_URL,
				method: "GET"
			}).success(function(data){
				initData = data;
			}).then(function(){
				return initData;
			})
		},
		
		addBookService: function(book) {
			var retString = "";
			return $http({
				url: bookConstants.BOOKS_URL,
				method: "POST",
				data: book
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		getAllBooksService: function(){
			var getBookData = {};
			return $http({
				url: bookConstants.BOOKS_URL,
				method: "GET"
			}).success(function(data){
				getBookData = data;
			}).then(function(){
				return getBookData;
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
		
		searchService: function(search) {
			var getSearch = {};
			return $http({
				url: bookConstants.BOOKS_URL,
				method: "GET",
			    params: {searchString: search}
			}).success(function(data){
				getSearch = data;
			}).then(function(){
				return getSearch;
			})
		},
		
		updateBookService: function(book) {
			var retString = "";
			return $http({
				url: bookConstants.BOOKS_URL,
				method: "PUT",
				data: book
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		deleteBookService: function(bookId) {
			var resturnStr = "";
			return $http({
				url: bookConstants.BOOKS_URL+bookId,
				method: "DELETE"
			}).success(function(data){
				resturnStr = data;
			}).then(function(){
				return resturnStr;
			})
		}
	}
})