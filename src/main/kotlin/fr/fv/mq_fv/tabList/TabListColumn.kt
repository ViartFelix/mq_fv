package fr.fv.mq_fv.tabList

class TabListColumn(
    final val index: Int,
    final val maxCapacity: Int = 20
) {
    private val allPlayers: HashMap<Int, SingleTabListPlayer> = HashMap(maxCapacity)


    /**
     * Initialises the column of the tab list
     */
    private fun initColumn()
    {

    }

}