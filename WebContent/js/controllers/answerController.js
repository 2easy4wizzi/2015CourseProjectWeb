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
						alert('somthing happend at add vote(answers)');
					});
}
	
}]);