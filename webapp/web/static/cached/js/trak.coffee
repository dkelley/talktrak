
# coffee -cw trak.coffee

# Models:
# ----
# AccountModel   - Account data
# TrakPointModel - Data for individual Trak "point"
# TrakModel      - Data for a Trak, has a "points" property, which contains the points related to that model


# Collections:
# ----
# TrakPointCollection - A group of TrakPointModels
# TrakCollection      - A group of TrakModels

# Views:
# ----
# MainNavView        - A view for the navigation in the header. When the route changes it
#                      calls "handleRoute" which updates the UI
#
# TrakEditView       - Main container view, for both the text editor area, and the point list
# TrakTextView       - A view for the trak editor area
# TrakPointView      - A view for the individual note block, clicking on it opens the EditPointView
# TrakPointListView  - A container for the different TrakPointView's, adding new views as different trak points are added to the collection.
# EditPointView      - A modal shown when "add Note" is clicked
#
# TrakListView       - A list of all traks, used on the /traks (home) route

window.App = App = {
  c: {}
  m: {}
  v: {}
}

$.ajaxSetup
  headers: 
  	'X-Api-Stateful': true
  	
  	
#    'X-Api-': 4

_.templateSettings.variable = 'd'

# Models & Collections:

class AccountModel extends Backbone.Model
  idAttribute: 'accountId'

class TrakPointModel extends Backbone.Model
  defaults: ->
    trakId: if App.m.current then App.m.current.id else null    
    title: 'Sample Title'
    description: 'Add your notes here...'
  validate: ->
    return "Title cannot be empty" if _.isEmpty @get('title')
    return "Error no id" if _.isNull @get('trakId')
  urlRoot: '/api/point'
  idAttribute: 'pointId'

class TrakPointCollection extends Backbone.Collection
  url: '/api/point'
  model: TrakPointModel

class TrakModel extends Backbone.Model
  urlRoot: '/api/trak'
  idAttribute: 'trakId'
  initialize: ->
    @points = new TrakPointCollection()
    if App.m.current
	    @points.url = @.url() + '/points'
    	@points.fetch()

class TrakCollection extends Backbone.Collection
  url: '/api/trak'
  model: TrakModel
  parse: (resp) ->
    return resp.traks


# Views:

# A view for the navigation in the header. When the route changes it
# calls "handleRoute" which updates the UI
class MainNavView extends Backbone.View
  events:
    "click [signOutLink]" : "signOut"
  template: _.template """
  <ul class="nav traksnav">
    <li>
      <a href="#traks">Traks</a>
    </li>
    <li>
      <a href="#account">Account</a>
    </li>
    </ul>    
    <ul class="nav pull-right">
    <li>
      <a signOutLink href="javascript://">Sign-out</a>
    </li>
    </ul>
  """
  initialize: ->
    this.render()
    this.listenTo Backbone.history, 'route', @handleRoute

  signOut: (e) ->
  	`stack.api({
			url: "/api/accounts/sign-out",
			method: "POST",
			onSuccess: function(response) {
				var destinationUrl = stack.getUrlParameter("destinationUrl");				
				location.href = destinationUrl ? destinationUrl : response.destinationUrl;
			},
			onFailure: function(error) {
				$("[errors]").text(error.description).show();
			}		
		});`
	
  render: ->
    @$el.html(@template())
    this

  handleRoute: (router, route, args) ->
    @render()
    if route is 'showTrak'
      trakId = args[0];
      @$el.find('.traksnav').append("<li><a href='#traks/#{trakId}'>Trak #{trakId}</a></li>")
    if route is 'createTrak'
      App.m.current = null
      @$el.find('.traksnav').append("<li><a href='#traks/create'>CreateTrak</a></li>")
    @$("a[href='##{Backbone.history.getHash()}']").each ->
      $(@).parent('li').addClass('active')

# A modal shown when "add Note" is clicked
class EditPointView extends Backbone.View
  id: 'trakPoint'
  className: 'modal hide fade'
  events:
    "click .js-savePoint" : "savePoint"
    "click .js-closeModal": "closePoint"
  template: _.template("""
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
      <h3 class="trakTitle" contenteditable="true">Trak Point</h3>
    </div>
    <div class="modal-body">
      <p class="js-description" style="height:300px" contenteditable="true"><%= d.description %></p>
    </div>
    <div class="modal-footer">
      <!-- a href="#" class="btn pull-left btn-info">Upload File</a -->
      <a href="#" class="btn js-closeModal">Close</a>
      <a href="#" class="btn btn-primary js-savePoint">Save changes</a>
    </div>
  """)
  
  initialize: ->
    @render()
  
  savePoint: (e) ->
    e.preventDefault()
    @model.set({
      description: @$('.js-description').html()
      title: @$('.trakTitle').html()
    })
    console.log @model
    @collection.add(@model)
    @model.save(null, {
      error: =>
        alert('Error saving the point!')
        if @model.isNew() then @collection.remove(@model)
    }).done =>
      @$el.modal('hide')
  
  closePoint: (e) ->
    e.preventDefault()
    this.$el.on 'hidden', =>
      @remove()
    this.$el.modal('hide')
  
  render: ->
    @$el.html(@template(@model.toJSON()))
    this

