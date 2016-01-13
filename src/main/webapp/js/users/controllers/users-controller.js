(function(angular) {

	angular.module('csf').controller('UsersController', controller);

	function controller($scope, $http, UsersService, $filter, NgTableParams) {
		var vm = this;
		vm.user = {}, vm.users = new NgTableParams({}, {
			dataset : null
		}), vm.editingInProgress = false;

		vm.createUser = createUser;
		vm.changeRole = changeRole;
		vm.editUser = editUser;
		vm.saveEditedUser = saveEditedUser;
		vm.deleteUser = deleteUser;

		activate();

		function activate() {
			UsersService.getUsers().then(function(data) {
				data.filter(function(el) {
					el.datePaid = $filter('date')(el.datePaid, "dd-MM-yyyy");
					el.dateExpiring = $filter('date')(el.dateExpiring, "dd-MM-yyyy");
				});
				vm.users = new NgTableParams({}, {
					dataset : data
				});
			});
		}

		function createUser() {
			UsersService.createUser(vm.user).then(function(data) {
				vm.user.username = null;
				vm.user.password = null;
				vm.user.name = null;
				vm.user.surname = null;
				vm.user.email = null;
				vm.user.sessionsLeft = null;
				vm.user.isAdmin = null;
				vm.user.isActive = null;
				vm.user.datePaid = null;
				vm.user.dateExpiring = null;

				vm.user.id = null;
				activate();
			});
		}

		function deleteUser(id) {
			UsersService.deleteUser(id).then(function() {
				vm.user.username = null;
				vm.user.password = null;
				vm.user.name = null;
				vm.user.surname = null;
				vm.user.email = null;
				vm.user.sessionsLeft = null;
				vm.user.isAdmin = null;
				vm.user.isActive = null;
				vm.user.datePaid = null;
				vm.user.dateExpiring = null;
				vm.user.id = null;
				activate();
			})
		}

		function changeRole(id) {
			UsersService.changeRole(id).then(function(data) {
				activate();
			});
		}

		function editUser(user) {
			vm.editingInProgress = true;
			
			vm.user.username = user.username;
			vm.user.password = user.password;
			vm.user.name = user.name;
			vm.user.surname = user.surname;
			vm.user.email = user.email;
			vm.user.sessionsLeft = user.sessionsLeft;
			vm.user.isAdmin = user.isAdmin;
			vm.user.isActive = user.isActive;
			vm.user.datePaid = user.datePaid;
			vm.user.dateExpiring = user.dateExpiring;
			vm.user.id = user.id;

		}

		function saveEditedUser() {
			
			vm.user.datePaid = moment(vm.user.datePaid).format('DD-MM-YYYY');
			vm.user.dateExpiring = moment(vm.user.dateExpiring).format('DD-MM-YYYY');
			UsersService.editUser(vm.user).then(function(data) {
				vm.editingInProgress = false;

				vm.user.username = null;
				vm.user.password = null;
				vm.user.name = null;
				vm.user.surname = null;
				vm.user.email = null;
				vm.user.sessionsLeft = null;
				vm.user.isAdmin = null;
				vm.user.isActive = null;
				vm.user.datePaid = null;
				vm.user.dateExpiring = null;
				vm.user.id = null;
				activate();
			});
		}

	}

})(angular);