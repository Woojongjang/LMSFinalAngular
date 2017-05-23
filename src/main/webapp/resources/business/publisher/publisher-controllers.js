lmsApp.controller("publisherController", function($scope, $http, $window, $location, bookService, $filter, Pagination, publisherService){
	if($location.$$path === "/viewpublishers"){
		publisherService.getAllPublishersService().then(function(backendList){
			$scope.publishers = backendList;
//			$scope.pagination = Pagination.getNew(10);
//			$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
		});
	}
	
	$scope.sort = function(){
		$scope.publishers = $filter('orderBy')($scope.publishers, 'publisherName');
	}
	
	$scope.closeModal = function(){
		$scope.editModal = false;
	}
	
	$scope.addPublisher = function() {
		$scope.add = true;
		publisherService.initPublisherService().then(function(data) {
			$scope.publisher = data;
			$scope.editModal = true;
		});
	}
	
	$scope.updateButton = function(publisher) {
//		alert(library);
		publisherService.initPublisherService().then(function(data) {
			$scope.publisher = data;
			$scope.publisher.publisherId = publisher.publisherId;
			$scope.publisher.publisherName = publisher.publisherName;
			$scope.publisher.publisherAddress = publisher.publisherAddress;
			$scope.publisher.publisherPhone = publisher.publisherPhone;
			$scope.add = false;
			$scope.editModal = true;
		});
	}
	
	$scope.deleteButton = function(publisher) {
//		alert(library);
		publisherService.deletePublisherService(publisher.publisherId).then(function(data){
			alert(data);
			publisherService.getAllPublishersService().then(function(backendList){
				$scope.publishers = backendList;
//				$scope.pagination = Pagination.getNew(10);
//				$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
			});
		});
	}
	
	$scope.updateData = function() {
		if($scope.add) {
			if($scope.publisher.publisherName === "" 
				|| $scope.publisher.publisherName === undefined
				|| $scope.publisher.publisherName === null) {
					alert("Required Fields are Empty");
					$scope.editModal = true;
			}
			else {
				publisherService.addPublisherService($scope.publisher).then(function(data){
					alert(data);
					publisherService.getAllPublishersService().then(function(backendList){
						$scope.publishers = backendList;
//						$scope.pagination = Pagination.getNew(10);
//						$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
		else {
			if($scope.publisher.publisherName === "" 
				|| $scope.publisher.publisherName === undefined
				|| $scope.publisher.publisherName === null){
					alert("Required Fields are Empty");
					$scope.editModal = true;
			}
			else {
//				alert(JSON.stringify($scope.library));
				publisherService.editPublisherService($scope.publisher).then(function(data){
					alert(data);
					publisherService.getAllPublishersService().then(function(backendList){
						$scope.publishers = backendList;
//						$scope.pagination = Pagination.getNew(10);
//						$scope.pagination.numPages = Math.ceil($scope.books.length / $scope.pagination.perPage);
					});
				});
				$scope.editModal = false;
			}
		}
	}
})