# A view for the trak editor area
class TrakTextView extends Backbone.View
  events: 
    'click .save-button' : 'saveTrak'
    'mousedown .editable': 'editableClick'
  template: _.template("""
    <div id="trakEditor" class="well">
      <div id="mainEditor" data-button-class="all" class="editable trakContainer"><%= d.description %></div>
    </div>
  """)
  initialize: ->
    @listenToOnce(@model, 'sync', @render)
  
  editableClick: etch.editableInit

  render: ->
    @$el.empty();
    @$el.append(@template(@model.toJSON()))
    @$el.append('<button class="btn btn-success save-button">Save Button</button>');
    this

  saveTrak: ->
    @model.save({description:$("#mainEditor").html(), title:"New Trak"}, {success: =>
    	Backbone.history.navigate("traks/#{@model.id}", {trigger:true})
    })
    

# A view for the individual note block, clicking on it opens the EditPointView
class TrakPointView extends Backbone.View
  className:'trak-point'
  events:
    'click': 'editPoint'
    'click .js-delete' : 'deletePoint'
  initialize: ->
    @listenTo(@model, 'change', @render)
    @listenTo(@model, 'remove', @remove)
  deletePoint: ->
    if confirm('Are you sure you would like to delete this point') then @model.destroy()
  editPoint: (e) ->
    if $(e.target).hasClass('js-delete') 
      e.preventDefault()
    else
      new EditPointView({model: @model, collection: @model.collection}).$el.modal()
  template: _.template("""
      <span class="trak-del js-delete">Delete</span>
      <h3><%= d.title %></h3>
      <p><%= d.description %></p>
  """)
  render: ->
    @$el.html(@template(@model.toJSON()))
    this;

# A container for the different TrakPoints, adding new views as different trak points are added.
class TrakPointListView extends Backbone.View
  initialize: ->
    @listenTo(@collection, 'add', @addPoint)
    @listenTo(@collection, 'reset', @render)
  addPoint: (model) ->
    @$el.append(new TrakPointView({model: model}).render().el)
  render: ->
    @$el.empty()
    @collection.each @addPoint, @
    this

# Main container view, for both the text editor area, and the point list
class TrakEditView extends Backbone.View
  initialize: ->
  	@listenTo @model.points, "reset", @render
  	@listenTo @model, "all", @render
  events: 
    "click .js-addPoint" : "addPoint"
  template: _.template("""
    <div class="span8">
      <div id="js-trakTextView"></div>
    </div>
    <div class="span4">
      <div class="well">
      <% if (App.m.current && App.m.current.id) { %>
      <hr>
      <a href="#" class="btn btn-large btn-info js-addPoint">+ Add Point</a>
      <% } else { %>
      Save Trak To Add Points
      <% } %>
      <div id="trakPoints" class="well"></div>
      </div>
    </div>
  """)
  addPoint: (e) ->
    e.preventDefault()
    new EditPointView({model: new TrakPointModel, collection: @model.points}).$el.modal()
  render: ->
    @$el.html(@template())
    _.defer =>
      new TrakTextView({model: @model, el: $("#js-trakTextView")}).render()
      new TrakPointListView({collection: @model.points, el: $("#trakPoints")}).render()
    this

# A list of all traks, used on the /traks (home) route
class TrakListView extends Backbone.View
  className: 'span12'
  events:
    'click tr[data-trakId]'  : 'showTrak'
  template: _.template("""
    <table class="table table-hover">
      <thead>
        <tr>
          <th>Trak Title</th>
          <th>Creator</th>
          <th>Role</th>
          <th>Last Updated</th>
        </tr>
      </thead>
      <tbody>
        <% if (d.length > 0) { %>
          <% _.each(d, function (trak) { %>
          <tr data-trakId="<%= trak.trakId %>">
            <td><%= trak.title %></td>
            <td><%= trak.username %></td>
            <td><%= trak.role %></td>
            <td><%= new Date(trak.updatedDate) %></td>
          </tr>
          <% }); %>
        <% } else { %>
          <tr class="js-addTrak">
            <td colspan="3">There are no Traks yet. Click to add one</td>
          </tr>
        <% } %>
      </tbody>
    </table>

    <hr>

    <% if (d.length > 0) { %>
    <div class="centered">
      <a href="#traks/create" class="btn btn-large btn-success">Add new Presentation</a>
    </div>
    <% } %>
  """)
  
  showTrak: (e) ->
    e.preventDefault()
    id = $(e.currentTarget).attr('data-trakId');
    Backbone.history.navigate("traks/#{id}", {trigger:true});
    
  render: ->
    @$el.html(@template(@collection.toJSON()))
    this

