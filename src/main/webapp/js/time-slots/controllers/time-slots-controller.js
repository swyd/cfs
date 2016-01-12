(function(angular) {

	angular.module('csf').controller('TimeSlotsController', controller);

	function controller($scope, $http, $q, TimeSlotsService, $filter,
			NgTableParams) {
		var vm = this;
		vm.timeSlots = [];
		vm.timeSlotsWithRemaining = {};
		vm.appointment = {};
		vm.appointments = new NgTableParams({}, {
			dataset : null
		})

		vm.findAll = findAll;
		vm.createAppointment = createAppointment;
		vm.findAllWithRemaining = findAllWithRemaining;
		vm.findAllTimeSlotUsage = findAllTimeSlotUsage;

		findAllTimeSlotUsage();
		findAllWithRemaining();
		
		function findAll() {
			TimeSlotsService.findAll().then(function(data) {
//				timeslots = [];
//				for(var timeslot in data){
//					timeslots.push(timeslot.startsAt);
//				}
//				vm.timeSlots = data;
			});
		}

		function createAppointment(timeSlotId) {
			TimeSlotsService.createAppointment(timeSlotId).then(function() {
				refresh();
			});
		}

		function refresh() {
			findAllWithRemaining();
			findAllTimeSlotUsage();
		}

		function findAllWithRemaining() {
			TimeSlotsService.findAllWithRemaining().then(function(data) {
				vm.timeSlotsWithRemaining = data;
			});
		}

		function findAllTimeSlotUsage() {
			TimeSlotsService.findAllTimeSlotUsage().then(function(data) {
				data.filter(function(el) {
					el.usageDate = $filter('date')(el.usageDate, "dd/MM/yyyy");
					el.name = el.user.name;
					el.startsAt = el.timeSlot.startsAt;
					el.surname = el.user.surname;
				});
				vm.timeSlots = [];
				for(var i = 0; i < data.length; i++){
					vm.timeSlots.push(data[i].startsAt);
				}
				vm.appointments = new NgTableParams({}, {
					dataset : data
				});
			});
		}

	}

})(angular);