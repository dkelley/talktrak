
# coffee -cw trak_point.coffee

$.ajaxSetup
  headers: 
    'X-Api-Stateful': true

window.App = App = {
  c: {}
  m: {}
  v: {}
}    

_.templateSettings.variable = 'd'


class TrakPointModel extends Backbone.Model
  defaults: ->
    trakId: `currentTrakId`
    title: 'Sample Title'
    description: 'Add your notes here...'
    highlighted: false
    editable: trakAction=="edit" || trakAction=="collaborate" 
  validate: ->
    return "Title cannot be empty" if _.isEmpty @get('title')
    return "Error no id" if _.isNull @get('trakId')
  highlightPoint: ->
      (@sync || Backbone.sync).call @, 'highlightPoint', @,
        url: "#{@url()}/highlight",
        type: 'POST',
        complete: =>
          @set(highlighted: true)
  retrievePoint: ->
      (@sync || Backbone.sync).call @, 'retrievePoint', @,
        url: "#{@url()}/retrieve",
        type: 'POST',
        complete: =>
          @set(highlighted: false)
  urlRoot: '/api/point'
  idAttribute: 'pointId'

class TrakPointCollection extends Backbone.Collection
  url: '/api/point'
  model: TrakPointModel


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
      <h3 class="trakTitle" contenteditable="true"><%= d.title %></h3>
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
    @collection.add(@model, { at: 0 })
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
    callback = -> $('h3.trakTitle').focus()
    setTimeout callback, 1000
    
    @$el.html(@template(@model.toJSON()))
    this

# A view for the individual note block, clicking on it opens the EditPointView
class TrakPointView extends Backbone.View
  tagName: "li",
  className:'trak-point',  
  events:
    'click': 'editPoint'
    'click .js-delete' : 'deletePoint'
    'click .js-highlight.pull' : 'retrievePoint'
    'click .js-highlight.pass' : 'highlightPoint'
  initialize: ->
    @listenTo(@model, 'change', @render)
    @listenTo(@model, 'remove', @remove)
  deletePoint: ->
    if confirm('Are you sure you would like to delete this point') then @model.destroy()
  highlightPoint: ->
    @model.highlightPoint()
  retrievePoint: ->
    @model.retrievePoint()    
  editPoint: (e) ->
    if $(e.target).hasClass('js-delete') || $(e.target).hasClass('js-highlight') 
      e.preventDefault()
    else      
      trakPointModal = new EditPointView({model: @model, collection: @model.collection}).$el.modal()
	  
  template: _.template("""
      <% if (d.editable) { %>
		<% if (trakAction == 'collaborate') { %>      
	        <% if (d.highlighted) { %>
	        <span class="trak-highlight trak-action js-highlight pull">Pull...</span>
	        <% } else { %>
	        <span class="trak-highlight trak-action js-highlight pass">Pass...</span>
	        <% } %>	    
        <% } else if (trakAction == 'edit'){ %>
      		<span class="trak-del trak-action js-delete">Delete</span>
      	<% } %>
      <% } %>
      <h3><%= d.title %></h3>
      <% if (d.description.length > 150) { %>
        <p><%= d.description.substring(0,150) + "..." %></p>
      <% }else{ %>
        <p><%= d.description %></p>
      <% } %>
  """)
  render: ->
    @$el.html(@template(@model.toJSON()))
    @$el.attr('id', @model.id)
    this;

# A container for the different TrakPoints, adding new views as different trak points are added.
class TrakPointListView extends Backbone.View
  initialize: ->
    @listenTo(@collection, 'add', @addPoint)
    @listenTo(@collection, 'reset', @render)    
  events:
    'click .js-addPoint' : 'addNewPoint'
  addNewPoint: (e) ->
    e.preventDefault()
    new EditPointView({model: new TrakPointModel, collection: @collection}).$el.modal()    
  addPoint: (model) ->
    @newPoint = new TrakPointView({model: model}).render()
    #@newPoint.$el.insertAfter(@$el, '.js-addPoint')
    #@newPoint.$el.insertAfter('.js-addPoint')
    $('#trakPoints').append(@newPoint.$el);
  template: _.template("""
  		<div class="js-addPoint">
			<a href="#" class="btn btn-large btn-info">+ Add Point</a>
		</div>
  """)
  render: ->
    @$el.empty()
    @$el.html(@template)
    @collection.each @addPoint, @
    this

@points = new TrakPointCollection()
@pointListView = new TrakPointListView({collection: @points, el: $("#trakPoints")})
if (currentTrakId)
  @points.url = '/api/trak/' + `currentTrakId` + '/points'
  @points.fetch()
else
  @pointListView.render()

