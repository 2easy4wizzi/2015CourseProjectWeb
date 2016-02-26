app.controller('questionController', ['$scope', '$http','$location',
                            function($scope, $http,$location){
	var getQuestionByTopic = 'getQuestionsByTopic';

	var arrayInizialize = false;
	

	var answerPositive = "Answer the question";
	var answerNegative = "Collapse Answer";
	$scope.answer_button = answerPositive;
	$scope.more_answers = "show more answers";
	
	
	
	$scope.from = 0;
	$scope.questions = "";
	$scope.answers = "";
	$scope.dontShowNextButton = false;
	
	var refreshIntervalId = -1;
	var answerBoxOpen = 0;

	$scope.showAnswerTextArea = false;
	
	var is_initialized = false;
	
	
	
	$scope.init = function(focus, topic){	
		$scope.focus = focus;
		if(focus == questionByTopicFocus){
			$scope.topic = topic;
			clearInterval(refreshIntervalId);
			$scope.getQuestionsByTopic(topic, 0);
		}
		else if(focus == questionByNewlyFocus){
			refreshIntervalId = setInterval($scope.update, 3000);
			$scope.get20NewQuestions(0);
		}
		else if(focus == questionByAllFocus){
			clearInterval(refreshIntervalId);
			$scope.get20questions(0);
		}
		
		if(focus != questionByNewlyFocus){ 
			$scope.how_much_to_show = [];
			for (var i=0;i<$scope.questions.length;i++) 
			{$scope.how_much_to_show.push({show:1 ,button:false});}
		}
		
	}
	
	
	$scope.getQuestionByFocus= function(focus){
		if(focus == questionByTopicFocus){
			$scope.getQuestionsByTopic($scope.topic, $scope.from);
		}
		else if(focus == questionByNewlyFocus){
			$scope.get20NewQuestions($scope.from);
		}
		else if(focus == questionByAllFocus){
			$scope.get20questions($scope.from);
		}
	}
	
	$scope.getQuestionsByTopic= function(topicPressed, from){
		
		$http(
				{
					method : 'GET',
					url : projectUrl + QuestionServlet +'getQuestionsByTopic',
/*					url : 'http://localhost:8080/webGilad/QuestionsServlet/getQuestionsByTopic',
*/					params : {from: from, topic: topicPressed},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				}).success(function(response){
					var res = response[1];
					$scope.dontShowNextButton = response[0];	
					if(res == "noQuestionsOnTopicsFound"){
						$scope.questions = "";
					}
					else{
						$scope.questions = res;
						if(is_initialized == false){
							$scope.how_much_to_show = [];
							for (var i=0;i<$scope.questions.length;i++) 
							{$scope.how_much_to_show.push({show:1 ,button:false});}
							is_initialized = true;
						}
					}
				}).error(function(error) {
					alert('somthing happend at calcPopularTopics()');
				});
	}
	
	$scope.update = function(){
		if(answerBoxOpen == 0){
			$http(
					{
						method : 'GET',
						url : projectUrl + QuestionServlet +'Update',
						params : {from: $scope.from},
						headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
					}).success(function(response) 
						{
							var res = response;			
							if (res.length != $scope.questions.length || $scope.questions[0].Created != res[0].Created)
							{
								if(res.length == 0){//no questions
									
								}
								else{//there are questions
									
								}
								$scope.get20NewQuestions($scope.from);
								//return;
							}										
						}).error(function(error) {
							alert('somthing happend at update');
							
						});
		}
	}
	
	
	$scope.get20NewQuestions = function(from)
	{
		$http(
		{
			method : 'GET',
			url : projectUrl + QuestionServlet +'GetNewTop20',
			params : { top20from: $scope.from},
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response) 
			{
				$scope.dontShowNextButton = response[0];
				if (response[1] == 'noQuestionsFound') //0 for no questions found
				{	
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
					method : 'GET',
					url : projectUrl + QuestionServlet +'GetTop20',
					params : { top20from: $scope.from},
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
					{
		
					$scope.dontShowNextButton = response[0];
					if (response[1] == 'noQuestionsFound') //0 for no questions found
					{	
						$scope.questions = "";
					} 
					else 
					{
						$scope.questions = response[1];	
						if(is_initialized == false){
							$scope.how_much_to_show = [];
							for (var i=0;i<$scope.questions.length;i++) 
							{$scope.how_much_to_show.push({show:1 ,button:false});}
							is_initialized = true;
						}
					}
					}).error(function(error) {
						alert('somthing happend at get Top20 -not NEW');
						
					});
						
	}
	
	
	
	$scope.answerButtonPressed = function()
	{
		this.showAnswerTextArea = !this.showAnswerTextArea;
		if(this.answer_button == answerNegative)
		{
			this.answer_button = answerPositive;
			answerBoxOpen--;
		}
		else
		{
			this.answer_button = answerNegative;
			answerBoxOpen++;
		}
		
	}
	
	$scope.postAnswer = function(qid,answerText,index)
	{
		
		if(answerText == null || answerText == "")
		{  
			return;
		}
		answerBoxOpen--;
		$http(
				{
					method : 'POST',
					url : projectUrl + AnswerServlet +'PostAnswer',
					params : { answerText: answerText , qid: qid},
					headers : { 'Content-Type' : 'application/x-www-form-urlencoded' }
				}).success(function(response) 
						{
							if($scope.focus != questionByNewlyFocus && $scope.how_much_to_show[index].button==true)
							{			
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
					method : 'PUT',
					url : projectUrl + QuestionServlet +'incQuestionAnswers',
					params : { qid: qid },
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
						{
							$scope.getQuestionByFocus($scope.focus);
							
						}).error(function(error) {
							alert('somthing happend at inc question answers');
						});
	}
	
	$scope.prev = function(){
		$scope.from--;
		$scope.getQuestionByFocus($scope.focus);
	}
	$scope.next = function(){
		$scope.from++;
		$scope.getQuestionByFocus($scope.focus);
	}
	
	
	$scope.addVote = function(qid,voteValue){
		$http(
				{
					method : 'PUT',
					url : projectUrl + QuestionServlet +'addVote',
					params : { qid: qid ,voteValue: voteValue},
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
						{
							var x = response;
							if(x == 0)//succsess
							{
								$scope.getQuestionByFocus($scope.focus);							
							}
							else{
								//1 - cant vote to your own answer
								//2 - allready voted
							}
							
						}).error(function(error) {
							alert('somthing happend at add vote');
						});
	}
	
	$scope.presentAllAnswers = function(answers,index){	
		if($scope.how_much_to_show[index].button == true)
		{
			this.more_answers = "show more answers";
			$scope.how_much_to_show[index].show = 1;
		}
		else 
		{
			this.more_answers = "hide more answers";
			$scope.how_much_to_show[index].show = answers;	
		}
		$scope.how_much_to_show[index].button = !$scope.how_much_to_show[index].button;


	}
	
	
}]);

/*app.controller('topicsService', ['$scope', '$http',
                            function($scope, $http){
	$scope.topics = "";
	$scope.getTopics = function (qid) {
		$http(
				{
					method : 'POST',
					url : projectUrl + TopicsServlet + 'GetTopics',
					params : {qid: qid},
					headers : { 'Content-Type' : 'application/x-www-form-urlencoded' }
				}).success(function(response) 
						{
					$scope.topics = response;
					
						}).error(function(error) {
							alert('somthing happend at get topics');
							
						});	
	}
	
}]);*/
app.controller('answersService', ['$scope', '$http',
                                    function($scope, $http){
  	
       	$scope.getAnswers = function(qid){
       		$http(
       		{
       		method : 'GET',
       		url : projectUrl + AnswerServlet + 'GetAnswers',
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
       			//return response;
       		}).error(function(error) {
       			alert('somthing happend at  get answers');
       					
       				});
           		}

       	$scope.addVote = function(qid,aid,voteValue){
       		$http(
       			{
       				method : 'PUT',
       				url : projectUrl + AnswerServlet + 'addVote',
       				params : { qid: qid ,aid: aid ,voteValue: voteValue},
       				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
       			}).success(function(response) 
       					{
       						var error1 = "already voted to this answer";
       						var error2 = "cant vote to your own answer";
       						var res1 = response.substring(1,error1.length+1);
       						var res2 = response.substring(1,error2.length+1);

       						$scope.getAnswers(qid);
       						if(res1 != error1 && res2 != error2)
       						{
       							$scope.question.QRating = response;
       						}
       					}).error(function(error) {
       						alert('somthing happend at add vote(answers)');
       					});
       }
       	
       	
       }]);
