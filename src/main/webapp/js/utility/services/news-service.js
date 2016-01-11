(function(angular){
	
	angular.module('gaintMedia.services').factory('NewsService', function($resource) {
		return $resource('rest/news/:id', {id: '@id'});
	});


})(angular);