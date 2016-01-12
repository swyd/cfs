(function(angular){
	
	angular.module('csf').factory('ContractService', function($http, $q) {
		
		return {
			getContracts: getContracts,
			updateContractPaidStatus: updateContractPaidStatus,
			deleteContract: deleteContract
		}

		function getContracts(data) {
			var deferred = $q.defer();

			$http.get('/rest/contract/all',
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

		function updateContractPaidStatus(status, id) {
			var deferred = $q.defer();

			$http.post('/rest/contract/' + id + '/'+ status +'')
			.success(function(data){
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			return deferred.promise;
		}

		function deleteContract(id) {
			var deferred = $q.defer();

			$http.delete('/rest/contract/' + id +'')
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