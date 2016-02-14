app.controller('newQuestionsC', ['$scope', '$http',
                            function($scope, $http){
		$scope.from = 0;
		$scope.questions = "";
		
		
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
					} 
					else 
					{
						$scope.questions = response;	
					}
					}).error(function(error) {
						alert('somthing happend at getNewTop20');
						
					});
			}
		$scope.setQuestionInSession = function(qid)
		{
			$http(
					{
						method : 'POST',
						url : 'http://localhost:8080/webGilad/QuestionsServlet/SetQuestionInSession',
						params : { qid : qid},
						headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
					}).success(function(response) 
						{
			
						}).error(function(error) {
							alert(response);	
							alert('somthing happend at set q in session ');
								
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
		/*$scope.getAnswerButton = function(index)
		{
			var str = "answer_button-" + index;
			var current = document.getElementById(str);
			var inner = current.innerHTML.toString();
			if(inner == "hide")
			{current.innerHTML = "show";}
			else
			{current.innerHTML = "hide";}
			
		}*/

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