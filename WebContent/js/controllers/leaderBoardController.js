app.controller('leaderBoardC', ['$scope', '$http','$location',
                            function($scope, $http,$location){
	$scope.from = 0;
	
	
	$scope.getUsers = function()/*brings 20 top user*/
	{
		$http(
		{
			method : 'GET',
			url : projectUrl + LeaderBoardServlet+ 'getUsers',
			params : {},
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response) 
			{
				$scope.users = response;	
			}).error(function(error) {
				console.log('somthing happend at get20users');
				
			});
		}	
	
}]);


