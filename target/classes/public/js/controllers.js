angular.module('app.controllers', []).controller('SpellListController', function($scope, $state, popupService, $window, Spell) {
  $scope.spells = Spell.query(); //fetch all spells. Issues a GET to /api/vi/spells

  $scope.deleteSpell = function(spell) { // Delete a Spell. Issues a DELETE to /api/v1/spells/:id
    if (popupService.showPopup('Really delete this?')) {
      spell.$delete(function() {
        $scope.spells = Spell.query();
        $state.go('spells');
      });
    }
  };
}).controller('SpellViewController', function($scope, $stateParams, Spell) {
  $scope.spell = Spell.get({ id: $stateParams.id }); //Get a single spell.Issues a GET to /api/v1/spells/:id
}).controller('SpellCreateController', function($scope, $state, $stateParams, Spell) {
  $scope.spell = new Spell();  //create new spell instance. Properties will be set via ng-model on UI

  $scope.classes = getClasses();

  $scope.addSpell = function() { //create a new spell. Issues a POST to /api/v1/spells
    $scope.spell.$save(function() {
      $state.go('spells'); // on success go back to the list i.e. spells state.
    });
  };
}).controller('SpellEditController', function($scope, $state, $stateParams, Spell) {
  $scope.updateSpell = function() { //Update the edited spell. Issues a PUT to /api/v1/spells/:id
    $scope.spell.$update(function() {
      $state.go('spells'); // on success go back to the list i.e. spells state.
    });
  };

  $scope.loadSpell = function() { //Issues a GET request to /api/v1/spells/:id to get a spell to update
    $scope.spell = Spell.get({ id: $stateParams.id });
  };

  $scope.loadSpell(); // Load a spell which can be edited on UI
  $scope.classes = getClasses();
});

function getClasses() {
    return ["Bard","Cleric","Druid","Paladin","Ranger","Sorcerer","Warlock","Wizard"]
}

