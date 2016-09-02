angular.module('calendar', ['ionic', 'calendar.controller', 'calendar.service'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})

.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state('calendar', {
    url: '/myCalendar',
    templateUrl: 'templates/calendar/myCalendar.html',
    controller: 'myCalendarCtrl'
  });
  $urlRouterProvider.otherwise('/myCalendar');
});