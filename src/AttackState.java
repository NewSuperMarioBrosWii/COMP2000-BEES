public class AttackState implements BeeState {
    private EnemyWasp predator;

    @Override
    public void update(BeeWorker bee, float deltaTime) {
        // bee died
        if (!bee.isAlive || bee.Health <= 0) {
            bee.releaseFlower();
            return;
        }

        // find the wasp that is targeting us
        if (predator == null) {
            predator = Field.getWasps().stream()
                    .filter(w -> w.target == bee)
                    .findFirst()
                    .orElse(null);
        }

        // wasp died -> stop fighting
        if (predator != null && (!predator.isAlive || predator.health <= 0)) {
            predator.isAlive = false;
            bee.beingAttacked = false;
            bee.changeState(new RestingState());
            return;
        }

        // sting the wasp
        if (predator != null) {
            bee.stingTimer += deltaTime;
            if (bee.stingTimer >= bee.stingInterval) {
                predator.health -= 20f;
                bee.stingTimer = 0f;
            }
        }
    }
}
