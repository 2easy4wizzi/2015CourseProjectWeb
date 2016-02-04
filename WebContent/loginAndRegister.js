
angular.module('loginAndRegister', [])
	.controller('loginAndRegController', ['$scope', '$http',
	function ($scope, $http) 
	{
	     $scope.Login=function () 
	     {
		     $http(
			 {
		         method: 'POST',
		         url: 'http://localhost:8080/webGilad/LoginServlet/Login',
		         params: { username: $scope.loginUsername
		        	 	 , password: $scope.loginPass },
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (response) 
		     {
		    	 $scope.regError = response;
		    	 if ($scope.regError == "")
	    		 {
		    		 alert('all went well');
		    		 //send do home page
	    		 }
		    	 else
	    		 {
		    		 
	    		 }
		         /*if (result == true) 
		         {
		             alert('login success');
		         }
		         else 
		         {
		             alert('login failed');
		         }*/
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
		         params: { username: $scope.regUsername ,
		        	 		password: $scope.regPass , 
		                    nickName: $scope.nickname , 
		                    description: $scope.description ,
		                    photo: $scope.photo , },
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (response) 
		     {
		    	 $scope.regError = response;
		    	 if ($scope.regError == "")
	    		 {
		    		 alert('all went well');
		    		 //send do home page
	    		 }
		    	 else
	    		 {
		    		 
	    		 }
		    	 /*if (result == 1) 
		         {
		             alert('register success');
		         }
		         else 
		         {
		             alert('register failed');
		         }*/
		     })
		     .error(function (error) 
		     {
		    	 alert('register error');
	             $scope.status = 'Unable to connect' + error.message;
		     });     
	     }
	 
	   // $scope.Login();
	    }]);