angular.module('starter', ['ionic', 'starter.controller','userSetting.service'])

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
  .state('welcome', {
    url: '/welcome',
    templateUrl: 'templates/starter/welcome.html',
    controller: 'welcomeCtrl'
  })
  .state('start', {
    url: '/start',
    templateUrl: 'templates/starter/start.html',
    controller: 'startCtrl'
    });
  $urlRouterProvider.otherwise('/start');
});