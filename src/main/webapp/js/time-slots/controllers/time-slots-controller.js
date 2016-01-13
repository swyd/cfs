(function(angular) {

	angular.module('csf').controller('TimeSlotsController', controller);

	function controller($scope, $http, $q, TimeSlotsService, $filter,
			NgTableParams) {
		var vm = this;
		vm.date = moment().format('MM/DD/YYYY');
		vm.today = moment().format('MM/DD/YYYY');
		vm.tommorow = moment().add('days', 1).format('MM/DD/YYYY');
		vm.isTommorow = false;
		vm.timeSlots = [];
		vm.timeSlotsWithRemaining = {};
		vm.appointment = {};
		vm.appointments = new NgTableParams({		       
		    }, {
			dataset : null
		})

		vm.findAll = findAll;
		vm.createAppointment = createAppointment;
		vm.findAllWithRemaining = findAllWithRemaining;
		vm.findAllTimeSlotUsage = findAllTimeSlotUsage;
		vm.cancelAppointment = cancelAppointment;
		vm.renderToday = renderToday;
		vm.renderTommorow = renderTommorow;
		vm.setDate = setDate;

		renderToday()

		function setDate(date){
			vm.date = date;
		}
		
		function renderToday() {
			vm.isTommorow = false;
			//console.log(moment().format('MM/DD/YYYY'));
			// TODO add active switch
			findAllTimeSlotUsage(false);
			findAllWithRemaining(false);
		}

		function renderTommorow() {
			vm.isTommorow = true;
			// TODO add active switch
			findAllTimeSlotUsage(true);
			findAllWithRemaining(true);
		}

		function findAll() {
			TimeSlotsService.findAll().then(function(data) {
				// timeslots = [];
				// for(var timeslot in data){
				// timeslots.push(timeslot.startsAt);
				// }
				// vm.timeSlots = data;
			});
		}

		function cancelAppointment(appointmentId) {
			TimeSlotsService.cancelAppointment(appointmentId).then(function() {
				refresh();
			});
		}

		function createAppointment(timeSlotId) {
			TimeSlotsService.createAppointment(timeSlotId, vm.isTommorow).then(function() {
				refresh();
			});
		}

		function refresh() {
			if (vm.isTommorow) {
				renderTommorow();
			} else {
				renderToday();
			}
		}

		function findAllWithRemaining(ist) {
			TimeSlotsService.findAllWithRemaining({
				isTommorow : ist
			}).then(function(data) {
				vm.timeSlotsWithRemaining = data;
			});
		}

		function findAllTimeSlotUsage(ist) {
			TimeSlotsService.findAllTimeSlotUsage({
				isTommorow : ist
			}).then(function(data) {
				data.filter(function(el) {
					el.usageDate = $filter('date')(el.usageDate, "dd/MM/yyyy");
					el.name = el.user.name;
					el.startsAt = el.timeSlot.startsAt;
					el.surname = el.user.surname;
				});
				vm.timeSlots = [];
				for (var i = 0; i < data.length; i++) {
					vm.timeSlots.push(data[i].startsAt);
				}
				vm.appointments = new NgTableParams({
//					filter: { name: firstName}
				}, {
					dataset : data
				});
			});
		}

	}

})(angular);