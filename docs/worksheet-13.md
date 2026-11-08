# COMP2000 Worksheet 2 — Final Submission

**Student name:** Farah Abbas

**GitHub repo URL:** https://github.com/farahabbas123/COMP2000-BEESfarah

---

## 1. Design Patterns

Don't forget to submit a pdf file which shows a diagram of your pattern implemenation to supplement this text description.

**1.1.** Which design pattern(s) did you implement? Name them and state where each appears in your codebase.

We used the State pattern for the worker bees. The interface is BeeState.java, which has three methods: enter, update and exit. enter and exit are default methods that do nothing, so a state only has to override them if it actually needs to set something up or clean up after itself. BeeWorker.java is the context class. It holds a private currentState and every frame it just calls currentState.update(this, deltaTime), and it has a changeState method that calls exit on the old state and enter on the new one.

There are five concrete states and each one is its own file in src. RestingState is where every bee starts. The bee flies around in a random direction and every couple of seconds there's a random chance it goes off to get nectar. FetchingState asks the hive for a free flower using pickFlower, marks it as occupied and flies to it. Once the bee's collider touches the flower it switches to SappingState, which takes nectar from the flower until the bee is full or the flower is empty. Its exit method frees the flower so another bee can use it. Then StashingState flies the bee back to the hive and deposits the nectar into the hive's HoneyCapacity and the bee goes back to RestingState. AttackState can happen from fetching or sapping if a wasp starts attacking the bee. The bee finds the wasp that's targeting it and stings it back until one of them dies.

The diagram for this is in the pdf I submitted (also in docs/media/beeStatePattern-v2.drawio). The left side shows the classes and the right side shows how the bee moves between states and what causes each change.

We also made a Simulated interface with update(float deltaTime) and Draw(Camera3D camera), and Bee and Enemy both implement it. That means anything in the simulation can be updated and drawn the same way without the main loop needing to know what type it is. It's not really a design pattern on its own but it uses the same idea of coding to an interface instead of a specific class.

**1.2.** For each pattern, paste a code snippet showing the key interaction between participants.

This is the main interaction in BeeWorker. The bee doesn't have any behaviour code in update anymore, it just hands it off to whatever state it's in:

```java
private BeeState currentState;

@Override
public void update(float deltaTime) {
    currentState.update(this, deltaTime);
}

public void changeState(BeeState next) {
    if (currentState != null) currentState.exit(this);
    currentState = next;
    currentState.enter(this);
}
```

The states are the ones that decide when to change to the next state. This is from FetchingState, where the bee switches to sapping once it reaches its flower, or to attacking if a wasp is on it:

```java
if (CheckCollisionBoxes(bee.getCollider(), bee.Target.getCollider())) {
    bee.changeState(new SappingState());
} else {
    bee.Position = Vector3Lerp(bee.Position, offsetPos, deltaTime * bee.Speed);
}

if (bee.beingAttacked) {
    bee.changeState(new AttackState());
}
```

And this is SappingState's exit method, which shows why having enter and exit in the interface is useful. Whenever the bee leaves sapping, whether it's because it's full or because it got attacked, the flower always gets freed up:

```java
@Override
public void exit(BeeWorker bee){
    bee.Target.occupied = false;
    bee.Target = null;
}
```

So the normal loop is resting -> fetching -> sapping -> stashing -> resting, and fetching or sapping can jump to attacking if a wasp goes for the bee.

**1.3.** For each pattern, explain why you chose it over an alternative. What trade-off did you make?

The alternative was what we had before, an enum of states and a switch statement inside BeeWorker's update. That was fine when the bee only had a couple of states, but once we added attacking and the bees roaming around when they rest, the update method got really long and every case was messing with the same variables. Adding a new behaviour meant editing that one big method and it was easy to break something else. With the State pattern each behaviour is in its own class, so adding a new one is just making a new class that implements BeeState and nothing else has to change. It also made group work easier because people could work on different states in different files without getting merge conflicts all the time. The exit method is also useful because the clean up, like freeing the flower, happens in one place no matter how the bee leaves the state.

