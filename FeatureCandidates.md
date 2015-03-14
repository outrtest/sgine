Possible features for sgine.  This is a brainstorming page for listing issues in early concept phase.  Not everything here will necessarily ever make it to sgine, and thigns may get implemented in different ways than listed here.

As a feature is selected for implementation, create an issue for it of type Enchancement, this way it is easier to track and will be included in the automatically generated changelog.




# Datamodel #

The basic datamodel for sgine will probably be a queryable tree structure.  It's data agnostic though, not really caring what kind of objects are stored in it, or how they are stored (they could even be generated on the fly in response to queries).

In particular, it doesn't store any 3D location of nodes.  **Hence the scenegraph name is a bit missleading, some other term would be great to find**.

It should probably support serialization of the content, to import or export it in pieces, or for updating between a client and server.

We might want to create a generic editor tool (or several specialized ones) that work with this datamodel, allowing the user to add resources to it and configure it, then load it from a relatively simple core application.  This way we can implement data-driven games and applications that are easy to configure and change.


# 3D Rendering #

The more traditional content of a 3D engine.

  * Geometry
  * Lighting
    * Basic OpenGL
    * Shadows etc
  * Shaders


# Alternative Rendering backends #

We could maybe also implement some alternative renderers, e.g.

  * Raytracer plugin for rendering still scenes.
  * Java2D Renderer for top-down overviews etc.


# Portal based rendering #

Allow nodes to have surfaces that show some other space beyond, with a transformation applied.  Support rendering of the portal, as well as traversing it.


# Particle system #

Create objects of some type, add them under a particle system node in the scenegraph, and modify the properties of the objects over time, or with some other functions.

The particles would not need to be just points, they could also for example have a tail along the path of the point (useful for e.g. airplane trails), etc.

Could also be interesting to consider update functions like flocking algorithms.


# Animation #


# Procedural shapes #

System for generating parametriced 3D shapes based on components.

Could be used e.g. for easily creating random stone boulders, trees, flower models, etc.

Could even have some primitive for animated creatures with limbs.


# Pictures #

Both simple loading from disk using Java image API:s, but also maybe procedural generation of textures on the CPU or GPU.  Most probably it would make most sense to generate a set of needed textures when first needed and cache them, instead of doing any complex on-the-fly rendering on the GPU.

Generated textures could utilize the shape of the object they are intended for as input, e.g. creating moss on the top of a boulder, or hair that is directed away from the nose of an animal.


# Video #

Video support is planned.


# Audio #

  * 3D positional audio
  * Music
    * mp3, ogg, etc
    * Tracker formats
  * Sound effects

## Generated Audio ##

Another idea could be to create noise and sound effects with procedural generation - configure various sound generators, filters, etc, to produce sound effects.  This could be used for pre-generating a set of sound effects, or for interactively generating sound.  The latter however will be subject to processing power and latency issues, so may not be practical.


# Input #

We might want to implement a middle layer between keyboard, mouse etc and application callbacks that would allow binding keys and mouse axis to different named actions / inputs provided by the application.  We could provide a default editor for those bindings (as this will be useful in most all games), and serialization of the bindings.

Different parts of a game might provide different actions to bind keys to (e.g. controlling a tank or helicopter could be different).  In these cases the game can either offer a set of actions for each case, or provide a set of generic actions that are then mapped to situation specific needs internally in the game.


# User Interface #

Various widgets, that can be nested in a scenegraph.

Layout managers that can lay out the widgets contained in a scenegraph node.

Skinnable look and feel, can use the procedural picture system to load or render the ui parts.

It would be nice to be able to automatically generate property editors for an object using reflection.

## Charts / Reporting ##

Advanced dynamic charts comparable to that of Flex to provide animation and data-driven visual representation

# Physics #

## Newtonian physics ##

Straghtforward implementation that just moves objects according to their acceleration and velocity.

An extension is to keep track whether an object is resting against a surface (terrain / room walls), or in free fall.

Objects could be configured to be either blocking or pass through for each other, depending on type.

## Collision Detection ##

Done by some container that keeps track of physical objects in some area.  Different implementations or outside bindings of various complexity could be done as sgine matures, starting with simple approaches that store objects in some spatially indexed structures like quadtrees for faster neighbour finding.


# Networking #

The scenegraph datastructure should provide interfaces that allow updating it and listening to changes, so that synchronization can be done.

Often we just want to send some updates, e.g. object appearance, dissappearance, visible property changes, and movements from server to client and action requests from client to server.

We could provide support for different backends for networking / servers, to allow users to pick their favourite technology or write their own.  For example SGS Darkstar could be supported, and we might implement a simple client server model of our own too.