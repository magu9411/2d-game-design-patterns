# 2d-game-design-patterns

Mini Java 2D game prototype that demonstrates how the Factory, Component, Observer, and State patterns interact inside a small teaching engine. The code is intentionally compact so that each pattern can be traced from UML-level discussions into concrete Java classes (see `docs/outline.tex` for the accompanying paper outline).

## Features
- **Component-based entities:** `Entity` composes behavior at runtime from input, rendering, AI, and player-state components.
- **State-driven player logic:** `PlayerStateComponent` hosts Idle/Moving/Attacking states that react to keyboard events and broadcast transitions.
- **Observer wiring:** `EventBus` decouples Swing input, gameplay systems, and HUD updates via lightweight events.
- **Factory-built world:** `EntityFactory` bundles the right components per entity type and seeds the playable demo (player + two patrolling enemies).

## Running the demo
The project has no external dependencies beyond a JDK (tested with openjdk 24.0.2).

```bash
# compile everything
make

# launch the Swing window (requires a desktop session)
make run

# cleanup compiled classes
make clean
```

### Controls
- Move with Arrow keys or `WASD`
- Press `Space` to trigger the Attacking state (the HUD shows the live state)

## Project layout
```
src/main/java/com/ooad/gameengine/
├── core/        -> Game loop, Swing surface, and world orchestration
├── entity/      -> Entity aggregate + lightweight vector math
├── components/  -> Component implementations (input, AI, rendering, state machine)
├── events/      -> Observer pattern primitives (EventBus + events)
├── factory/     -> EntityFactory for player/enemy creation
└── state/       -> Player state hierarchy (Idle, Moving, Attacking)
```

## Adapting/Extending
- Add new entity types by composing components in `EntityFactory`.
- Plug extra Observer flows by registering listeners on `EventBus` (e.g., scoring, audio cues).
- Introduce more states by implementing `PlayerState` and switching via the existing state component.
