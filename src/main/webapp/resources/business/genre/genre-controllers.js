lmsApp.controller("genreController", function($scope, $http, $window, $location, bookService, $filter, Pagination, genreService){
	if($location.$$path === "/viewgenres"){
		genreService.getAllGenresService().then(function(backendList){
			$scope.genres = backendList;
//			$scope.pagination = Pagination.getNew(10);
//			$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
		});
	}
	
	$scope.sort = function(){
		$scope.genres = $filter('orderBy')($scope.genres, 'genreName');
	}
	
	$scope.closeModal = function(){
		$scope.editModal = false;
	}
	
	$scope.addGenre = function() {
		$scope.add = true;
		genreService.initGenreService().then(function(data) {
			$scope.genre = data;
			$scope.editModal = true;
		});
	}
	
	$scope.updateButton = function(genre) {
//		alert(library);
		genreService.initGenreService().then(function(data) {
			$scope.genre = data;
			$scope.genre.genreId = genre.genreId;
			$scope.genre.genreName = genre.genreName;
			$scope.add = false;
			$scope.editModal = true;
		});
	}
	
	$scope.deleteButton = function(genre) {
//		alert(library);
		genreService.deleteGenreService(genre.genreId).then(function(data){
			alert(data);
			genreService.getAllGenresService().then(function(backendList){
				$scope.genres = backendList;
//				$scope.pagination = Pagination.getNew(10);
//				$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
			});
		});
	}
	
	$scope.updateGenre = function() {
		if($scope.add) {
			if($scope.genre.genreName === "" || $scope.genre.genreName === undefined || $scope.genre.genreName === null){
				alert("Required Fields are Empty");
				$scope.editModal = true;
			}
			else {
				genreService.addGenreService($scope.genre).then(function(data){
					alert(data);
					genreService.getAllGenresService().then(function(backendList){
						$scope.genres = backendList;
//						$scope.pagination = Pagination.getNew(10);
//						$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
		else {
			if($scope.genre.genreName === "" || $scope.genre.genreName === undefined || $scope.genre.genreName === null){
				alert("Required Fields are Empty");
				$scope.editModal = true;
			}
			else {
//				alert(JSON.stringify($scope.library));
				genreService.editGenreService($scope.genre).then(function(data){
					alert(data);
					genreService.getAllGenresService().then(function(backendList){
						$scope.genres = backendList;
//						$scope.pagination = Pagination.getNew(10);
//						$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
	}
})