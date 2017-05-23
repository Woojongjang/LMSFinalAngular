lmsApp.factory("publisherService", function($http, publisherConstants){
	return{
		initPublisherService: function(){
			var initData = {};
			return $http({
				url: publisherConstants.INIT_PUBLISHER_URL,
				method: "GET"
			}).success(function(data){
				initData = data;
			}).then(function(){
				return initData;
			})
		},
		
		addPublisherService: function(publisher){
			var retString = "";
			return $http({
				url: publisherConstants.PUBLISHERS_URL,
				method: "POST",
				data: publisher
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		editPublisherService: function(publisher){
			var retString = "";
			return $http({
				url: publisherConstants.PUBLISHERS_URL,
				method: "PUT",
				data: publisher
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		deletePublisherService: function(publisherId){
			var retString = "";
			return $http({
				url: publisherConstants.PUBLISHERS_URL+publisherId,
				method: "DELETE"
			}).success(function(data){
				retString = data;
			}).then(function(){
				return retString;
			})
		},
		
		getAllPublishersService: function(){
			var getData = {};
			return $http({
				url: publisherConstants.PUBLISHERS_URL,
				method: "GET"
			}).success(function(data){
				getData = data;
			}).then(function(){
				return getData;
			})
		}
	}
})