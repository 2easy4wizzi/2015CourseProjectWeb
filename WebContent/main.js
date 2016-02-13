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
						headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
					}).success(function(response) {
						
				if (response == "") {
					//window.location = "\loginAndRegister.html";
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
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
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
		});
	}
    
	}]);
	
	

app.directive("navDirective", function() {
	    return {
	        templateUrl : "navBar.html"
	    };
	});

app.controller('askQuestionC', ['$scope', '$http',
      function($scope, $http){
      	$scope.questionText = null;
      	$scope.topic = null;
      	$scope.clearQuestion = function()
      	{
      		$scope.questionText="";  
      		$scope.topic="";  
      	} 
      	$scope.postQuestion = function(){
      		if($scope.questionText == null || $scope.questionText == "")
  			{
      			$scope.questionText="you must write some text...";  
      			return;
  			}
      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/QuestionsServlet/PostQuestion',
				params : { questionText: $scope.questionText , topics: $scope.topic},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					window.location = "\homePage.html";
				}).error(function(error) {
					alert('somthing happend at post question');
				});
      		}
}]);

app.controller('newQuestionsC', ['$scope', '$http',
                            function($scope, $http){
		$scope.from = 0;
		$scope.questions = "";
		$scope.answer_button = "click to Answer";

		$scope.getTop20NewQuestions = function(from)
		{
			
			$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/QuestionsServlet/GetNewTop20',
				params : { top20from: $scope.from},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
			}).success(function(response) 
				{
					if (response == 0) //0 for no questions found
					{
						alert('no Questions found');
						$scope.questions = "";
						//$scope.questions = {"Qid":1,"Qvotes":0,"QuestionText":"a","QTopics":"nothingYet","OwnerNickname":"wizzi","Created":"2016-02-08 20:27:39.335","QRating":0.0}
					} 
					else 
					{
						$scope.questions = response;	
					}
					}).error(function(error) {
						alert('somthing happend at getNewTop20 top20new');
						
					});
			}
		
		$scope.prev = function(){
			$scope.from--;
			$scope.getTop20NewQuestions($scope.from);
		}
		$scope.next = function(){
			$scope.from++;
			$scope.getTop20NewQuestions($scope.from);
		}
		$scope.getAnswerButton = function(answer_button,index)
		{
			if(answer_button == "click to Answer")
				{alert(index);
				$scope[index].answer_button = 'asd';}
			else
				{answer_button = "click to Answer";}
		}
		$scope.getTimeStamp = function(tsSQL)
		{
			/*date = new Date(tsSQL * 1000);
			datevalues  = [
			              date.getFullYear(),
			              date.getMonth()+1,
			              date.getDate(),
			              date.getHours(),
			              date.getMinutes(),
			              date.getSeconds(),
			           ];
			var date2 = new Date();*/
			return tsSQL;
		}

		$scope.incQuestionAnswers = function(qid)
		{
      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/QuestionsServlet/incQuestionAnswers',
				params : { qid: qid },
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					
				}).error(function(error) {
					alert('somthing happend at inc question answers');
				});
      		}
		$scope.postAnswer = function(qid,answerText)
		{
			if(answerText == null || answerText == "")
			{  
				return;
			}
			$http(
					{
						method : 'POST',
						url : 'http://localhost:8080/webGilad/AnswersServlet/PostAnswer',
						params : { answerText: answerText , qid: qid},
						headers : { 'Content-Type' : 'application/x-www-form-urlencoded' }
					}).success(function(response) 
						{
							alert('post answer succeeded');
							$scope.incQuestionAnswers(qid);
							$scope.getTop20NewQuestions($scope.from);
							if (response == 0) 
							{
								
							} 
							else 
							{
												
							}
						}).error(function(error) {
							alert('somthing happend at getNewTop20 top20new');
							
						});
		}
}]);


app.controller('answerC', ['$scope', '$http',
                            function($scope, $http)
{
            alert('in ansC');                	
    	$scope.getAnswers = function(qid){
    		alert('in ansC'); 
    		$http(
			{
			method : 'POST',
			url : 'http://localhost:8080/webGilad/AnswersServlet/GetAnswers',
			params : { qid: qid },
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
			}).success(function(response) 
			{
				alert('returned from servelet with respons = ' + response);
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