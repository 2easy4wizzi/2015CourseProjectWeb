app.controller('topicsC', ['$scope', '$http','$location',function($scope, $http,$location){
	/*topics ctor*/
	
	$scope.from = 0;
	$scope.dontShowNextButton = false;
	$scope.topics = "";
	$scope.questions = "";
	$scope.focus = "topics";
	
	$scope.browseQuestions = function(topicPressed){/*on topic clicked*/
		$scope.focus = questionByTopicFocus;
		$scope.tempTopic = topicPressed;

	}
	
	
	$scope.calcPopularTopics = function(from)/*get top 20 topics by popularity*/
	{
		$http(
		{
			method : 'GET',
			url : projectUrl + TopicsServlet + 'calcPopularTopics',
			params : {from: from},
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(response){
			var res = response[1];
			$scope.dontShowNextButton = response[0];
			
			if(res == "noTopicsFound"){
				$scope.topics = "";
				
			}
			else{
				$scope.topics = res;
				
			}
		}).error(function(error) {
			console.log('somthing happend at calcPopularTopics()');
		});
	}
	
	
	$scope.prevTopics = function(){/*next and prev buttons*/
		$scope.from--;
		$scope.calcPopularTopics($scope.from);
		
	}
	
	$scope.nextTopics = function(){
		$scope.from++;
		$scope.calcPopularTopics($scope.from);
	}
	
	$scope.topics = "";
	$scope.getTopics = function (qid) {/*get all topics for a question*/
		$http(
				{
					method : 'GET',
					url : projectUrl + TopicsServlet + 'GetTopics',
					params : {qid: qid},
					headers : { 'Content-Type' : 'application/x-www-form-urlencoded' }
				}).success(function(response) 
						{
					$scope.topics = response;
					if($scope.topics.length == 0){
					}
					else{
					}
						}).error(function(error) {
							console.log('somthing happend at get topics');
							
						});	
	}

}]);


