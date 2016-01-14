(function(angular){
	
	angular.module('csf').factory('TrainingService', function($http, $q) {
		
		return {
			search: search,
			deleteTraining: deleteTraining
		}

		function search(data) {
			var deferred = $q.defer();

			$http.get('/rest/timeslot/all/usage',
				{
					params: data
				}
			)
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}

		function deleteTraining(id) {
			var deferred = $q.defer();

			$http.delete('/rest/timeslot/usage/' + id +'')
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