var app = angular.module("myApp", []);

app.controller('askQController', ['$scope', '$http',
              function($scope, $http){
	          	$scope.question = null;
	          	$scope.Clear = function()
	          	{
	          		$scope.question="";  
	          	} 
}]);

/*app.directive("navDirective", function() {
    return {
        templateUrl : "navBar.html"
    };
});*/