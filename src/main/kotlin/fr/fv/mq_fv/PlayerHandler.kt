package fr.fv.mq_fv

import fr.fv.mq_fv.utils.Database

/**
 * Class to handle the player and methods
 */
class PlayerHandler {

    //singleton
    companion object {
        val instance : PlayerHandler by lazy {
            PlayerHandler()
        }
    }
}