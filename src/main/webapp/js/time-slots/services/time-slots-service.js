(function(angular){
	
	angular.module('csf').factory('TimeSlotsService', function($http, $q) {
		
		return {
			findAll: findAll,
			findAllWithRemaining: findAllWithRemaining,
			findAllTimeSlotUsage: findAllTimeSlotUsage,
			createAppointment: createAppointment,
			cancelAppointment: cancelAppointment
			
		}
		
		function findAll(packet){
			var deferred = $q.defer();

			$http.get('/rest/timeslot/all')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}
		
		function findAllWithRemaining(query){
			var deferred = $q.defer();

			$http.get('/rest/timeslot/all/remaining', {
				params: query
			})
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			}); 	

			return deferred.promise;
		}
		
		function findAllTimeSlotUsage(query){
			var deferred = $q.defer();

			$http.get('/rest/timeslot/usage/all', {
				params: query
			})
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			}); 	

			return deferred.promise;
		}
		
		function createAppointment(timeSlotId, date){
			var deferred = $q.defer();
			$http.post('/rest/timeslot/usage/'+timeSlotId+'/'+date+'')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			}); 	

			return deferred.promise;
		}

		function cancelAppointment(usageId){
			var deferred = $q.defer();

			$http.delete('/rest/timeslot/usage/'+usageId+'')
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