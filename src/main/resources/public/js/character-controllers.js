angular.module('app.charControllers', []).controller('CharacterListController', function($scope, $state, popupService, $window, Character) {
  $scope.characters = Character.query(); //fetch all characters. Issues a GET to /api/vi/characters

  $scope.deleteCharacter = function(character) { // Delete a Character. Issues a DELETE to /api/v1/characters/:id
    if (popupService.showPopup('Really delete this?')) {
      character.$delete(function() {
        $scope.characters = Character.query();
        $state.go('characters');
      });
    }
  };
}).controller('CharacterViewController', function($scope, $stateParams, Character) {
    $scope.getModifier = function (stat) {
        return getStatModifier(stat);
    };

    $scope.getSaveModifier = function (statName, character) {

    //*ngIf won't work so this is to stop console errors on page load
    if (typeof character.saves == 'undefined') {
        return
    }
        let save = character.saves[statName];
        let stat = character.stats[statName];
        if (save.proficient) {
            return getStatModifier(stat.value)+character.proficiencyBonus;
        } else {
            return getStatModifier(stat.value);
        }
    };
    $scope.character = Character.get({ id: $stateParams.id }); //Get a single character. Issues a GET to /api/v1/characters/:id
}).controller('CharacterCreateController', function($scope, $state, $stateParams, Character) {
    $scope.character = new Character();  //create new character instance. Properties will be set via ng-model on UI

    $scope.classes = getClasses();
    $scope.character.classes = new Object();

  $scope.addNewCharClass = function() {
    $scope.character.classes[''] = {'classLevel':1};
  };

  $scope.filterChanged = function(newValue, oldValue){
    if (oldValue === '') {
         $scope.character.classes[newValue] = {'classLevel':1};
         if ($scope.character.classes.hasOwnProperty('')) {
             delete $scope.character.classes[''];
         }
    }
   };

  $scope.removeCharClass = function(key) {
    delete $scope.character.classes[key];
  };

  $scope.addCharacter = function() { //create a new character. Issues a POST to /api/v1/characters
    $scope.character.$save(function() {
      $state.go('characters'); // on success go back to the list i.e. characters state.
    });
  };
}).controller('CharacterEditController', function($scope, $state, $stateParams, Character) {
  $scope.updateCharacter = function() { //Update the edited character. Issues a PUT to /api/v1/characters/:id
    $scope.character.$update(function() {
      $state.go('characters'); // on success go back to the list i.e. characters state.
    });
  };

  $scope.loadCharacter = function() { //Issues a GET request to /api/v1/characters/:id to get a character to update
    $scope.character = Character.get({ id: $stateParams.id });
  };

  $scope.addNewCharClass = function() {
    $scope.character.classes[''] = {'classLevel':1};
  };

  $scope.filterChanged = function(newValue, oldValue){
    if (oldValue === '') {
         $scope.character.classes[newValue] = {'classLevel':1};
         if ($scope.character.classes.hasOwnProperty('')) {
             delete $scope.character.classes[''];
         }
    }
   };

  $scope.removeCharClass = function(key) {
    delete $scope.character.classes[key];
  };

  $scope.classes = getClasses();
  $scope.loadCharacter(); // Load a character which can be edited on UI
});

function getStatModifier(stat) {
        let mod = (stat/2-5).toFixed(0);
        if (mod > 0) {
            return "+"+mod;
        } else {
            return mod;
        }
}

function getClasses() {
    return ["Bard","Cleric","Druid","Paladin","Ranger","Sorcerer","Warlock","Wizard"];
}