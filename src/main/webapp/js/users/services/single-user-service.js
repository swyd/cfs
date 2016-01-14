(function(angular){
	
	angular.module('csf.services').factory('SingleUserService', function($http, $q) {
		
		return {
			getUser: getUser,
			saveUser: saveUser
		}
		
		
		function getUser() {
			var deferred = $q.defer();

			$http.get('/rest/user')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}

		function saveUser(user) {
			var deferred = $q.defer();

			$http.put('/rest/user', user)
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}
		
	});

})(angular);