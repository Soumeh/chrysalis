package net.sydokiddo.chrysalis.misc.util.entities;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.FlyingAnimal;

@SuppressWarnings("unused")
public class HybridWalkingFlyingMoveControl extends MoveControl {

    /**
     * This move control allows for mobs that integrate it to use both flying and walking pathfinding types
     **/

    public final Mob mob;
    public final int maxTurn;
    public final boolean hoversInPlace;

    public HybridWalkingFlyingMoveControl(Mob mob, int maxTurn, boolean noGravity) {
        super(mob);
        this.mob = mob;
        this.maxTurn = maxTurn;
        this.hoversInPlace = noGravity;
    }

    @Override
    public void tick() {
        if (this.mob instanceof FlyingAnimal flyingAnimal && flyingAnimal.isFlying()) {
            if (this.operation == Operation.MOVE_TO) {

                this.operation = Operation.WAIT;
                this.mob.setNoGravity(true);

                double x = this.wantedX - this.mob.getX();
                double y = this.wantedY - this.mob.getY();
                double z = this.wantedZ - this.mob.getZ();

                double g = x * x + y * y + z * z;

                if (g < 2.500000277905201E-7D) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                float h = (float) (Mth.atan2(z, x) * 57.2957763671875D) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), h, 90.0F));
                float i;

                if (this.mob.onGround()) i = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                else i = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));

                this.mob.setSpeed(i);
                double j = Math.sqrt(x * x + z * z);

                if (Math.abs(y) > 9.999999747378752E-6D || Math.abs(j) > 9.999999747378752E-6D) {
                    float k = (float) (-(Mth.atan2(y, j) * 57.2957763671875D));
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), k, (float) this.maxTurn));
                    this.mob.setYya(y > 0.0D ? i : -i);
                }

            } else {
                if (!this.hoversInPlace) this.mob.setNoGravity(false);
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }
        } else {
            this.mob.setNoGravity(false);
            super.tick();
        }
    }
}