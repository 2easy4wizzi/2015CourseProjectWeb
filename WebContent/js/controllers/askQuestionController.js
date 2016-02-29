app.controller('askQuestionC', ['$scope', '$http',
      function($scope, $http){
		/*ctor*/
      	$scope.questionText = null;
      	$scope.text_area_place_holder = "type your question...";
      	$scope.topicBar = null;
      	$scope.topicsArray = [];
      	$scope.error_msg = null;
      	
      	$scope.clearQuestion = function()/*clears all the info typed in the question*/
      	{
      		$scope.questionText="";  
      		$scope.topicBar="";  
      		$scope.topicsArray = [];
      		
      	} 
      	/*upon submit of new question*/
      	$scope.postQuestion = function(){
      		if($scope.questionText == null || $scope.questionText == "" )/*not legal to submit with no text*/
  			{
      			$scope.text_area_place_holder ="you must write some text...";  
      			return;
  			}
      		var myJson = JSON.stringify($scope.topicsArray);/*make the topics array a string*/
      		$http(
			{
				method : 'POST',
				url : projectUrl+ QuestionServlet+ 'PostQuestion',
				params : { questionText: $scope.questionText, topics: myJson},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) /*after good submition goto new questions page*/
				{
					window.location = "\homePage.html";
				}).error(function(error) {
					console.log('somthing happend at post question');
				});
      		}
      	$scope.getTopic = function(){/*ng-change - parse the topics input*/
      			$scope.error_msg = "";     		
      			var tempTopicBar = $scope.topicBar.toLowerCase();
      			var len =  tempTopicBar.length;
      			var lastChar = tempTopicBar.substring(len-1,len );
      			
      			if(lastChar == ',')
      			{
      				var topic = tempTopicBar.substring(0,len - 1);
      				$scope.topicBar = "";
      				if(topic == ""){      					
      					return;
      				}
					var topicUniqe = true;
					for (var int = 0; int < $scope.topicsArray.length; int++) {
						if($scope.topicsArray[int] == topic){
							topicUniqe = false;
						}
					}
					if(topicUniqe){
						$scope.topicsArray.push(topic);						
					}
      				
      			}
      			else if(lastChar.match(/[a-z0-9]/i) == null ){ /*dictionary*/
      				$scope.topicBar = $scope.topicBar.substring(0,len - 1);
      				$scope.error_msg = "you can only use [a-z] and [0-9]";
      			}
      	}
      	$scope.deleteTopic=function(index){ /*you can pop a topic by clicking on it*/
      		$scope.topicsArray.splice(index, 1);
      	}
}]);