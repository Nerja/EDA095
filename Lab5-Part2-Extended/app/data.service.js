(function(app) {

  app.DataService = DataService;
  function DataService() {
    this.myList = [{text: "make a list", done: true},
                   {text: "print the list", done: true},
                   {text: "create a service", done: true},
                   {text: "add more functionality...", done: false}];
  }

  DataService.prototype.getTODOs = function() {
    return this.myList;
  };

  DataService.prototype.addTodo = function(todo) {
    //  Test: this.myList.push(todo);
    //  Fix: this.myList.push({text: todo.text, done: todo.done});
    this.myList.push({text: todo.text, done: todo.done});
  };

  DataService.prototype.editTodo = function(todo) {
    this.myList[todo.id].text = todo.text;
    this.myList[todo.id].done = todo.done;
  }

  DataService.prototype.deleteTodo = function(todo) {
    this.myList.splice(todo.id, 1);
  }

})(window.app = window.app || {});
