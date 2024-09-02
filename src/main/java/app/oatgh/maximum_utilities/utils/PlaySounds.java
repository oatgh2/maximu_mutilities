package app.oatgh.maximum_utilities.utils;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlaySounds {
    public static void PlayBucketFillSound(Level level, LivingEntity pLivingEntity){
        if(!level.isClientSide()){
            level.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(),
                    SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
        }else{
            pLivingEntity.playSound(SoundEvents.BUCKET_FILL);
        }
    }
    public static void PlayBucketEmptySound(Level level, LivingEntity pLivingEntity){
        if(!level.isClientSide()){
            level.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(),
                    SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
        }else{
            pLivingEntity.playSound(SoundEvents.BUCKET_EMPTY);
        }
    }
}
