package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class GlowingSuspendedTownParticle extends FadingEmissiveParticle {

    // region Initialization and Ticking

    public GlowingSuspendedTownParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 1.0F, 0.0F, spriteSet);
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
        this.lifetime = (int) (20.0 / (Math.random() * 0.8 + 0.2));
        this.xd *= 0.02;
        this.yd *= 0.02;
        this.zd *= 0.02;
        float random = this.random.nextFloat() * 0.1F + 0.2F;
        this.rCol = random;
        this.gCol = random;
        this.bCol = random;
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.99;
            this.yd *= 0.99;
            this.zd *= 0.99;
        }
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    // endregion

    // region Providers

    @Environment(EnvType.CLIENT)
    public static class GlowingParticleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public GlowingParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            GlowingSuspendedTownParticle particle = new GlowingSuspendedTownParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, this.spriteSet);
            particle.pickSprite(this.spriteSet);
            particle.setColor(1.0F, 1.0F, 1.0F);
            return particle;
        }
    }

    // endregion
}