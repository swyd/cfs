(function(angular){
	
	angular.module('csf.services').factory('UserService', function($resource) {
		
		return $resource('http://ec2-52-27-30-179.us-west-2.compute.amazonaws.com:8080/rest/user/:action', {},
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