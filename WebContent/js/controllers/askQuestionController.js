app.controller('askQuestionC', ['$scope', '$http',
      function($scope, $http){
      	$scope.questionText = null;
      	$scope.title = null;
      	$scope.topic = null;
      	$scope.clearQuestion = function()
      	{
      		$scope.questionText="";  
      		$scope.topic="";  
      		$scope.title="";  
      	} 
      	$scope.postQuestion = function(){
      		if($scope.questionText == null || $scope.questionText == "" )
  			{
      			$scope.questionText="you must write some text...";  
      			return;
  			}
      		if( $scope.title == null || $scope.title == "")
      		{
      			$scope.title ="you must write a title...";  
      			return;
      		}
      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/QuestionsServlet/PostQuestion',
				params : { questionText: $scope.questionText , topics: $scope.topic , qtitle : $scope.title },
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					window.location = "\homePage.html";
				}).error(function(error) {
					alert('somthing happend at post question');
				});
      		}
}]);