app.controller('askQuestionC', ['$scope', '$http',
      function($scope, $http){
      	$scope.questionText = null;
      	$scope.text_area_place_holder = "type your question...";
      	$scope.topicBar = null;
      	//$scope.debuger3 = "d";
      	$scope.topicsArray = [];
      	$scope.error_msg = null;
      	
      	$scope.clearQuestion = function()
      	{
      		$scope.questionText="";  
      		$scope.topicBar="";  
      		
      	} 
      	$scope.postQuestion = function(){
      		if($scope.questionText == null || $scope.questionText == "" )
  			{
      			$scope.text_area_place_holder ="you must write some text...";  
      			return;
  			}
      		var myJson = JSON.stringify($scope.topicsArray);
      		alert(myJson);
      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/QuestionsServlet/PostQuestion',
				params : { questionText: $scope.questionText , topics: $scope.topic  },
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					window.location = "\homePage.html";
				}).error(function(error) {
					alert('somthing happend at post question');
				});
      		}
      	$scope.getTopic = function(){
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
      			else if(lastChar.match(/[a-z0-9]/i) == null ){
      				$scope.topicBar = $scope.topicBar.substring(0,len - 1);
      				$scope.error_msg = "you can only use [a-z] and [0-9]";
      			}
      	}
      	$scope.deleteTopic=function(index){
      		$scope.topicsArray.splice(index, 1);
      	}
}]);