(function(angular){
	
	angular.module('gaintMedia.services').factory('UserService', function($resource) {
		
		return $resource('rest/user/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				}
			}
		);
	});


})(angular);