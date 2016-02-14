app.controller('navC', ['$scope', '$http','$location',                         
    function($scope, $http, $location){
	
	$scope.isActive = function (viewLocation) 
	{			
         var s = location.href;			
         var fields = s.split("/");			
         var name = "/" + fields[4];			
         if(viewLocation == name) { return true;}						
	};
	$scope.test = 0;
	$scope.nickName = "Guest";
	$scope.getNickname = function()
	{
		  if ($scope.test == 0) {
			$http(
					{
						method : 'POST',
						url : 'http://localhost:8080/webGilad/GetSessionUserNameServlet/GetNickName',
						headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
					}).success(function(response) {
						
				if (response == "") {
					//window.location = "\loginAndRegister.html";
					//send do home page
				} 
				else 
				{
					$scope.name = response;
				}
			}).error(function(error) {
				alert('somthing happend at get user name ');
			});
		}
		$scope.test = 1;
	}
	$scope.removeAtt = function(){
		$http(
				{
					method : 'POST',
					url : 'http://localhost:8080/webGilad/GetSessionUserNameServlet/RemoveAtt',
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) {
					
			if (response == "") {
				window.location = "\loginAndRegister.html";
				//send do home page
			} 
			else 
			{
				alert('goodbye '+ response);
				$scope.name = response;
			}
		}).error(function(error) {
			alert('remove att somthing happend');
		});
	}
    
	}]);