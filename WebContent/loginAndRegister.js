
angular.module('loginAndRegister', [])
	.controller('AdminCtrl', ['$scope', '$http',
	function ($scope, $http) 
	{
	     $scope.Login=function () 
	     {
		     $http(
			 {
		         method: 'POST',
		         url: 'http://localhost:8080/webGilad/LoginServlet/Login',
		         params: { username: $scope.U_NameLogin
		        	 	 , password: $scope.U_PWDLogin
		        	 	 , action: "login" },
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (result) 
		     {
		         if (result == true) 
		         {
		             alert('login success');
		         }
		         else 
		         {
		             alert('login failed');
		         }
		     })
		     .error(function (error) 
		     {
		    	 alert('login error');
		             $scope.status = 'Unable to connect' + error.message;
		     });     
	     }
	     $scope.Register=function () 
	     {
		     $http(
			 {
		         method: 'POST',
		         url: 'http://localhost:8080/webGilad/LoginServlet/Register',
		         params: {  username: $scope.U_Name,
		        	 		password: $scope.U_PWD, 
		                    nickName: $scope.U_Nickname, 
		                    description: $scope.U_Description,
		                    photo: $scope.U_Photo, 
		                    action: "register"},
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (result) 
		     {
		         if (result == 1) 
		         {
		             alert('register success');
		         }
		         else 
		         {
		             alert('register failed');
		         }
		     })
		     .error(function (error) 
		     {
		    	 alert('register error');
	             $scope.status = 'Unable to connect' + error.message;
		     });     
	     }
	 
	   // $scope.Login();
	    }]);