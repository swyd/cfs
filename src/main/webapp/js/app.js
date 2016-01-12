angular
		.module(
				'csf',
				[ 'ngRoute', 'ngCookies', 'ngMessages', 'csf.services',
						'daterangepicker', 'ngTable' ])
		.config(
				[
						'$routeProvider',
						'$locationProvider',
						'$httpProvider',
						function($routeProvider, $locationProvider,
								$httpProvider) {

							$routeProvider
									.when('/login', {
										templateUrl : 'partials/login.html',
										controller : LoginController
									})
									.when(
											'/time-slots',
											{
												templateUrl : 'partials/time-slots.html',
												controller : 'TimeSlotsController',
												controllerAs : 'vm'
											})
									.when(
											'/user-management',
											{
												templateUrl : 'partials/user-management-template.html',
												controller : 'UsersController',
												controllerAs : 'vm'
											})
									.otherwise({
										templateUrl : 'partials/index.html',
										controller : IndexController,
										controllerAs : 'vm'
									});

							$locationProvider.hashPrefix('!');

							/*
							 * Register error provider that shows message on
							 * failed requests or redirects to login page on
							 * unauthenticated requests
							 */
							$httpProvider.interceptors
									.push(function($q, $rootScope, $location) {
										return {
											'responseError' : function(
													rejection) {
												var status = rejection.status;
												var config = rejection.config;
												var method = config.method;
												var url = config.url;

												if (status == 401
														|| status == 403) {
													$location.path("/login");
													$rootScope.error = rejection.data.message;
												} else {
													$rootScope.error = rejection.data.message;
												}

												return $q.reject(rejection);
											}
										};
									});

							/*
							 * Registers auth token interceptor, auth token is
							 * either passed by header or by query parameter as
							 * soon as there is an authenticated user
							 */
							$httpProvider.interceptors
									.push(function($q, $rootScope, $location) {
										return {
											'request' : function(config) {
												var isRestCall = config.url
														.indexOf('rest') !== -1;
												console.log(config.url.indexOf('login'));
												console.log(config.url.indexOf('login') == -1)
												var isLoginCall = config.url.indexOf('login')== -1;
//												config.headers['X-Auth-Token'] = null;
												if (isRestCall
														&& angular
																.isDefined($rootScope.authToken)) {
													var authToken = $rootScope.authToken;
													if (exampleAppConfig.useAuthTokenHeader) {
														config.headers['X-Auth-Token'] = authToken;
													} else {
														config.url = config.url
																+ "?token="
																+ authToken;
													}
												}
												return config
														|| $q.when(config);
											}
										};
									});

						} ]

		).directive('ngConfirmClick', [ function() {
			return {
				link : function(scope, element, attr) {
					var msg = attr.ngConfirmClick || "Da li ste sigurni?";
					var clickAction = attr.confirmedClick;
					element.bind('click', function(event) {
						if (window.confirm(msg)) {
							scope.$eval(clickAction)
						}
					});
				}
			};
		} ]).run(function($rootScope, $location, $cookieStore, UserService) {

			/* Reset error when a new view is loaded */
			$rootScope.$on('$viewContentLoaded', function() {
				delete $rootScope.error;
			});

			$rootScope.hasRole = function(role) {

				if ($rootScope.user === undefined) {
					return false;
				}

				if ($rootScope.user.roles[role] === undefined) {
					return false;
				}

				return $rootScope.user.roles[role];
			};

			$rootScope.logout = function() {
				delete $rootScope.user;
				delete $rootScope.authToken;
				$cookieStore.remove('authToken');
				$location.path("/login");
			};

			/* Try getting valid user from cookie or go to login page */
			var originalPath = $location.path();
			$location.path("/login");
			var authToken = $cookieStore.get('authToken');
			if (authToken !== undefined) {
				$rootScope.authToken = authToken;
				UserService.get(function(user) {
					$rootScope.user = user;
					$location.path(originalPath);
				});
			}

			$rootScope.initialized = true;
		});

function IndexController($scope, $http, NewsService) {
	var vm = this;
//	vm.docs = [];

//	$http.get('./downloads/').success(function(data) {
//		$('a', data).each(function(index, el) {
//			if (index > 0) {
//				vm.docs.push($.trim($(el).text()));
//			}
//		});
//	});
};

function EditController($scope, $routeParams, $location, NewsService) {

	$scope.newsEntry = NewsService.get({
		id : $routeParams.id
	});

	$scope.save = function() {
		$scope.newsEntry.$save(function() {
			$location.path('/');
		});
	};
};

function CreateController($scope, $location, NewsService) {

	$scope.newsEntry = new NewsService();

	$scope.save = function() {
		$scope.newsEntry.$save(function() {
			$location.path('/');
		});
	};
};

function LoginController($scope, $rootScope, $location, $cookieStore,
		UserService) {

	$scope.rememberMe = true;

	$scope.login = function() {
		UserService.authenticate($.param({
			username : $scope.username,
			password : $scope.password
		}), function(authenticationResult) {
			var authToken = authenticationResult.token;
			$rootScope.authToken = authToken;
			$cookieStore.put('authToken', authToken);
			UserService.get(function(user) {
				$rootScope.user = user;
				$location.path("/");
			});
		});
	};
};
