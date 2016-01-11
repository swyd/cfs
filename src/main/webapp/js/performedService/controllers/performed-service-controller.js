(function(angular) {

	angular.module('gaintMedia').controller('PerformedServiceController',
			controller);

	function controller($scope, $http, PerformedService, $filter, NgTableParams) {
		var vm = this;
		vm.contracts = null;
		vm.packet = {}, vm.packets = new NgTableParams({}, {
			dataset : null
		}), vm.editingInProgress = false;
		vm.datePicker = {
			date : {
				startDate : null,
				endDate : null
			}
		}, vm.contracts = new NgTableParams({}, {
			dataset : null
		}), vm.executedContracts = new NgTableParams({}, {
			dataset : null
		});

		vm.search = search;
		vm.executeContract = executeContract;
		vm.searchExecutedContracts = searchExecutedContracts;
		vm.deleteExecution = deleteExecution;
		vm.calculateTotalContract = calculateTotalContract;
		vm.calculateTotalContractExecution = calculateTotalContractExecution;
		vm.calculateTotalContractsPaid = calculateTotalContractsPaid;
		vm.calculateTotalContractsUnpaid = calculateTotalContractsUnpaid;
		vm.searchPacket = searchPacket;
		vm.saveEditedPacket = saveEditedPacket;
		vm.createPacket = createPacket;
		vm.editPacket = editPacket;

		function searchPacket() {
			$http.get('/rest/packet').success(function(data) {
				data.filter(function(el) {

				});

				vm.packets = new NgTableParams({}, {
					dataset : data
				});
			});
		}

		function createPacket() {
			PerformedService.createPacket(vm.packet).then(function(data) {
				vm.user.username = null;
				vm.user.password = null;
				vm.user.name = null;
				vm.user.id = null;
				searchPacket();
			});
		}

		function editPacket(packet) {
			vm.editingInProgress = true;

			vm.packet.name = packet.name;
			vm.packet.usage = packet.usage;
			// if(packet.isActive === "Jeste"){
			vm.packet.isActive = packet.isActive;
			// }else{
			// }
			vm.packet.price = packet.price;
			vm.packet.id = packet.id;
		}

		function saveEditedPacket() {
			PerformedService.saveEditPacket(vm.packet).then(function(data) {
				vm.editingInProgress = false;

				vm.packet.name = null;
				vm.packet.usage = null;
				vm.packet.isActive = null;
				vm.packet.price = null;
				vm.packet.id = null;
				searchPacket();
			});
		}

		function calculateTotalContractExecution(data) {
			var total = 0;
			if (data && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					total += parseInt(data[i].contract.packet.price);
				}
			}
			return total;
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
					if (data[i].contract.active)
						total += 1;
				}
			}
			return total;
		}

		function calculateTotalContractsUnpaid(data) {
			var total = 0;
			if (data && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					if (!data[i].contract.active)
						total += 1;
				}
			}
			return total;
		}

		function search() {
			vm.contracts = null;
			if (vm.preformedService) {
				PerformedService
						.preformSearch(
								{
									brojUgovora : vm.preformedService.contractNumber,
									brojSasije : vm.preformedService.chassisNumber,
									regOznaka : vm.preformedService.regNumber,
									punoime : vm.preformedService.fullName,
									fromDate : encodeURIComponent(vm.preformedService.datePicker.date.startDate
											.format('DDMMYYYY')),
									toDate : encodeURIComponent(vm.preformedService.datePicker.date.endDate
											.format('DDMMYYYY'))
								}).then(
								function(data) {
									// vm.contracts = data;
									data.filter(function(el) {
										el.creationDate = $filter('date')(
												el.creationDate, "dd/MM/yyyy")
										el.packetName = el.packet.name;
										el.packetPrice = el.packet.price;
									});

									vm.contracts = new NgTableParams({}, {
										dataset : data
									});
								});
			} else {
				PerformedService.preformSearch().then(
						function(data) {
							// vm.contracts = data;
							data.filter(function(el) {
								el.creationDate = $filter('date')(
										el.creationDate, "dd/MM/yyyy")
								el.packetName = el.packet.name;
								el.packetPrice = el.packet.price;
							});

							vm.contracts = new NgTableParams({}, {
								dataset : data
							});
						});
			}
		}

		function executeContract(id) {
			PerformedService.executeContract(id).then(function(data) {
				search();
			})
		}

		function deleteExecution(id) {
			PerformedService.deleteExecution(id).then(function() {
				vm.packet = {};
				searchExecutedContracts();
			})
		}

		function searchExecutedContracts() {
			vm.executedContracts = new NgTableParams({}, {
				dataset : null
			});
			if (vm.datePicker.date.startDate === null
					|| vm.datePicker.date.endDate === null) {
				PerformedService.getExcecutedContracts({
					fromDate : null,
					toDate : null
				}).then(function(data) {

					var newData = [];

					data.filter(function(el) {
						el.contractId = el.contract.id;
						el.name = el.contract.name;
						el.licencePlate = el.contract.licencePlate;
						el.vehicle = el.contract.vehicle;
						el.date = $filter('date')(el.date, "dd/MM/yyyy")
						el.packetName = el.contract.packet.name;
						el.packetPrice = el.contract.packet.price;
						newData.push(el);
					});

					vm.executedContracts = new NgTableParams({}, {
						dataset : newData
					});
				});

			} else {
				PerformedService
						.getExcecutedContracts(
								{
									fromDate : encodeURIComponent(vm.datePicker.date.startDate
											.format('DDMMYYYY')),
									toDate : encodeURIComponent(vm.datePicker.date.endDate
											.format('DDMMYYYY'))
								})
						.then(
								function(data) {

									var newData = [];

									data
											.filter(function(el) {
												el.contractId = el.contract.id;
												el.name = el.contract.name;
												el.licencePlate = el.contract.licencePlate;
												el.vehicle = el.contract.vehicle;
												el.date = $filter('date')(
														el.date, "dd/MM/yyyy")
												el.packetName = el.contract.packet.name;
												el.packetPrice = el.contract.packet.price;
												newData.push(el);
											});

									vm.executedContracts = new NgTableParams(
											{}, {
												dataset : newData
											});
								});
			}
		}
	}

})(angular);