class Router extends Backbone.Router
  routes:
    'traks/create' : 'createTrak'
    'traks/:id' : 'showTrak'
    'traks': 'home'
    'account': 'account'
    '': 'redir'

  redir: ->
    Backbone.history.navigate('traks', {trigger: true});

  home: ->
    $("#appArea").html(new TrakListView({collection:App.c.traks}).render().el);

  account:  ->
    $("#appArea").html('<div class="span12">Coming Soon</div>');

  createTrak: ->
    $("#appArea").html(new TrakEditView(model:new TrakModel()).render().el);

  showTrak: (id) ->
    model = App.c.traks.get(id) || new TrakModel({trakId: id})
    App.m.current = model;
    $("#appArea").html(new TrakEditView(model:model).render().el);

$ ->

  # Create a new Collection, containing the app's "Traks"
  App.c.traks = new TrakCollection()

  # Once the Traks are done fetching, we go ahead and create the "MainNav",
  # create the router (which handles the routes & hash changes),
  # start the 'history' (to trigger the route)
  # and show the footer.
  App.c.traks.fetch().done ->
    App.nav    = new MainNavView(el: $("#talktrak-nav"))
    App.router = new Router()
    Backbone.history.start({root: '/home'})
    $('.footer').show()


# Roles
# 1 - owner
# 2 - editor
# 3 - assistant

# Account
  # account_id
  # username
  # email
  # first_name
  # last_name
  # phone_number
  # password
  # remember_me_token
  # api_token
  # updated_date

# Point
  # point_id
  # trak_id
  # description
  # created_by
  # updated_date

# Trak
  # trak_id
  # title
  # description
  # updated_date

# -- test123 = 'cbaeb0cc5d74a93390b27cf215492563'
# INSERT INTO account (account_id, username, email, first_name, last_name, phone_number, password, remember_me_token, api_token, updated_date) VALUES(1, 'dkelley','test@xmog.com', 'Dan', 'Kelley', '6103061733', 'cbaeb0cc5d74a93390b27cf215492563', '1', '1', now());

# INSERT INTO trak(trak_id,title ,description, updated_date) VALUES(1, 'Test Trak', '<h1>Test Trak</h1>The is a <b>test trak</b>, <br/>it is just bs', now());
# INSERT INTO trak(trak_id,title ,description, updated_date) VALUES(2, 'Talk Trak', '<h1>Talk Trak</h1>The is a <b>test trak</b>, <br/>it is just bs<br/>
# Phasellus mattis ultricies dapibus. Vivamus consequat ultrices nibh, id consectetur neque feugiat id. Ut at dolor lorem. Aenean in neque velit, eget adipiscing purus. Aliquam erat volutpat. Proin quis augue commodo turpis venenatis rhoncus. Donec adipiscing tristique laoreet. Suspendisse potenti. Donec vehicula nulla ut orci suscipit in porta nibh feugiat. Mauris adipiscing congue volutpat. Integer sit amet tortor ligula. Aenean scelerisque lectus eu diam condimentum accumsan pretium nulla aliquam. Nulla molestie mollis nisl, eget vehicula risus tincidunt quis.<br/>
# Ut hendrerit ultricies dolor, ac sagittis nisl egestas vel. Aenean pellentesque euismod nisi, eu malesuada ipsum porta vel. Pellentesque augue est, dapibus sit amet fringilla sed, convallis non magna. Nulla dolor tortor, ultrices sed tempus a, rhoncus et magna. Quisque vel sem aliquam felis adipiscing consequat. Morbi lobortis aliquet arcu, vel pulvinar lorem rutrum vitae. Sed iaculis gravida orci eu accumsan. Pellentesque eget sollicitudin felis. Quisque lorem lorem, viverra ac molestie ac, dapibus sed augue. Nam venenatis, neque id interdum pretium, augue nulla tristique quam, sit amet suscipit libero diam sed ipsum. Fusce in ullamcorper est.', now());

# INSERT INTO point(point_id, trak_id, description, created_by, updated_date) VALUES(1,1, 'This is a very important point, you wouldn''t believe how important it is', 1, now());

# INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(1,4,1);
# INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(1,4,2);

