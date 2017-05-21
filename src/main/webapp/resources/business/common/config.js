lmsApp.config(["$routeProvider", function($routeProvider){
	return $routeProvider.when("/", {
		redirectTo: "/home"
	}).when("/home",{
		templateUrl: "welcome.html"
	}).when("/admin",{
		templateUrl: "admin.html"
	}).when("/author",{
		templateUrl: "author.html"
	}).when("/viewauthors",{
		templateUrl: "viewauthors.html"
	}).when("/viewbooks",{
		templateUrl: "viewbooks.html"
	})
}])