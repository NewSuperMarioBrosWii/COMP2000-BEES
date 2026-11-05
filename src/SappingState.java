public class SappingState implements BeeState {

    @Override
    public void update(BeeWorker bee, float deltaTime) {
        Flower flower = bee.Target;

        if (flower != null && !bee.isNectorFull() && !flower.empty) {
            float amount = bee.takeRate * deltaTime;
            flower.Pollen -= amount;
            bee.addNector(amount);
        } else {
            bee.changeState(new StashingState());
        }

        if (bee.beingAttacked) {
            bee.InHive = false;
            bee.changeState(new AttackState());
        }
    }

    @Override 
    public void exit(BeeWorker bee){
        bee.Target.occupied = false;
        bee.Target = null;
    }
}
