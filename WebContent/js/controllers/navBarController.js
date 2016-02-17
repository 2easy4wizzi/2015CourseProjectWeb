app.controller('navC', ['$scope', '$http','$location',                         
    function($scope, $http, $location){
	var name =  null;
	var name2 = null;
	$scope.isActive = function (viewLocation1) 
	{			
        var s = location.href;

         var fields = s.split("/");			
         name = "/" + fields[4];
         name2 = "/" + fields[5];

         if(viewLocation1 == name) { return true;}						
	};
	
	//alert("name = "+ name "name2 = " + name2 );
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
					$scope.nickName = response;
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
				//$scope.nickName = response;
			}
		}).error(function(error) {
			alert('remove att somthing happend');
		});
	}
    
	}]);