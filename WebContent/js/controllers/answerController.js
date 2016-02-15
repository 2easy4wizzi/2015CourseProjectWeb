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
			alert('somthing happend at  get answers');
					
				});
    		}

$scope.addVote = function(qid,aid,voteValue){
	$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/AnswersServlet/addVote',
				params : { qid: qid ,aid: aid ,voteValue: voteValue},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
			}).success(function(response) 
					{
						alert(response);
						$scope.getAnswers(qid);
					}).error(function(error) {
						alert('somthing happend at add vote(answers)');
					});
}
	
}]);