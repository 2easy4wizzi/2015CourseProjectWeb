app.controller('homePageC', ['$scope', '$http','$location',
                            function($scope, $http,$location){

	
	var answerPositive = "Answer the question";
	var answerNegative = "Collapse Answer";
	var focus = "";
	$scope.answer_button = answerPositive;
	$scope.from = 0;
	$scope.questions = "";
	$scope.answers = "";
	$scope.dontShowNextButton = false;
	$scope.question_title = "Questions";
	$scope.is_all_questions = false;	
	
	$scope.getQuestions = function(from)
	{
		var s = location.href;			
        var fields = s.split("/");			
        var name = "/" + fields[4];
        if(name == '/homePage.html?tab=AllQuestions')
    	{
        	$scope.is_all_questions = true;
        	focus = "all";
        	$scope.get20questions(from);
    	}
        else
    	{
        	focus = "new";
        	$scope.get20NewQuestions(from);
    	}
	}
	
	$scope.get20NewQuestions = function(from)
	{
		$http(
		{
			method : 'POST',
			url : 'http://localhost:8080/webGilad/QuestionsServlet/GetNewTop20',
			params : { top20from: $scope.from},
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response) 
			{
				$scope.dontShowNextButton = response[0];
				if (response[1] == 'noQuestionsFound') //0 for no questions found
				{	
					$scope.question_title = "no new Questions";
					$scope.questions = "";
				} 
				else 
				{
					$scope.questions = response[1];	
				}
				}).error(function(error) {
					alert('somthing happend at getNewTop20');
					
				});
		}

	$scope.get20questions = function(from)
	{
		
		$http(
				{
					method : 'POST',
					url : 'http://localhost:8080/webGilad/QuestionsServlet/GetTop20',
					params : { top20from: $scope.from},
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
					{
		
					$scope.dontShowNextButton = response[0];
					if (response[1] == 'noQuestionsFound') //0 for no questions found
					{	
						$scope.question_title = "no new Questions";
						$scope.questions = "";
					} 
					else 
					{
						$scope.questions = response[1];	
					}
					}).error(function(error) {
						alert('somthing happend at get Top20 -not NEW');
						
					});
						
	}
	
	$scope.getAnswerButtonText = function(index)
	{
		var str = "answer_button-" + index;
		var current = document.getElementById(str);
		var inner = current.innerHTML.toString();
		if(inner == answerNegative)
		{current.innerHTML = answerPositive;}
		else
		{current.innerHTML = answerNegative;}
		
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
					(focus == "all") ?  $scope.get20questions($scope.from) : $scope.get20NewQuestions($scope.from);
					
						}).error(function(error) {
							alert('somthing happend at postAnswer');
							
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
	
	$scope.prev = function(){
		$scope.from--;
		(focus == "all") ?  $scope.get20questions($scope.from) : $scope.get20NewQuestions($scope.from);
	}
	$scope.next = function(){
		$scope.from++;
		(focus == "all") ?  $scope.get20questions($scope.from) : $scope.get20NewQuestions($scope.from);	}
	
	$scope.getAnswers = function(qid){
		
		$http(
		{
		method : 'POST',
		url : 'http://localhost:8080/webGilad/AnswersServlet/GetAnswers',
		params : { qid: qid },
		headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response) 
		{
			$scope.answers = response;
			if (response == "") {
				
			} 
			else 
			{
				
			}
		}).error(function(error) {
			alert('somthing happend at post question');
					
				});
    		}
	
	
}]);

/*
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
		
*/