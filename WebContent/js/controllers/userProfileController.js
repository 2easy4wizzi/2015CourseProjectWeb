app.controller('userProfileC', ['$scope', '$http','$location',  
      function($scope, $http,$location){
      
	var x = location.hash;
	alert(x);

      		$http(
			{
				method : 'POST',
				url : 'http://localhost:8080/webGilad/UserProfileServlet/getUserDetails',
				params : { user: x},
				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) 
				{
					window.location = "\homePage.html";
				}).error(function(error) {
					alert('somthing happend at post question');
				});
      		
}]);