The trade-off is there are a lot more files, and you can't see the whole bee lifecycle in one place anymore, you have to jump between classes to follow it (which is part of why the diagram is useful). The states also access the bee's fields directly like Target, myHive and beingAttacked, so they're pretty tightly coupled to BeeWorker and wouldn't work for any other class. We also create a new state object every time the bee changes state instead of reusing them. That's a bit wasteful but it means each state keeps its own timers, like doTime in RestingState, and they don't get shared between bees by accident.

The wasps still use the old enum and switch approach (EnemyState in Enemy) and you can kind of see the difference, EnemyWasp's update is a lot longer and harder to follow than any of the bee states.

---

## 2. Streams / Lambdas / Parallelism

**2.1.** List every file in your project that uses lambda expressions or the Streams API.

App.java uses parallel streams to update the bees and wasps every frame, a lambda comparator to sort the flowers by how far they are from the camera so they draw in the right order, and switch expressions with -> for showing the stats of whatever you click on. AttackState.java has a stream with filter and findFirst to find which wasp is attacking the bee. AssetManager.java uses a stream over Files.list to load all the png textures in the assets folder. Hive.java has a removeIf lambda to remove dead bees, and Field.java has removeIf lambdas to remove dead wasps and flowers that have run out.

**2.2.** For the most sophisticated use, paste the code and explain: what does it do, and why was a stream/lambda better than a loop?

I think the one in AssetManager is the most complicated one:

```java
try (Stream<Path> stream = Files.list(dir)) {
    stream.filter(file -> !Files.isDirectory(file))
          .filter(file -> {
              String fileName = file.getFileName().toString().toLowerCase();
              return fileName.endsWith(".png");
          })
          .forEach(file -> {
              String fullPath = file.toAbsolutePath().toString();
              String rawFileName = file.getFileName().toString();
              int dotIndex = rawFileName.lastIndexOf('.');
              String key = (dotIndex == -1) ? rawFileName : rawFileName.substring(0, dotIndex);

              Texture texture = LoadTexture(fullPath);
              if (texture.id() > 0) {
                  textureMap.put(key, texture);
              }
          });
}
```

It goes through everything in the assets folder, skips any folders and anything that isn't a png, then loads each texture and puts it in a map with the file name minus the extension as the key, so wasp.png becomes "wasp". It also checks texture.id() > 0 so if a texture fails to load it doesn't get added. This means the rest of the code can just call assets.GetTexture("wasp") instead of writing out file paths everywhere, and if we want a new texture we just drop the png in the folder.

You could do the same thing with a for loop, but you'd need nested if statements and continue to skip the files you don't want, and it would be harder to see what the code is doing at a glance. With the stream each filter is one rule, so it reads like "files that aren't folders, that end in .png, load each one". Files.list already returns a stream as well, and putting it in try with resources means the folder gets closed properly after.

The one in AttackState is a lot shorter but I think it's a good example too:

```java
predator = Field.getWasps().stream()
        .filter(w -> w.target == bee)
        .findFirst()
        .orElse(null);
```

With a loop you'd need a temporary variable, an if and a break. Here it's one statement and orElse(null) handles the case where no wasp is attacking the bee.

**2.3.** Did you use parallel streams, custom collectors, or the Fork/Join framework? If yes, how did you ensure thread safety? If no, where could parallelism be introduced?

Yes, we used parallel streams in App.java to update all the bees and wasps every frame:

```java
hive.Bees.parallelStream().forEach(bee -> bee.update(deltaTime));
flowers.wasps.parallelStream().forEach(wasp -> wasp.update(deltaTime));
```

Honestly it isn't completely thread safe. We did a few things to help. Most of what a bee changes in its update is its own data like its position, nectar and timers, so bees running at the same time don't really affect each other there. We also call BeeCheck and fieldCheck before the parallel streams, which is where dead bees, dead wasps and empty flowers get removed and new flowers get spawned, so the lists aren't being resized while they're being streamed over. All the drawing is done afterwards on the main thread because raylib isn't thread safe.

But there are still some shared things that could cause problems. Two bees in FetchingState could call pickFlower at the same time, both see the same flower as not occupied, and both take it. Wasps lower a bee's Health and increase the static totalBeesKilled counter while the bee might be changing its own health in AttackState at the same time. And HoneyCapacity in the hive gets added to by bees stashing nectar, which could lose updates if two bees stash at once.

