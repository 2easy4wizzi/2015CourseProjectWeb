//var app = angular.module
/*app.controller('allQuestions', ['$scope', '$http',
                                 function($scope, $http){
	$scope.from = 0;
	$scope.questions = "";
	$scope.answers = "";
	$scope.answer_button = "open Answer box";
	$scope.older_answers_button = "show older Answers";
	
	
	$scope.getTop20Questions = function(from)
	{
		
		$http(
				{
					method : 'POST',
					url : 'http://localhost:8080/webGilad/QuestionsServlet/GetTop20',
					params : { top20from: $scope.from},
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
						{
					if (response == 0) //0 for no questions found
					{
						alert('no Questions found');
					} 
					else 
					{
						$scope.questions = response;	
					}
						}).error(function(error) {
							alert('somthing happend at getTop20 top20');
							
						});
	}
	
	$scope.prev = function(){
		$scope.from--;
		$scope.getTop20Questions($scope.from);
	}
	$scope.next = function(){
		$scope.from++;
		$scope.getTop20Questions($scope.from);
	}
	$scope.getAnswerButton = function(index)
	{
		var str = "answer_button-" + index;
		var current = document.getElementById(str);
		var inner = current.innerHTML.toString();
		if(inner == "hide Answer box")
		{current.innerHTML = "open Answer box";}
		else
		{current.innerHTML = "hide Answer box";}
		
	}
	$scope.getOlderAnswersButton = function(index)
	{
		var str = "older_answers_button-" + index;
		var current = document.getElementById(str);
		var inner = current.innerHTML.toString();
		if(inner == "hide older Answers")
		{current.innerHTML = "show older Answers";}
		else
		{current.innerHTML = "hide older Answers";}
		
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
					$scope.getTop20Questions($scope.from);
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
}]);*/
















/*app.controller('answerC', ['$scope', '$http',
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
  }]);*/