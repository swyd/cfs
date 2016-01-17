(function(angular) {

	angular.module('csf').controller('SingleUserController', controller);

	function controller($scope, $http, SingleUserService, $filter,
			NgTableParams) {
		var vm = this;
		vm.user = {};
		vm.showModal = false;
		vm.password = {};
		
		vm.saveUser = saveUser;
		vm.toggleModal = toggleModal;
		vm.changePassword = changePassword;
		
		activate();

		function activate() {
			SingleUserService.getUser().then(
					function(data) {
						vm.user = data;
					});
		}
		
		function toggleModal(){
	        vm.showModal = !vm.showModal;
	    }
		
		function changePassword(oldPassword, newPassword){
			SingleUserService.changePassword($.param({
				oldPass : oldPassword,
				newPass : newPassword
			})).then(function(data){
				vm.user = data;
			});
		}
		
		function saveUser() {
			SingleUserService.createUser(vm.user).then(function(data) {
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