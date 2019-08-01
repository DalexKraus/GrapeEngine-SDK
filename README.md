## About GrapeEngine-SDK:
The GrapeEngine-SDK is a *Software Development Kit* for the **GrapeEngine**, a dynamic 2D game-engine written in Java.
This engine makes use of OpenGL and GLFW using LWJGL (https://www.lwjgl.org/).


## Important:
Please note, that this is only a spare time project for me, and although I try to work
on this project as frequently as possible, I am unable to give any support at the moment.

## (Planned) Features:
(This list is only a rough sketch and does not contain everything)

  * Easy to use Graphics-Utility
  * Tilemaps
    - Layer Support
    - Chunk System (Allows for Infinite Maps and Dynamic loading)
    - Entities
    - Multiple Collision Layers
    - **Recursive** Templates and Prefabs
    - Instanced Rendering (Performance)
  * Lighting
    - Baked Lightmaps (static Lighting)
    - Dynamic Lighting
    - Colored Lights
    - Directional Lighting (e.g. Flashlight)
    - Shadow Maps
  * Tile-Based Animations
    - Uses Atlas as source
    - Repeatable
    - Animation Editor
    - Speed Manipulation (also works in reverse)
  * Particles
    - Animation as source
    - Particle Systems (Forms and Timed Sequences)
    - Instanced Rendering (Performance)
  * Post Processing:
    - Node Pipeline for constructing Effects
    - Kernel Support
    - _Possible_ Shading pipeline

## Why dynamic?
Well, there are many things that can be considered dynamic in a engine, 
but the term _"dynamic"_ mostly represents the feature to dynamically build together components,
even at runtime. Although this feature is not mentioned on the list, I am really looking forward implementing it.
More details about that topic coming soon.

## Screenshots
![Alt text](/alpha_screenshot.png?raw=true "Alpha Screenshot")
     
## Used Libraries
This SDK is dependent on the following libraries:
* [JSON-Simple](https://code.google.com/archive/p/json-simple/)
* [RichTextFX](https://github.com/FXMisc/RichTextFX)
