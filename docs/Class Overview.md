# Class Overview

## App
The entry point — `main` runs here and sets up the window, camera, field, and hive, then drives the game loop that draws and updates every other object.

## AssetManager
Not currently used anywhere. Reserved for centralising texture/asset loading (e.g. bee and flower textures) so individual classes don't each load their own copies.

## Bee
Base class for all bees. Holds shared state (name, position, texture, speed/scale) and the `Draw` method that billboards the sprite and builds its collider. `update` is a no-op here — it's overridden by subclasses.

## BeeWorker
Child of `Bee`. Implements the worker behaviour as a state machine (`RESTING`, `FETCHING`, `SAPPING`, `STACHING`, `ATTACK`) driven from `update`: rests, picks a flower, moves to it, drains its pollen into nectar, then returns nectar to the hive as honey.

## Enemy
Implements `CollisionBox` to get a bounding box on its position, and `pickBeeTarget()` picks a random bee from the hive to target. Note: touching/collision detection against a bee isn't implemented yet — only the collider and the random-target logic exist so far.

## Field
Child of `Location`. Represents the flower field: spawns flowers at random positions (`SpawnFlower`), replaces flowers once they're emptied of pollen (`fieldCheck`), and stores them in `flowerfield`.

## Flower
Child of `Location`. Each flower starts with 100 pollen (set by `Field.SpawnFlower`), tracks whether it's `occupied` by a bee and whether it's `empty` (pollen depleted).

## Hive
Child of `Location` (not a parent class of anything itself — nothing currently extends `Hive`). Creates bees (`CreateBee`), lets a bee pick an unoccupied flower (`pickFlower`, throwing `NoAvailableFlowerException` when none are free), and draws itself (`DrawHive`).

## Location
Abstract base class implementing `CollisionBox`. Initialises an entity with a name and a position — used as the common parent for `Field`, `Flower`, and `Hive`.

## NameManager
Not currently wired into any other class. Loads a word list from file, enforces a fixed word length (`wordLength`) as the boundary for valid words, and picks/stores a random word (`randomWord`/`chosenWord`) — intended for generating entity names.

## QueenBee
Child of `Bee`. Alternates between `RESTING` and `LAYING` states on a timer; once the egg interval elapses, she "lays" by calling `myHive.CreateBee()`.

## Wasp
Child of `Enemy`. Currently just tracks `beesKilled`, `honeyStolen`, and a `target` bee picked on construction — it doesn't yet have its own state machine like `BeeWorker`/`QueenBee` do, though that seems to be the intended direction.
