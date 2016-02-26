app.controller('topicsC', ['$scope', '$http','$location',function($scope, $http,$location){
	$scope.from = 0;
	$scope.dontShowNextButton = false;
	$scope.topics = "";
	$scope.questions = "";
	$scope.focus = "topics";
	
	
	$scope.browseQuestions = function(topicPressed){
		$scope.focus = questionByTopicFocus;
		$scope.tempTopic = topicPressed;

	}
	
	
	$scope.calcPopularTopics = function(from)
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
			alert('somthing happend at calcPopularTopics()');
		});
	}
	
	
	$scope.prevTopics = function(){
		$scope.from--;
		$scope.calcPopularTopics($scope.from);
		
	}
	
	$scope.nextTopics = function(){
		$scope.from++;
		$scope.calcPopularTopics($scope.from);
	}
	
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

}]);


