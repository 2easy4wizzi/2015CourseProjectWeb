var app = angular.module("main", []);

//app.module('myApp', [])

app.controller('navC', ['$scope', '$http','$location',                         
    function($scope, $http, $location){
	
	$scope.isActive = function (viewLocation) 
	{			
         var s = location.href;			
         var fields = s.split("/");			
         var name = "/" + fields[4];			
	
         if(viewLocation == name) { return true;}						
	};
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
				alert('somthing happend at get user name ');
				
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
      	$scope.questionText = null;
      	$scope.AskClear = function()
      	{
      		$scope.questionText="";  
      	} 
      	$scope.AskPost = function(){
      		if($scope.questionText == null || $scope.questionText == "")
  			{
      			$scope.questionText="you must write some text...";  
      			return;
  			}
      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/QuestionsServlet/PostQuestion',
				params : { questionText: $scope.questionText , topics: 'nothingYet'},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
				}).success(function(response) 
				{
					window.location = "\homePage.html";
					if (response == "") {
						
						//send do home page
					} 
					else 
					{
						
					}
				}).error(function(error) {
					alert('somthing happend at post question');
					
				});
      		}
}]);

app.controller('newQuestionsC', ['$scope', '$http',
                            function($scope, $http){
		$http(
		{
			method : 'POST',
			url : 'http://localhost:8080/webGilad/QuestionsServlet/GetNewTop20',
			params : { top20from: '0'},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
			}).success(function(response) 
			{
				$scope.questions = response;
				if (response == "") {
					
					//send do home pages
				} 
				else 
				{
					
				}
			}).error(function(error) {
				alert('somthing happend at ctor top20new');
				
			});
		  		
	
	
					
	
}]);