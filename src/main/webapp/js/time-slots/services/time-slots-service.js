(function(angular){
	
	angular.module('csf').factory('TimeSlotsService', function($http, $q) {
		
		return {
			findAll: findAll,
			findAllWithRemaining: findAllWithRemaining,
			findAllTimeSlotUsage: findAllTimeSlotUsage,
			createAppointment: createAppointment
			
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
		
		function findAllWithRemaining(){
			var deferred = $q.defer();

			$http.get('/rest/timeslot/all/remaining')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			}); 	

			return deferred.promise;
		}
		
		function findAllTimeSlotUsage(){
			var deferred = $q.defer();

			$http.get('/rest/timeslot/usage/all')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			}); 	

			return deferred.promise;
		}
		
		function createAppointment(timeSlotId){
			var deferred = $q.defer();

			$http.post('/rest/timeslot/usage/'+timeSlotId+'')
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