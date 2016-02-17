app.controller('leaderBoardC', ['$scope', '$http','$location',
                            function($scope, $http,$location){
	$scope.from = 0;
$scope.debuger3 = 'e ';
	
	$scope.getUsers = function()
	{
		$http(
		{
			method : 'POST',
			url : 'http://localhost:8080/webGilad/LeaderBoardServlet/getUsers',
			params : {},
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response) 
			{
				$scope.users = response;	
			}).error(function(error) {
				alert('somthing happend at get20users');
				
			});
		}	
}]);


