	(function(angular){

	angular.module('gaintMedia')
	.controller('UsersController', controller);

	function controller($scope, $http, UsersService, NgTableParams){
		var vm = this;
			vm.user = {},
			vm.users = new NgTableParams({}, { dataset: null }),
			vm.editingInProgress = false;

		vm.createUser = createUser;
		vm.changeRole = changeRole;
		vm.editUser = editUser;
		vm.saveEditedUser = saveEditedUser;
		vm.deleteUser = deleteUser;

		activate();

		function activate() {
			UsersService.getUsers().then(function(data) {
				vm.users = new NgTableParams({}, { dataset: data });
			});
		}
		
		function createUser() {
			UsersService.createUser(vm.user).then(function(data) {
				vm.user.username = null;
				vm.user.password = null;
				vm.user.name = null;
				vm.user.isAdmin = null;
				vm.user.id = null;
				activate();
			});
		}
		
		function deleteUser(id) {
			UsersService.deleteUser(id).then(function(){
				vm.user.username = null;
				vm.user.password = null;
				vm.user.name = null;
				vm.user.isAdmin = null;
				vm.user.id = null;
				activate();
			})
		}
		
		function changeRole(id){
			UsersService.changeRole(id).then(function(data){
				activate();
			});
		}

		function editUser(user) {
			vm.editingInProgress = true;

			vm.user.username = user.username;
			vm.user.password = user.password;
			vm.user.name = user.name;
			vm.user.isAdmin = user.isAdmin;
			vm.user.id = user.id;
		}

		function saveEditedUser() {
			UsersService.editUser(vm.user).then(function(data) {
				vm.editingInProgress = false;

				vm.user.username = null;
				vm.user.password = null;
				vm.user.name = null;
				vm.user.isAdmin = null;
				vm.user.id = null;
				activate();
			});
		}

	}

})(angular);