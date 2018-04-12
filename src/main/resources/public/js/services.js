angular.module('app.services', []).factory('Spell', function($resource) {
  return $resource('/api/v1/spells/:id', { id: '@id' }, {
    update: {
      method: 'PUT'
    }
  });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
});
