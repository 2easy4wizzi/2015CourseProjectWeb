
angular.module('homePage', [])
	.controller('homePageController', ['$scope', '$http',
    function($scope, $http){
	$scope.test = 0;
	$scope.name = "Guest";
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
						
				if (response == "") {
					window.location = "\loginAndRegister.html";
					//send do home page
				} 
				else 
				{
					alert(response);
					$scope.name = response;
				}
			}).error(function(error) {
				alert('somthing happend');
				// $scope.status = 'Unable to connect' + error.message;
			});
		}
		$scope.test = 1;
		
	}
    
	}]);