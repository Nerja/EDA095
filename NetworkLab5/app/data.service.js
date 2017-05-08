(function(app) {

  app.DataService = DataService;
  function DataService() {
    this.myList = [{id: 0, text: "make a list", done: true},
                   {id: 1, text: "print the list", done: true},
                   {id: 2, text: "create a service", done: true},
                   {id: 3, text: "add more functionality...", done: false}];
  }

  DataService.prototype.getTODOs = function() {
    return this.myList;
  };

  DataService.prototype.addTodo = function(todo) {
    this.myList.push({id: this.myList.length + 1, text: todo.text, done: todo.done});
  };

  DataService.prototype.updateTodo = function(todo) {
    this.myList[todo.id].text = todo.text;
    this.myList[todo.id].done = todo.done;
  };
})(window.app = window.app || {});
