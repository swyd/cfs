(function(angular) {

	angular.module('gaintMedia').controller('ContractsController', controller);

	function controller($scope, $http, $filter, ContractService, NgTableParams) {
		var vm = this;
		vm.contracts = new NgTableParams({}, {
			dataset : null
		}), vm.datePicker = {
			date : {
				startDate : null,
				endDate : null
			}
		}, vm.search = {};

		vm.changePaidStatus = changePaidStatus;
		vm.deleteContract = deleteContract;
		vm.searchContract = searchContract;
		vm.calculateTotalContract = calculateTotalContract;
		vm.calculateTotalContractsPaid = calculateTotalContractsPaid;
		vm.calculateTotalContracstUnpaid = calculateTotalContracstUnpaid;
		vm.getSumPerPacket = getSumPerPacket;
		vm.getQuantityPerPacket = getQuantityPerPacket;

		function getQuantityPerPacket(data) {
			var map = new Object();

			for (var i = 0; i < data.length; i++) {
				if ((data[i].packet.name in map)) {
					map[data[i].packet.name] = map[data[i].packet.name] + 1;
				} else {
					map[data[i].packet.name] = 1;
				}
			}
			return map;

		}

		function getSumPerPacket(data) {
			var map = new Object();

			for (var i = 0; i < data.length; i++) {
				if ((data[i].packet.name in map)) {
					if (data[i].active === 'Placeno') {
						map[data[i].packet.name] = map[data[i].packet.name]
								+ parseInt(data[i].packet.price);
					}
				} else {
					if (data[i].active === 'Placeno') {
						map[data[i].packet.name] = parseInt(data[i].packet.price);
					} else {
						map[data[i].packet.name] = 0;
					}
				}
			}
			return map;

		}

		function calculateTotalContract(data) {
			var total = 0;
			if (data && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					total += parseInt(data[i].packet.price);
				}
			}
			return total;
		}

		function calculateTotalContractsPaid(data) {
			var total = 0;
			if (data && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					if (data[i].active === 'Placeno')
						total += parseInt(data[i].packet.price);
				}
			}
			return total;
		}

		function calculateTotalContracstUnpaid(data) {
			var total = 0;
			if (data && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					console.log(data[i].active)
					if (data[i].active === 'Nije Placeno')
						total += parseInt(data[i].packet.price);
				}
			}
			return total;
		}

		activate();

		function activate(data) {
			ContractService.getContracts(data).then(
					function(data) {
						data.filter(function(el) {
							el.creationDate = $filter('date')(el.creationDate,
									"dd/MM/yyyy")
							el.packetName = el.packet.name;
							el.packetPrice = el.packet.price;
							el.active = el.active ? 'Placeno' : 'Nije Placeno';
						});
						vm.contracts = new NgTableParams({}, {
							dataset : data
						});
					});
		}

		function changePaidStatus(status, id) {
			ContractService.updateContractPaidStatus(status, id).then(
					function(data) {
						activate();
					});
		}

		function deleteContract(id) {
			ContractService.deleteContract(id).then(function(data) {
				activate();
			});
		}

		function searchContract() {
			vm.search.fromDate = vm.datePicker.date.startDate ? vm.datePicker.date.startDate
					.format('DDMMYYYY')
					: null;
			vm.search.toDate = vm.datePicker.date.endDate ? vm.datePicker.date.endDate
					.format('DDMMYYYY')
					: null;

			activate(vm.search);
		}
	}

})(angular);