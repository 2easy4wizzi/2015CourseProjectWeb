app.controller('userProfileC', ['$scope', '$http','$location',  
      function($scope, $http,$location){
	
	$scope.getUserDetails = function () 
	{			
         var s = location.href;			
         var fields = s.split("/");			
         var name = "/" + fields[4];			

      
	var x = location.hash;
	alert(x);

      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/UserProfileServlet/getUserDetails',
				params : { userToShow : x},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					alert(response);
				}).error(function(error) {
					alert('somthing happend at post question');
				});
      		
	}
      		
}]);