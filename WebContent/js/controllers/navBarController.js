app.controller('navC', ['$scope', '$http','$location',                         
    function($scope, $http, $location){

	$scope.isActive = function (viewLocation) 
	{			/*hightlight the currect tab*/
        var s = location.href;
        var fields = s.split("/");			
        var name1 = "/" + fields[4];			
        var name2 = "/" + fields[4].slice(0, -1);
        var homePage = "/homePage.html";
        var existing = "/allQuestions.html";
        var topics = "/topics.html";
        
        if(name1 == homePage){
        	$scope.browse = "Newly Questions";
        	name1 = "/dropdoun"
        }else if(name1 == existing){
        	$scope.browse = "Existing Questions";	
        	name1 = "/dropdoun"
        }else if(name1 == topics){
        	$scope.browse = "Topics";
        	name1 = "/dropdoun"
        }
        
        
        if(viewLocation == name1 || viewLocation == name2) { return true;}						
	};

	$scope.test = 0;
	$scope.nickName = "Guest";
	$scope.getNickname = function()/*getting the user nickname that is logged in to put in the nav bar*/
	{
		  if ($scope.test == 0) {
			$http(
					{
						method : 'POST',
						url : projectUrl + GetSessionUserNameServlet + 'GetNickName',
						headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
					}).success(function(response) {
						
				if (response == "") {/*if no user is logged in - goto login page*/
					window.location = "\loginAndRegister.html";
					//send do home page
				} 
				else 
				{
					$scope.nickname = response;				
				}
			}).error(function(error) {
				console.log('somthing happend at get user name ');
			});
		}
		$scope.test = 1;/*do it just once*/
	}
	$scope.removeAtt = function(){/*click on logout will go to login page*/
		$http(
				{
					method : 'POST',
					url : projectUrl + GetSessionUserNameServlet + 'RemoveAtt',
					headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
				}).success(function(response) {
					
			if (response == "") {
				window.location = "\loginAndRegister.html";
				//send do home page
			} 
			else 
			{
				console.log('goodbye '+ response);
				//$scope.nickName = response;
			}
		}).error(function(error) {
			console.log('remove att somthing happend');
		});
	}
    
	}]);