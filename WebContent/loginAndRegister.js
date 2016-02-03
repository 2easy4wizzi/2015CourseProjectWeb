
angular.module('loginAndRegister', [])
	.controller('AdminCtrl', ['$scope', '$http',
	function ($scope, $http) 
	{
	     $scope.Login=function () 
	     {
		     $http(
			 {
		         method: 'POST',
		         url: 'http://localhost:8080/webGilad/LoginServlet',
		         params: { username: $scope.U_Name, password: $scope.U_PWD, action: "login" },
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (result) 
		     {
		         if (result == true) 
		         {
		             alert('user is valid');
		         }
		         else 
		         {
		             alert('unauthorised access!');
		         }
		     })
		     .error(function (error) 
		     {
		             $scope.status = 'Unable to connect' + error.message;
		     });     
	     }
	     $scope.Register=function () 
	     {
		     $http(
			 {
		         method: 'POST',
		         url: 'http://localhost:8080/webGilad/LoginServlet',
		         params: { username: $scope.U_Name, password: $scope.U_PWD, 
		                   nickName: $scope.U_Nickname, description: $scope.U_Description,
		                   photo: $scope.U_Photo, action: "register"},
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (result) 
		     {
		         if (result == true) 
		         {
		             alert('user is valid');
		         }
		         else 
		         {
		             alert('unauthorised access!');
		         }
		     })
		     .error(function (error) 
		     {
		             $scope.status = 'Unable to connect' + error.message;
		     });     
	     }
	 
	   // $scope.Login();
	    }]);