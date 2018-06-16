angular.module('app.services', []).factory('Spell', function ($resource) {
    return $resource('/api/v1/spells/:id', {id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Character', function ($resource) {
    return $resource('/api/v1/characters/:id', {id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('CharUpdater', function ($resource) {
    return {
        alterHp: $resource('/api/v1/characters/:id/alterHp', {id: '@id'},
            {
                go: {
                    method: 'POST',
                    transformRequest: function (data) {
                        return data.hp;
                    }
                }
            }),
        setTempHp: $resource('/api/v1/characters/:id/setTempHp', {id: '@id'},
            {
                go: {
                    method: 'POST',
                    transformRequest: function (data) {
                        return data.hp;
                    }
                }
            })
    }
}).service('popupService', function ($window) {
    this.showPopup = function (message) {
        return $window.confirm(message);
    }
});
