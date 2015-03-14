**TODO: Write this.**

The different parts should be split into own wiki pages when they grow larger, this page can link to them.

# Install #

  * Download
  * Unzip

# Configure #

  * Need to specify -Djava.library.path

# First Program #

  * Create 3D screen
  * Create world
    * Add a few sgine example objects
  * Add default debugging camera & world control to the scenegraph and setup keybingings for them - allows moving the camera around, shows FPS, maybe toggle wireframe or such things if it makes sense.  This should all be doable with an oneliner, and should be easy to remove as the application grows more ready.
  * Start

# Game Example - Fruit Fight #

  * ExampleGame

# Multiplayer example #

Quite a bit more complex, but hopefully sgine can hide some of the complexity.

  * Using the network facilities of sgine
  * Setting up a scenegraph on the server that communicates updates to clients
  * Setting up controls on client side that send messages to clients avatars on server side

# More Documentation #

  * More in-depth overview of the different parts of sgine
  * Look at the example sources
  * Scaladocs