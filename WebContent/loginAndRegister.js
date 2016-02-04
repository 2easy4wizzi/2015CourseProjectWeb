
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
		    	
		    	 $scope.logErr = response;
		    	 if ($scope.logErr == "")
	    		 {
		    		 window.location = "\homePage.html";
		    		 //send do home page
	    		 }
		    	 else
	    		 {
		    		 $scope.loginUsername = "";
		    		 $scope.loginPass = "";
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
		         params: { username: $scope.regUsername ,
		        	 		password: $scope.regPass , 
		                    nickName: $scope.nickname , 
		                    description: $scope.description ,
		                    photo: $scope.photo , },
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (response) 
		     {

		    	if (response == 0)
	    		 {
		    		 $scope.regUNError = "User name exist";
		    		 $scope.regUsername = "";
	    		 }
		    	 if (response == 2)
	    		 {
		    		 $scope.regNNError = "Nick name exist";
		    		 $scope.nickname = "";
	    		 }
		    	 if (response == 3)
	    		 {
		    		 $scope.regUNError = "User name exist"
		    		 $scope.regNNError = "Nick name exist";
		    		 $scope.regUsername = "";
		    		 $scope.nickname = "";
	    		 }
		    	 
		    	 if (response == "")
	    		 {
		    		// alert('all went well');
		    		 window.location = "\homePage.html";
		    		 //send do home page
	    		 }
		    	 else
	    		 {
		    		 
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