To fix it, I'd make pickFlower synchronized or change occupied to an AtomicBoolean and use compareAndSet so only one bee can claim a flower, and use AtomicInteger for totalBeesKilled and HoneyCapacity. Another option would be to collect the damage during the parallel update and apply it after on one thread. To be honest we don't have that many bees, so it probably runs just as fast without parallel streams, it was more to try it out.

---

## 3. Uniqueness and Creativity

**3.1.** What did you add to your simulation that went beyond the in-class activities? Describe the feature and what inspired it.

The main thing is that the colony kind of runs itself. The queen only lays a new bee when the hive has at least 200 honey, and laying the egg uses that honey up. The wait between eggs is also random, between 5 and 20 seconds. That means the number of bees depends on how much nectar the workers manage to bring back, so you don't just get bees appearing forever. I worked on the queen bee so this is the part I'm most proud of. I got the idea from how real hives need enough food stored before they can grow.

We also added wasps that fly around the field and hunt bees, and the bees can sting back so it's not always the wasp that wins. Flowers run out of pollen and get removed, and new ones grow back over time, sometimes two at once when one runs out, so where the food is keeps changing. It's all in 3D using raylib and you can click on a bee, flower, the hive or a wasp to see its stats.

**3.2.** Paste a code snippet or describe a design decision that demonstrates emergent behaviour, non-trivial complexity, or a genuinely surprising outcome.

This is the queen's update:

```java
eggTimer += deltaTime;
if (myHive.HoneyCapacity >= eatAmount && eggTimer > eggInterval){
    eggTimer = 0f;
    myHive.CreateBee();
    myHive.HoneyCapacity -= eatAmount;
    eggInterval = new Random().nextFloat(5f, 20f);
}
```

On its own it's simple, but it gets interesting when you put it together with everything else. More bees means more nectar which means more honey which means more bees. But more wasps means fewer bees and less honey, and if there are too many bees they use up the flowers faster than they grow back. So the population goes up and down by itself even though we never wrote any code that controls the population directly. One thing that surprised me is that when a wasp kills a bee it steals the nectar the bee was carrying, so a bee that dies on the way back to the hive is a much bigger loss than one that dies on the way out.

A problem we had to solve was bees fighting over flowers. At first more than one bee could go to the same flower, so we added an occupied flag that gets set when a bee picks a flower. But then when every flower was taken, pickFlower kept calling itself looking for a free one and the program crashed. I fixed this by checking if any flower is free first and throwing a NoAvailableFlowerException if not. FetchingState catches it and sends the bee back to RestingState so it tries again later instead of crashing. Bees also have to give their flower back when they leave it, which is what SappingState's exit method does, and if a bee dies while fighting, AttackState calls releaseFlower so the flower doesn't stay stuck as occupied forever. So when there are lots of bees and not many flowers, you can actually see bees hanging around waiting for a flower to free up.

**3.3.** If you had another 6 weeks, what would you add or change?

I'd add another bee type like a guard bee that stays near the hive and goes after wasps, and maybe larvae so new bees have to grow up first instead of just appearing. I'd also change the wasps to use the State pattern like the bees so the code is consistent, and properly fix the thread safety problems from 2.3. We also planned an exterminator enemy that we didn't get to, and it would be cool to have day and night or seasons that change how many flowers grow. And I'd clean up the code a bit because a lot of variables are public or static when they probably shouldn't be, like Hive.Bees and Field.wasps, which makes it too easy for any class to change them.

---

## 4. Log Book and Final Reflection

**4.1.** Attach or link your log book entries for Weeks 7–13.

I didn't keep a weekly log book this semester, so I don't have entries to attach. Instead this is a summary of what I worked on, written at the end of semester from my commit history in the repo, so the dates come from my commits and not from log entries.

7 September: I added the QueenBee class (commit 78d184a). It extended Bee and used an enum with LAYING and RESTING states and a switch statement to lay a new bee every few seconds through the hive. I also added the code in App and Hive to create the queen and update her each frame.

10 September: I added the NoAvailableFlowerException and the generic pickRandom method in Hive (commit 75e35fc). Before this, if every flower was taken the bees would keep looking for a free flower and the program would crash. Now pickFlower checks first and throws the exception, and BeeWorker handles it. pickRandom works with any ArrayList, so it was used for picking flowers and later for the wasps picking a bee to attack. I merged this into main with pull request #1 the same day.

