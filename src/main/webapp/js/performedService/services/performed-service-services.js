(function(angular){
	
	angular.module('csf').factory('PerformedService', function($http, $q) {
		
		return {
			preformSearch: preformSearch,
			executeContract: executeContract,
			getExcecutedContracts: getExcecutedContracts,
			deleteExecution: deleteExecution,
			searchPacket : searchPacket,
			saveEditPacket : saveEditPacket,
			createPacket : createPacket
		}
		
		function saveEditPacket(packet){
			var deferred = $q.defer();

			$http.put('/rest/packet/' + packet.id + '', packet)
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}
		
		function createPacket(packet){
			var deferred = $q.defer();

			$http.post('/rest/packet', packet)
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			}); 	

			return deferred.promise;
		}
		
		function searchPacket(data){
			
		}
		
		function preformSearch(query) {
			var deferred = $q.defer();

			$http.get('/rest/contract/search', {
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

		function executeContract(id) {
			var deferred = $q.defer();

			$http.post('/rest/contract/' + id)
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			}); 	

			return deferred.promise;
		}
		
		function deleteExecution(id) {
			var deferred = $q.defer();

			$http.get('/rest/contract/executed/' + id)
			.success(function(data){
				deferred.resolve(data)
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}
		
		function getExcecutedContracts(query) {
			var deferred = $q.defer();

			$http.get('/rest/contract/executed', {
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


	});


})(angular);