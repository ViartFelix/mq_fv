package fr.fv.mq_fv.tabList

class TabListHandler {
    var maxColumnsNumber: Int = 4
        private set

    var allColumns: HashMap<Int, TabListColumn> = HashMap(maxColumnsNumber)
        private set

    /**
     * Gets a column from the tab list
     */
    fun getColumn(columnNumber: Int): TabListColumn
    {
        return allColumns[columnNumber]!!
    }

    /**
     * Replace the contents of the column
     */
    fun replaceColumn(columnNumber: Int, tabListColumn: TabListColumn)
    {
        allColumns[columnNumber] = tabListColumn
    }
}