package fr.fv.mq_fv

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