12 September: I committed my Week 7 worksheet into docs/Class Overview.md (commit 516d6ea), which listed and explained our classes and inheritance.

23 October: I merged the latest main into my branch and added the Simulated interface with update(float) and Draw(Camera3D), and made Bee and Enemy implement it (commit c4f0e93). This means the main loop can update and draw bees and wasps the same way.

After that, the queen was simplified by the team to a timer plus a honey check, so she only lays eggs when the hive has enough honey, and the worker bees were moved to the State pattern. I wasn't the main author of those changes, but my exception is what FetchingState uses to send a bee back to resting when there's no free flower, and my generic and interface are still used across the project.

Not all of my contribution shows up in the commits. I was also part of the design discussions in our team group chat on WhatsApp, where we talked through and made decisions about the design of the simulation together. Two decisions I was part of were choosing the State pattern for the worker bees and making the queen depend on honey. We went with the State pattern because the worker bee's switch statement was getting too long as we added more behaviours like attacking and roaming, and splitting each behaviour into its own class made it easier to add new ones and let us work on different states without clashing. We made the queen depend on honey so the colony couldn't just grow forever. Linking egg laying to how much nectar the workers bring back means the population goes up and down depending on how well the hive is doing, which makes the simulation feel more like a real hive.

**4.2.** Reflect on the semester:

- What was the most important concept you learned?

  I think the most important thing I learned was how interfaces and generics let you write code once and use it for lots of different things. When I made the pickRandom generic in Hive I was only thinking about picking a random flower, but because it takes an ArrayList of any type T it ended up being reused for the wasps picking a random bee to attack (Enemy.pickTarget calls Hive.pickRandom(Hive.Bees)) without having to write a second method. That's the main reason I made it generic instead of just writing a pickRandomFlower method, it was clear other things in the simulation would need to pick something random from a list too. Same with the Simulated interface, since Bee and Enemy both implement it the main loop doesn't need to know what kind of thing it's updating. I also learned that exceptions aren't just for errors, they can be used to handle a normal situation in the simulation. NoAvailableFlowerException is a RuntimeException so it doesn't have to be declared everywhere, and it lets FetchingState deal with there being no free flowers instead of the program crashing. Bianca ended up using the same idea for the wasps with NoAvailableBeeTarget, which showed me it was actually a useful way of doing it.

- What design trade-off did you face, and how did you resolve it?

  When I first made the queen bee I gave her an enum with LAYING and RESTING states and a switch statement in update (it's still commented out in QueenBee.java). It worked but the queen only really does one thing, wait and then lay an egg, so having a whole state machine for that was kind of overkill. Later we changed it so she just has a timer and a honey check, and she only lays an egg if the hive has enough honey. The trade-off was losing the clear states in exchange for simpler code that's actually connected to the rest of the simulation. I think it was the right call for the queen, but for the worker bees it was the opposite, they had too many behaviours for a switch so we moved them to the State pattern instead. So I learned that the right structure depends on how complicated the thing actually is.

- If you started over, what would you do differently?

  I would set up my fork properly from the start and commit a lot more often with smaller commits, because I tended to do a bunch of work and then commit it all at once so my history doesn't really show how much I did. I'd also keep up with the log book every week instead of falling behind, and try to stay more involved with what the rest of the team was doing so I understood the whole codebase better and not just my own classes. I'd also plan the thread safety stuff earlier instead of adding parallel streams at the end and then realising it had problems.

- What feedback from the Week 7 submission did you act on?

  The feedback said I linked a branch instead of a fork, so this time I've linked my own fork. It also said there wasn't a lot of detail in the worksheet and that it should be full sentences instead of dot points, so I've tried to explain things properly in paragraphs this time, go through how each part actually works and include code snippets. For the generics and exceptions it said they needed more explanation and justification, so I explained why pickRandom is generic and what problem NoAvailableFlowerException actually solves and how it gets handled. The diagram was said to have inaccuracies and not enough detail, so we made a new diagram for the bee state pattern based on the actual classes, and I've also described each state and how they connect in 1.1 so it's clear how it works. I also kept using pull requests to merge my branch into main since that was mentioned as a positive.
