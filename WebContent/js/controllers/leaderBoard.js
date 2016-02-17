app.controller('leaderBoardC', ['$scope', '$http','$location',
                            function($scope, $http,$location){


	
	$scope.getUsers = function(from)
	{
		$http(
		{
			method : 'POST',
			url : 'http://localhost:8080/webGilad/LeaderBoardServlet/getUsers',
			params : { top20from : $scope.from},
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response) 
			{
			//if we wont to display how much users are registered in the system
				/*$scope.dontShowNextButton = response[0];
				if (response[1] == 'Users found in the system') //0 for no questions found
				{	
					$scope.user_title = "Users found in the system";
					$scope.questions = "";
				} */

					$scope.users = response[1];	

				}).error(function(error) {
					alert('somthing happend at get20users');
					
				});
		}	
}]);


