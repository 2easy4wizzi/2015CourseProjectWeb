app.controller('homePageC', ['$scope', '$http','$location',
                            function($scope, $http,$location){

	
	var answerPositive = "Answer the question";
	var answerNegative = "Collapse Answer";
	var focus = "";
	var arrayInizialize = false;
	$scope.answer_button = answerPositive;
	$scope.from = 0;
	$scope.questions = "";
	$scope.answers = "";
	$scope.dontShowNextButton = false;
	$scope.question_title = "Questions";
	$scope.more_answers = "show more answers";
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
        	$scope.is_all_questions = false;
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
				
					if(arrayInizialize == false)
					{
						
						$scope.how_much_to_show = [];
						for (var i=0;i<$scope.questions.length;i++) 
						{
							//$scope.how_much_to_show.push({show:1 ,button:false});//how much to show
							$scope.how_much_to_show.push({show:1 ,button:false});//how much to show
								
						}
						arrayInizialize = true;
						
					}
					else
					{
						
					}
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
	
	$scope.postAnswer = function(qid,answerText,index)
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
							
		
							if(focus == "all" && $scope.how_much_to_show[index].button==true)
							{
								alert('plus');
								$scope.how_much_to_show[index].show++;
							}
							$scope.incQuestionAnswers(qid);
					
						
					
					
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
					(focus == "all") ?  $scope.get20questions($scope.from) : $scope.get20NewQuestions($scope.from);
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
	$scope.addVote = function(qid,voteValue){
		$http(
				{
					method : 'POST',
					url : 'http://localhost:8080/webGilad/QuestionsServlet/addVote',
					params : { qid: qid ,voteValue: voteValue},
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
						{
							(focus == "all") ?  $scope.get20questions($scope.from) : $scope.get20NewQuestions($scope.from);
							
						}).error(function(error) {
							alert('somthing happend at add vote');
						});
	}
	$scope.presentAllAnswers = function(answers,index){
		
		var str = "more_answers-" + index;
		var current = document.getElementById(str);
		
		if($scope.how_much_to_show[index].button == true)
		{
			current.innerHTML = "show more answers";
			$scope.how_much_to_show[index].show = 1;
		}
		else 
		{
			current.innerHTML = "hide more answers";
			$scope.how_much_to_show[index].show = answers;	
		}
		$scope.how_much_to_show[index].button = !$scope.how_much_to_show[index].button;


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