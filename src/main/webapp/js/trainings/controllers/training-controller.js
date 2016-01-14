(function(angular) {

	angular.module('csf').controller('TrainingController', controller);

	function controller($scope, $http, $filter, TrainingService, NgTableParams) {
		var vm = this;
		vm.datePicker = {
			date : {
				startDate : moment().subtract(1,'days'),
				endDate : moment().add(1,'days')
			}
		};

		vm.trainings = new NgTableParams({}, {
			dataset : null
		})

		vm.searchTrainings = searchTrainings;
		vm.deleteTrainings = deleteTraining;
		// vm.calculateTotalContract = calculateTotalContract;
		// vm.calculateTotalContractsPaid = calculateTotalContractsPaid;
		// vm.calculateTotalContracstUnpaid = calculateTotalContracstUnpaid;
		// vm.getSumPerPacket = getSumPerPacket;
		// vm.getQuantityPerPacket = getQuantityPerPacket;

		render();

		function render() {
//			vm.datePicker.date.startDate = vm.datePicker.date.startDate
//					.format('DD-MM-YYYY');
//			vm.datePicker.date.endDate = vm.datePicker.date.endDate
//					.format('DD-MM-YYYY');
			searchTrainings();
		}

		function searchTrainings() {
			TrainingService.search({
				fromDate : vm.datePicker.date.startDate.format('DD-MM-YYYY'),
				toDate : vm.datePicker.date.endDate.format('DD-MM-YYYY')
			}).then(function(data) {
				data.filter(function(el) {
					el.usageDate = $filter('date')(el.usageDate, "dd-MM-yyyy");
					el.name = el.user.name;
					el.startsAt = el.timeSlot.startsAt;
					el.surname = el.user.surname;
				});
				vm.trainings = new NgTableParams({}, {
					dataset : data
				});
			});
		}

		function deleteTraining(slotUsageId) {
			console.log("NIY")
		}

		// function getQuantityPerPacket(data) {
		// var map = new Object();
		//
		// for (var i = 0; i < data.length; i++) {
		// if ((data[i].packet.name in map)) {
		// map[data[i].packet.name] = map[data[i].packet.name] + 1;
		// } else {
		// map[data[i].packet.name] = 1;
		// }
		// }
		// return map;
		//
		// }
		//
		// function getSumPerPacket(data) {
		// var map = new Object();
		//
		// for (var i = 0; i < data.length; i++) {
		// if ((data[i].packet.name in map)) {
		// if (data[i].active === 'Placeno') {
		// map[data[i].packet.name] = map[data[i].packet.name]
		// + parseInt(data[i].packet.price);
		// }
		// } else {
		// if (data[i].active === 'Placeno') {
		// map[data[i].packet.name] = parseInt(data[i].packet.price);
		// } else {
		// map[data[i].packet.name] = 0;
		// }
		// }
		// }
		// return map;
		//
		// }
		//
		// function calculateTotalContract(data) {
		// var total = 0;
		// if (data && data.length > 0) {
		// for (var i = 0; i < data.length; i++) {
		// total += parseInt(data[i].packet.price);
		// }
		// }
		// return total;
		// }
		//
		// function calculateTotalContractsPaid(data) {
		// var total = 0;
		// if (data && data.length > 0) {
		// for (var i = 0; i < data.length; i++) {
		// if (data[i].active === 'Placeno')
		// total += parseInt(data[i].packet.price);
		// }
		// }
		// return total;
		// }
		//
		// function calculateTotalContracstUnpaid(data) {
		// var total = 0;
		// if (data && data.length > 0) {
		// for (var i = 0; i < data.length; i++) {
		// console.log(data[i].active)
		// if (data[i].active === 'Nije Placeno')
		// total += parseInt(data[i].packet.price);
		// }
		// }
		// return total;
		// }

	}

})(angular);