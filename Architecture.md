TODO: Rename to space or some such, or rewrite as a usage example / guide when the code is ready.

Some of these ideas are a bit changed, refer to sources for the latest understanding.

# Spatial Object Management #

From Approach D in the notes at http://etherpad.com/ep/pad/view/USOfh8JaEl/rev.2803

Based mostly on ideas by MatthiasM and zzorn.

  * I'd suggest using this approach -- zzorn

  * MatthiasM recommends using 48.16 bit fixed point coordinates (but floats could be used for directions)
    * This would give about 0.03 light years range at 0.01 mm resolution :)
    * Did some test runs with fixed point vs doubles, and doubles do beat even manually inlined fixed point math, and are much easier to use.  Will have to evaluate the precision of doubles vs fixed point longs, but as it looks now doubles are probably a better choise.

## Space ##
  * methods for getting nodes in some volume (e.g. in some distance from a point)
  * Contains / references Nodes that have a position and bounding size
  * Different implementations for different kinds of geometries (terrain, space, indoors, etc).
  * Some possible implementations could be BruteForceSpace, QuadTreeSpace, OctTreeSpace, BSPSpace, RTreeSpace.
  * Also allows Spaces that generate objects on the fly (e.g. generating tree entities in a forest for a queried area).
  * When rendering a space, the opengl camera can be projected to the space, and then the space objects just converted to floats with one division by 65536.0f
    * This approach doesn't work if something assumes the world is all in the same coordinates, e.g. the default OpenGL fog implementation.  But we could allow writing own fog shaders and whatever that get the correct projection as parameters.
  * Spaces should take care of collision detection.
  * Physics could be bound to nodes, or to Spaces.

## SpaceView ##
  * Works a bit like an iterator - provides all objects inside a space that are in a specified volume with a specified level of detail.
  * The SpaceView can be moved around.
  * The SpaceView has some methods for iterating its contents as a normal collection.
  * For typical perspective rendering we'd like high level of detail around the camera and lower level of detail with increasing distance, up to quite large distances.
    * This could be implemented either with a grid of SpaceViews that are smaller near the camera and larger further out (subdivided / quad tree like), or as a specialized SpaceView that encapsulates this logic, and changes the level of detail for objects based on distance (we'll want something like this to make users life easy).
    * Some objects such as terrains are continuous but have the same principe of higher level of detail closer to the camera.  They might or might not be handled in SpaceViews.

## Portals ##
  * Connects two spaces
  * A polygon, or a boundary sphere or arbitrary edge (e.g. a pirate ship space could have a portal to the surrounding world that starts at the edges of the pirate ship).
  * The arrive and depart planes are placed a bit apart to avoid hysteresis of objects between spaces

## SpaceNode extends Node ? ##
  * A node that contains other nodes inside some space?
  * We want to be able to nest Spaces (e.g. a BruteForceSpace for a pirate ship deck in a more sparse QuadTreeSpace for some kind of archipelago world)