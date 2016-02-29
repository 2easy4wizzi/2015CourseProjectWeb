app.controller('logAndRegC', ['$scope', '$http',
	function ($scope, $http) 
	{
		 $scope.logErr="";
		 $scope.logUNError="";
		 $scope.logPWError="";
		 $scope.loginUsername = null;
		 $scope.loginPass = null;
		 
		 $scope.regUNError = "";
		 $scope.regNNError = "";
		 $scope.regPWError="";		 
		 $scope.regUsername = null;
		 $scope.regPass = null;
		 $scope.nickname = null;
		 $scope.description = null;
		 $scope.photoUrl = null;
		 
		 $scope.showLoginView = true;
		 $scope.showDangerRegNN = true;
		 $scope.showDangerUserName = true;
		 $scope.showDangerPassword = true;
		 $scope.showDangerRegUN = true;
		 $scope.showDangerRegPW = true;
 
	 
 
 
 
	     $scope.Login=function () 
	     {
	    	 $scope.logErr="";
	    	 $scope.logUNError = "";
	    	 $scope.logPWError = "";
			 $scope.showDangerUserName = true;
			 $scope.showDangerPassword = true;
	    	 if($scope.loginUsername == null || $scope.loginPass == null || $scope.loginUsername == "" || $scope.loginPass == "")
			 {
				 if($scope.loginUsername == null || $scope.loginUsername == "")
				 {
					 $scope.showDangerUserName = false;
					 $scope.logUNError = "must enter user name!";
				 }
				 if($scope.loginPass == null || $scope.loginPass == "")
				 {
					 $scope.showDangerPassword = false;
					 $scope.logPWError = "must enter password!";
				 }
				 return;
			 }	
		     $http(
			 {
		         method: 'POST',
		         url: projectUrl+LoginServlet+'Login',
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
		    	 console.log('login error');
	             $scope.status = 'Unable to connect' + error.message;
		     });     
	     }
	     $scope.Register=function () 
	     {
	    	 $scope.regUNError = "";
			 $scope.regNNError = "";
			 $scope.regPWError="";
			 $scope.showDangerRegUN = true;
			 $scope.showDangerRegPW = true;
			 $scope.showDangerRegNN = true;
			 
			 if($scope.regUsername == null || $scope.regUsername =="" || $scope.regPass == null || $scope.regPass =="" || $scope.nickname == null || $scope.nickname =="")
			 {
				 if($scope.regUsername == null || $scope.regUsername == "")
				 {
					$scope.showDangerRegUN = false;
					$scope.regUNError = "must enter user name!";

				 }
				 if($scope.regPass == null || $scope.regPass == "")
				 {
					 $scope.showDangerRegPW = false;
					 $scope.regPWError = "must enter password!";
				 }
				 if($scope.nickname == null || $scope.nickname == "")
				 {
					 $scope.showDangerRegNN = false;
					 $scope.regNNError = "must enter nickname!";
				 }
				 return;
			 }	 
			
		     $http(
			 {
		         method: 'POST',
		         url: projectUrl+LoginServlet+'Register',
		         params: { username: $scope.regUsername ,
		        	 		password: $scope.regPass , 
		        	 		nickname: $scope.nickname , 
		                    description: $scope.description ,
		                    photo: $scope.photoUrl },
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		     })
		     .success(function (response) 
		     {
		    	
		    	if (response == 2)
	    		 {
		    		 $scope.showDangerRegUN = false;
		    		 $scope.regUNError = "User name exist";
		    		 $scope.regUsername = "";
	    		 }
		    	 if (response == 3)
	    		 {
		    		 $scope.showDangerRegNN = false;
		    		 $scope.regNNError = "Nick name exist";
		    		 $scope.nickname = "";
	    		 }
		    	 if (response == 4)
	    		 {
		    		 $scope.showDangerRegUN = false;
		    		 $scope.showDangerRegNN = false;
		    		 $scope.regUNError = "User name exist"
		    		 $scope.regNNError = "Nick name exist";
		    		 $scope.regUsername = "";
		    		 $scope.nickname = "";
	    		 }
		    	 
		    	 if (response == "")
	    		 {
		    		 window.location = "\homePage.html";
		    		 //send do home page
	    		 }
		    	 else
	    		 {
		    		 
	    		 }
		 
		     })
		     .error(function (error) 
		     {
		    	 console.log('register error');
	             $scope.status = 'Unable to connect' + error.message;
		     });     
	     }
	     $scope.regClear=function () {
	    	 $scope.regUNError = "";
			 $scope.regNNError = "";
			 $scope.regPWError="";		 
			 $scope.regUsername = "";
			 $scope.regPass = "";
			 $scope.nickname = "";
			 $scope.description = "";
			 $scope.photoUrl = ""; 
			 $scope.showDangerRegUN = true;
			 $scope.showDangerRegNN = true;
	     }
	     $scope.logClear=function () {
	    	 $scope.logErr="";
			 $scope.logUNError="";
			 $scope.logPWError="";
			 $scope.loginUsername = "";
			 $scope.loginPass = "";
			 $scope.showDangerUserName = true;
			 $scope.showDangerPassword = true;
			 
	     }
	     
	 
	    }]);