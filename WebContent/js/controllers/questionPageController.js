app.controller('questionPage', ['$scope', '$http',function($scope, $http){
	
	$scope.question ="";
	$scope.answerText = "";
	$scope.getQuestionFromSession = function()
	{
		$http(
				{
					method : 'POST',
					url : 'http://localhost:8080/webGilad/QuestionsServlet/GetQuestionFromSession',
					params : {},
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
					{
					
						if (response == 0) //0 for no questions found
						{
							window.location = "\homePage.html";
							
						} 
						else 
						{
							questions = response;
							$scope.question = questions[0];
						}
						}).error(function(error) {
							alert('somthing happend at get q from session');
							
						});
		}
		$scope.getAnswers = function()
		{
			$http(
					{
						method : 'POST',
						url : 'http://localhost:8080/webGilad/AnswersServlet/GetAnswers',
						params : {},
						headers : { 'Content-Type' : 'application/x-www-form-urlencoded' }
					}).success(function(response) 
						{
						$scope.answers = response;
					
							}).error(function(error) {
								alert('somthing happend at get answers');
								
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
							$scope.incQuestionAnswers(qid);
							$scope.question.Answers++;
							$scope.getAnswers();
							if (response == 0) 
							{
								
							} 
							else 
							{
												
							}
						}).error(function(error) {
							alert('somthing happend at post answer QPage');
							
						});
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
	
		


}]);