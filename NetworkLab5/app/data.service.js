(function(app) {

  app.DataService = DataService;
  function DataService() {
    this.myList = [{id: 1, text: "make a list", done: true},
                   {id: 2, text: "print the list", done: true},
                   {id: 3, text: "create a service", done: true},
                   {id: 4, text: "add more functionality...", done: false}];
  }

  DataService.prototype.getTODOs = function() {
    return this.myList;
  };

  DataService.prototype.addTodo = function(todo) {
    this.myList.push({id: this.myList.length + 1, text: todo.text, done: todo.done});
  };

})(window.app = window.app || {});
