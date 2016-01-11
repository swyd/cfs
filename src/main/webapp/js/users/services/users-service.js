(function(angular){
	
	angular.module('gaintMedia.services').factory('UsersService', function($http, $q) {
		
		return {
			getUsers: getUsers,
			createUser: createUser,
			changeRole: changeRole,
			editUser: editUser,
			deleteUser: deleteUser
		}
		
		function deleteUser(id) {
			var deferred = $q.defer();

			$http.delete('/rest/user/' + id)
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}
		
		function getUsers() {
			var deferred = $q.defer();

			$http.get('/rest/user/all')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}

		function createUser(data) {
			var deferred = $q.defer();

			$http.post('/rest/user/', data)
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}
		
		function changeRole(id) {
			var deferred = $q.defer();

			$http.post('/rest/user/' + id + '')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}

		function editUser(user) {
			var deferred = $q.defer();

			$http.put('/rest/user/' + user.id + '', user)
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