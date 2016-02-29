app.controller('userProfileC', ['$scope', '$http','$location',  
      function($scope, $http,$location){
	/*ctor*/
	 var s = location.href;			
     var fields = s.split("/");	
     var name = fields[4];
     if(name == "userProfile.html")/*understand which user was clicked by uri*/
     {
    	 name = "logNow";
     }
     else{
    	 var name = fields[5].slice(1);       	 
     }
	
	$scope.getUserDetails = function () /*get user clicked details*/
	{			
		$http(
			{
				method : 'GET',				
				url : projectUrl +UserProfileServlet + 'getUserDetails',
				params : { userToShow : name},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					$scope.user = response[0];	
					
				}).error(function(error) {
					console.log('somthing happend at getUserDetails');
				});
      		
	}
	$scope.last5Questions = function () /*get the user's last 5 questions*/
	{			
		$http(
			{
				method : 'GET',
				url : projectUrl +UserProfileServlet + 'last5Questions',
				params : { userToShow : name},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					$scope.questions = response;	
					
				}).error(function(error) {
					console.log('somthing happend at last5Questions');
				});
      		
	}
	$scope.getExpertise = function () /*get user's expertise*/
	{			
		$http(
			{
				method : 'GET',
				url : projectUrl +UserProfileServlet + 'getExpertise',
				params : { userToShow : name},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					$scope.expertises = response;	
					
				}).error(function(error) {
					console.log('somthing happend at getUserDetails');
				});
      		
	}
	$scope.getQuestionForAnswer = function () /*get 5 last answers with their questions*/
	{			
		$http(
			{
				method : 'GET',
				url : projectUrl +UserProfileServlet + 'getQuestionForAnswer',
				params : { userToShow : name},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{	
	
				$scope.answersQuestions = response;
				
				}).error(function(error) {
					console.log('somthing happend at getQuestionForAnswer');
				});
      		
	}
}]);