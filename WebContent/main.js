var app = angular.module("main", []);

//app.module('myApp', [])

app.controller('mainC', ['$scope', '$http',
    function($scope, $http){
	$scope.test = 0;
	$scope.name = "Guest";
	$scope.username = function()
	{
		  if ($scope.test == 0) {
			$http(
					{
						method : 'POST',
						url : 'http://localhost:8080/webGilad/GetSessionUserNameServlet/GetUsername',
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
					$scope.name = response;
				}
			}).error(function(error) {
				alert('somthing happend');
				
			});
		}
		$scope.test = 1;
	}
	$scope.removeAtt = function(){
		$http(
				{
					method : 'POST',
					url : 'http://localhost:8080/webGilad/GetSessionUserNameServlet/RemoveAtt',
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
				alert('goodbye '+ response);
				$scope.name = response;
			}
		}).error(function(error) {
			alert('remove att somthing happend');
			// $scope.status = 'Unable to connect' + error.message;
		});
	}
    
	}]);
	
	

app.directive("navDirective", function() {
	    return {
	        templateUrl : "navBar.html"
	    };
	});

app.controller('askQuesC', ['$scope', '$http',
                                  function($scope, $http){
                    	          	$scope.question = null;
                    	          	$scope.Clear = function()
                    	          	{
                    	          		$scope.question="";  
                    	          	} 
                    }]);