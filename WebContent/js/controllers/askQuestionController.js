app.controller('askQuestionC', ['$scope', '$http',
      function($scope, $http){
      	$scope.questionText = null;
      	
      	$scope.topic = null;
      	$scope.clearQuestion = function()
      	{
      		$scope.questionText="";  
      		$scope.topic="";  
      		
      	} 
      	$scope.postQuestion = function(){
      		if($scope.questionText == null || $scope.questionText == "" )
  			{
      			$scope.questionText="you must write some text...";  
      			return;
  			}
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
}]);