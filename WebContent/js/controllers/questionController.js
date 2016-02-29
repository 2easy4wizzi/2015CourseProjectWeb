app.controller('questionController', ['$scope', '$http','$location',
                            function($scope, $http,$location){
	var getQuestionByTopic = 'getQuestionsByTopic';

	var arrayInizialize = false;/*used to initialize once*/
	
	/*button text change on click*/
	var answerPositive = "Answer the question";
	var answerNegative = "Collapse Answer";
	$scope.answer_button = answerPositive;
	$scope.more_answers = "show more answers";
	
	
	/*from is offset*/
	$scope.from = 0;
	$scope.questions = "";
	$scope.answers = "";
	$scope.dontShowNextButton = false;
	
	var refreshIntervalId = -1;/*used for update function - working every 3 seconds on newly questions view*/
	var answerBoxOpen = 0;/*will cancel the update if 1 or more answers boxes are open*/

	$scope.showAnswerTextArea = false;/*the leave answer area is shown upon click*/
	
	var is_initialized = false;
	
	
	/*3 pages start with this function. each sends a different focus*/
	$scope.init = function(focus, topic){	
		$scope.focus = focus;
		if(focus == questionByTopicFocus){/*topic page also sends a topic's name*/
			$scope.topic = topic;
			clearInterval(refreshIntervalId);
			$scope.getQuestionsByTopic(topic, 0);
		}
		else if(focus == questionByNewlyFocus){
			refreshIntervalId = setInterval($scope.update, 3000);/*update function starts*/
			$scope.get20NewQuestions(0);
		}
		else if(focus == questionByAllFocus){
			clearInterval(refreshIntervalId);
			$scope.get20questions(0);
		}
		
		if(focus != questionByNewlyFocus){ /*initialize an array for "show more" button*/
			$scope.how_much_to_show = [];
			for (var i=0;i<$scope.questions.length;i++) 
			{$scope.how_much_to_show.push({show:1 ,button:false});}
		}
		
	}
	
	
	$scope.getQuestionByFocus= function(focus){/*after changes we call this function with the right focus*/
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
	
	$scope.getQuestionsByTopic= function(topicPressed, from){/*get top 20 by topic*/
		
		$http(
				{
					method : 'GET',
					url : projectUrl + QuestionServlet +'getQuestionsByTopic',
					params : {from: from, topic: topicPressed},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				}).success(function(response){
					var res = response[1];
					$scope.dontShowNextButton = response[0];	/*enable or disable next button*/
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
					console.log('somthing happend at calcPopularTopics()');
				});
	}
	
	$scope.update = function(){/*update function, runs every 3 seconds in Newly focus*/
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
							if (res.length != $scope.questions.length || ($scope.questions.length>0 && $scope.questions[0].Created != res[0].Created))
							{
								if(res.length == 0){//no questions
									
								}
								else{//there are questions
									
								}
								$scope.get20NewQuestions($scope.from); /*if there was a change reload questions*/
								//return;
							}										
						}).error(function(error) {
							console.log('somthing happend at update');
							
						});
		}
	}
	
	
	$scope.get20NewQuestions = function(from)/*get top new 20 Questions with an offset*/
	{
		$http(
		{
			method : 'GET',
			url : projectUrl + QuestionServlet +'GetNewTop20',
			params : { top20from: $scope.from},
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response) 
			{
				$scope.dontShowNextButton = response[0];/*enable of disable next button*/
				if (response[1] == 'noQuestionsFound') //0 for no questions found
				{	
					$scope.questions = "";
				} 
				else 
				{
					$scope.questions = response[1];	
				}
				}).error(function(error) {
					console.log('somthing happend at getNewTop20');
					
				});
		}

	$scope.get20questions = function(from)/*get top 20 regular questions by rating*/
	{	
		$http(
				{
					method : 'GET',
					url : projectUrl + QuestionServlet +'GetTop20',
					params : { top20from: $scope.from},
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
					{
		
					$scope.dontShowNextButton = response[0];/*enable or disable next button*/
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
						console.log('somthing happend at get Top20 -not NEW');
						
					});
						
	}
	
	
	
	$scope.answerButtonPressed = function()/*leave and answer button pressed-> change the text and open the answer area*/
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
	
	$scope.postAnswer = function(qid,answerText,index)/*post answer clicked*/
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
							console.log('somthing happend at postAnswer');
							
						});
	}
	
	$scope.incQuestionAnswers = function(qid)/*increment the number of answers to a question*/
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
							console.log('somthing happend at inc question answers');
						});
	}
	
	$scope.prev = function(){/*next and prev buttons move the offset*/
		$scope.from--;
		$scope.getQuestionByFocus($scope.focus);
	}
	$scope.next = function(){
		$scope.from++;
		$scope.getQuestionByFocus($scope.focus);
	}
	
	
	$scope.addVote = function(qid,voteValue){/*like or dislike a question*/
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
							console.log('somthing happend at add vote');
						});
	}
	
	$scope.presentAllAnswers = function(answers,index){	/*show more answers button*/
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

/*answer controller*/
app.controller('answersService', ['$scope', '$http',
                                    function($scope, $http){
  	
       	$scope.getAnswers = function(qid){/*get all answers to a question*/
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
       			console.log('somthing happend at  get answers');
       					
       				});
           		}

       	$scope.addVote = function(qid,aid,voteValue){/*like or dislike an answer*/
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
       						console.log('somthing happend at add vote(answers)');
       					});
       }
       	
       	
       }]);
