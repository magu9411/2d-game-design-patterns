package com.ooad.gameengine.components;

/**
 * Interface for querying player combat state without reflection.
 */
public interface IPlayerStateProvider {
    /**
     * @return true if the player is currently attacking
     */
    boolean isAttacking();

    /**
     * @return true if the player took damage recently (invulnerability window)
     */
    boolean tookDamageRecently();
}
