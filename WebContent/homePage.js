
angular.module('homePage', [])
	.controller('homePageController', ['$scope', '$http',
	                                   function($scope, $http){
	$scope.test = 0;
	$scope.username = function()
	{
		  if ($scope.test == 0) {
			$http(
					{
						method : 'POST',
						url : 'http://localhost:8080/webGilad/GetSessionUserNameServlet',
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(response) {
				//alert(response);
				$scope.name = response;
				if (response == "") {
					window.location = "\loginAndRegister.html";
					//send do home page
				} else {

				}
			}).error(function(error) {
				alert('login error');
				// $scope.status = 'Unable to connect' + error.message;
			});
		}
		$scope.test = 1;
		
	}
    
	}]);