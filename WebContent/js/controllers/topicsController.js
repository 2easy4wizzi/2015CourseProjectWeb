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
			method : 'POST',
			url : 'http://localhost:8080/webGilad/TopicsServlet/calcPopularTopics',
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



}]);


