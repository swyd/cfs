(function(angular){
	
	angular.module('csf.services').factory('NewsService', function($resource) {
		return $resource('rest/news/:id', {id: '@id'});
	});


})(angular);