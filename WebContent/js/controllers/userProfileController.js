app.controller('userProfileC', ['$scope', '$http','$location',  
      function($scope, $http,$location){
	
	$scope.getUserDetails = function () 
	{			
         var s = location.href;			
         var fields = s.split("/");	
         var name = fields[4];
         if(name == "userProfile.html")
         {
        	 name = "logNow";
         }
         else{
        	 var name = fields[5].slice(1);       	 
         }


      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/UserProfileServlet/getUserDetails',
				params : { userToShow : name},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					$scope.user = response[0];	
					
				}).error(function(error) {
					alert('somthing happend at getUserDetails');
				});
      		
	}
      		
}]);