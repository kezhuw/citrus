(function() {
    define(["TemplateManager", "views/TestDetailsView"], function(TemplateManager, TestDetailsView) {
        var TestListView = Backbone.View.extend({
    
          tests: {},
          searchResults: {},
          
          events: {
              "click tr.show-test-details" : "showDetails",
              "submit #search-form" : "searchTests"
          },
          
          initialize: function() {
          },
          
          render: function() {
              $(this.el).html(TemplateManager.template('TestListView', {}));
              
              $.ajax({
                  url: "testcase",
                  type: 'GET',
                  dataType: "json",
                  success: _.bind(function(response) {
                      this.tests = response;
                      this.searchResults = response;
                      this.afterRender();
                  }, this)
              });
              
              return this;
          },
          
          afterRender: function() {
              $('#search-results').html(TemplateManager.template('TestTableView', { tests: this.tests }));
              
              var searchNames = _.map(this.tests, function(test){ return test.name; });
              var searchPackages = _.map(this.tests, function(test){ return test.packageName + ".*"; });
              var searchKeys = _.union(searchPackages, searchNames);
              $('#test-name').typeahead({
                  source: searchKeys,
                  items: 5,
                  minLength: 1
              });
          },
          
          searchTests: function() {
              var searchKey = $('#test-name').val();
              
              if (searchKey) {
                  if (searchKey.indexOf(".*")) {
                      this.searchResults = _.where(this.tests, {packageName: searchKey.substring(0, searchKey.length - 2)});
                  } else {
                      this.searchResults = _.where(this.tests, {name: searchKey});
                  }
                  
              } else {
                  this.searchResults = this.tests;
              }
              
              $('#search-results').html(TemplateManager.template('TestTableView', { tests: this.searchResults }));
          },
          
          showDetails: function(event) {
              var test = this.searchResults[event.currentTarget.rowIndex - 1];
              var idHash= test.name.toLowerCase();
              
              if ($('ul#testlist-tabs li#tab-' + idHash).size() === 0) {
                  
                  $('ul#testlist-tabs').append(Handlebars.compile($('#test-details-tab').html())({hash: idHash, name: test.name}));
                  $('div#testlist-tab-content').append(Handlebars.compile($('#test-details-tab-pane').html())({hash: idHash}));
                
                  // bind close function on newly created tab
                  $('#tab-close-' + idHash).click(function() {
                      if ($(this).parent('li').hasClass('active')) {
                          // removed tab was active so display first tab (search tab)
                          $('#testlist-tabs a:first').tab('show');
                      }
                    
                      // remove tab item
                      $(this).parent('li').remove();
                  });
                
                  $('#test-case-details-' + idHash).html(new TestDetailsView({ test: test }).render().el);
              }
              
              // show test details tab
              $('#testlist-tabs a[href="#test-details-tab-' + idHash + '"]').tab('show');
          } 
    
        });
        
        return TestListView;
    });
}).call(this);