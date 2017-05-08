(function(app) {

app.AppModule = AppModule;
function AppModule() { }

AppModule.annotations = [
  new ng.core.NgModule({
    imports: [
      ng.platformBrowser.BrowserModule,
      ng.forms.FormsModule
    ],

    declarations: [
      app.AppComponent,
      app.TodoComponent,
      app.TodoListComponent
    ],
    providers: [
      app.DataService,
    ],
    bootstrap: [ app.AppComponent ]
  })
]

})(window.app = window.app || {});
