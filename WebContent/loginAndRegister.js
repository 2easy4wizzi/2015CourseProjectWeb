
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
		    	 $scope.regNNError = response;
		    	 $scope.regError = response;
		    	 if ($scope.regError == "user name exist")
	    		 {
		    		 $scope.regUNError = response;
		    		 $scope.regUsername = "";
	    		 }
		    	 if ($scope.regError == "nicekname exist")
	    		 {
		    		 $scope.regNNError = response;
		    		 $scope.nickname = "";
	    		 }
		    	 
		    	 if ($scope.regError == "")
	    		 {
		    		 //alert('all went well');
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