lmsApp.config(["$routeProvider", function($routeProvider){
	return $routeProvider.when("/", {
		redirectTo: "/home"
	}).when("/home",{
		templateUrl: "welcome.html"
	}).when("/admin",{
		templateUrl: "admin.html"
	}).when("/librarian",{
		templateUrl: "librarian.html"
	}).when("/viewlibrarybooks",{
		templateUrl: "viewlibrarybooks.html"
	}).when("/author",{
		templateUrl: "author.html"
	}).when("/viewauthors",{
		templateUrl: "viewauthors.html"
	}).when("/viewbooks",{
		templateUrl: "viewbooks.html"
	}).when("/adminbookloan",{
		templateUrl: "adminbookloan.html"
	}).when("/borrowerlogin",{
		templateUrl: "borrowerlogin.html"
	}).when("/borrower",{
		templateUrl: "borrower.html"
	}).when("/viewbooksloans",{
		templateUrl: "viewbooksloans.html"
	}).when("/choosebranch",{
		templateUrl: "choosebranch.html"
	}).when("/borrowlibrarybooks",{
		templateUrl: "borrowlibrarybooks.html"
	})
}])