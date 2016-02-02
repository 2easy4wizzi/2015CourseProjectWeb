
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
		         params: { username: $scope.U_Name, password: $scope.U_PWD },
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
		                   nickName: $scope.U_NickMane, description: $scope.U_Description,
		                   photo: $scope.U_Photo },
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