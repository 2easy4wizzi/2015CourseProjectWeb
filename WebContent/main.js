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
					//window.location = "\loginAndRegister.html";
					//send do home page
					//alert('no one is logged in')
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
		$scope.from = 0;
		$scope.questions = "";
		$scope.getNewTop20 = function(from)
		{
			
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
					if (response == 0) //0 for no questions found
					{
						alert('in if');
						//$scope.questions = {"Qid":1,"Qvotes":0,"QuestionText":"a","QTopics":"nothingYet","OwnerNickname":"wizzi","Created":"2016-02-08 20:27:39.335","QRating":0.0}
					} 
					else 
					{
						$scope.questions = response;					
					}
					}).error(function(error) {
						alert('somthing happend at ctor top20new');
						
					});
			}
}]);