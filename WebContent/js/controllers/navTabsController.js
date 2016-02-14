app.controller('navTabsC', ['$scope', '$http','$location',                         
    function($scope, $http, $location){
	$scope.isActive = function (viewLocation1,viewLocation2) 
	{			
         var s = location.href;			
         var fields = s.split("/");			
         var name = "/" + fields[4];			
         if(viewLocation1 == name || viewLocation2 == name) { return true;}						
	};
	}]);