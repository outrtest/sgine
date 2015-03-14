# Example Game - Fruit Fight #

In this example game you are a weird creature that picks up fruits and throws it at other weird creatures.  (Better game idea could also be used, as long as it is simple to implement with sgine :).

Most of the functionality used will be provided by sgine - e.g. UI system, procedural terrain / object framework, input handling, default Newtonian physics, scenegraph serialization, maybe even behavior state machine.  The game just sets up things, configures them, and adds objects to the scenegraph.  In total the example should be under 100 lines of code, perhaps 60 or so.

  * World with
    * Procedural Terrain
    * Skybox
    * Trees, grass, flowers
    * Background music / noise
  * Menu
    * New game, quit, options, return to game (if the players avatar is alive)
    * Save and load game by serializing the scenegraph
    * Show the world in the background nehind the menu, but maybe with blur effect or such.
  * Bind the escape key to toggle the menu on and off
  * Configure simple physics process for the world
    * Apply velocity & friction & gravity to Physical objects.  Friction is higher for object moving along the ground.
    * Detect collisions between Physical objects, and notify them
    * Block movement through trees etc.
  * Create a HUD widget for the UI to display picked up fruit objects, score, and hitpoints of the players avatar
  * Create a fruit class
    * Create a randomized procedural model (sphere with some texture + some funny bumps etc)
    * specify some weight and such properties for the physics engine
    * handle a sufficiently hard impact with a creature by playing a splatter particle effect, playing a sound, and removing the fruit
  * Create a fruit spawning process, that creates new random fruit objects and drops them in random places in the world, if there is less than the maximum number of fruits in the world.
  * Create superclass for moving creatures, with
    * Movement max speed etc
    * Some kind of hp
    * Movement properties (velocity, direction) (inherited from Physical)
    * The model to show it with
    * Handle collision with fruit objects by decreasing hp, and calling a death method on zero or negative hp
  * Create player avatar that extends the creature class
    * Procedural animated character model
    * Add score / kill count property
    * Setup inputs to control it
    * Create an action for picking up nearby fruits (might be done automatically on low speed collision with them)
    * Create an action for throwing a fruit
    * Handle avatar death by dropping to the main menu
  * Create creeps that extends the creature class
    * Procedural animated creature model
    * Configure a behaviour with a state engine
      * Random walk state by default, and randomly triggered
      * Flight away from pain source triggered when hp is lowered
    * Handle creep death by showing a particle effect, playing a sound, and increasing the score of the killer.

The game can be made more interesting by adding creatures that throw back, levels with different kinds of scenery and creatures, various fruit effects (blast radius, poisonous to self, etc), and so on.  That's left as an exercise for the reader :)