app.controller('answerController', ['$scope', '$http','$location',
                             function($scope, $http,$location){
$scope.getAnswers = function(qid){
		
		$http(
		{
		method : 'POST',
		url : 'http://localhost:8080/webGilad/AnswersServlet/GetAnswers',
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
			alert('somthing happend at post question');
					
				});
    		}
	
}]);