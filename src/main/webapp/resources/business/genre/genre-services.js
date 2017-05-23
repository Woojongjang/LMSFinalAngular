lmsApp.factory("genreService", function($http, genreConstants){
	return{
		initGenreService: function(){
			var initData = {};
			return $http({
				url: genreConstants.INIT_GENRE_URL,
				method: "GET"
			}).success(function(data){
				initData = data;
			}).then(function(){
				return initData;
			})
		},
		
		addGenreService: function(genre){
			var retString = "";
			return $http({
				url: genreConstants.GENRES_URL,
				method: "POST",
				data: genre
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		editGenreService: function(genre){
			var retString = "";
			return $http({
				url: genreConstants.GENRES_URL,
				method: "PUT",
				data: genre
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		deleteGenreService: function(genreId){
			var retString = "";
			return $http({
				url: genreConstants.GENRES_URL+genreId,
				method: "DELETE"
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
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
		}
	}
})