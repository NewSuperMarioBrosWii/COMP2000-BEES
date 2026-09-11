
what are we doing visually?
is the hive more of a house or on a tree

---

we can implement generics with the object picker the uses hitboxes to get a type then display certain information about the object

---

the current worker bee is quite temporary and also flowers currently crash the simulation due to not deleting themselves correctly

---

!!! Please Read
## This is how the system is structured

- all types of bees are downcasted into the Bees Arraylist in the hive
- the hive knows the field and flowers so when accessing a flower from a bee go through the hive
- nothing should really be added to the main App.java script if you want to spawn a bee do it through the hive
	- the hive still needs a function to spawn a random type of bee instead of just 1
- anything that changes overtime should be multiplied by deltatime