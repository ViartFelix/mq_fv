package fr.fv.mq_fv.utils

import fr.fv.mq_fv.exceptions.DatabaseException
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.ktorm.database.Database

/**
 * Database wrapper to communicate with the database.
 * Is a singleton.
 */
class DatabaseWrapper
private constructor() {

    private val configuration : YamlConfiguration = ConfigurationsHolder.instance.getConfig("database")!!

    /** "Url" of the DB */
    var jdbc: String

    /** DB user */
    lateinit var user: String

    /** DB host */
    lateinit var host: String

    /** DB Port. Default is 5432 (postgres) */
    var port: Int = 5432

    /** DB user's password */
    lateinit var password: String

    /** DB name */
    lateinit var dbName: String

    /** DB type (recommended: "postgresql") */
    lateinit var type: String

    //singleton
    companion object {
        /** Singleton instance */
        val instance : DatabaseWrapper by lazy {
            DatabaseWrapper()
        }

        /**
         * Returns the connection via the instance
         */
        fun getDatabase(): Database
        {
            //set the database instance
            if(this.instance !is DatabaseWrapper) {
                throw DatabaseException("DatabaseWrapper is not instantiated.")
            }

            if(this.instance.connection !is Database) {
                throw DatabaseException("Database Ktorm connection is not instantiated.")
            }

            return this.instance.connection
        }
    }

    /** Current DB Ktorm connection */
    lateinit var connection: Database
        private set

    init {
        this.verifyYaml()
        this.setCredentialsFromYaml()

        this.jdbc = this.buildJdbcString()

        this.initKtormConnection()

        this.testConnection()
    }

    /**
     * Checks if the yaml config file contains all the necessary values for a db connection
     */
    fun verifyYaml()
    {
        val dbCredentialsYaml = this.getConfigurationCredentials()

        //check if the necessary properties are in the configuration
        val keysToCheck = arrayOf("host", "user", "password", "db_name", "type")

        keysToCheck.forEach { key ->
            if(! this.isYamlDbEntryValid(dbCredentialsYaml, key) ) {
                throw DatabaseException("The credential '$key' in config does not exist or is not is incorrect.")
            }
        }
    }

    /**
     * Test if the database is contactable.
     */
    fun testConnection()
    {
        try {
            val dbErrorPrefix = "Database connection test failed"

            this.connection.useConnection { connection ->
                //connection test addition
                val calcOne = 50
                val calcTwo = 99
                val expectedResult = 149

                val resultSet = connection
                    .createStatement()
                    .executeQuery("SELECT ${calcOne}+${calcTwo} AS test_result")

                if (resultSet.next()) {
                    val actualResult = resultSet.getInt("test_result")

                    if(expectedResult != actualResult) {
                        throw DatabaseException("${dbErrorPrefix}: expected result '${expectedResult}', got '${actualResult}' from the DB.")
                    }
                } else {
                    throw DatabaseException("${dbErrorPrefix}: no result from test query.")
                }
            }
        } catch (e: DatabaseException) {
            e.printStackTrace()
            throw e
        } catch (e: Exception) {
            e.printStackTrace()

            throw DatabaseException("Cannot connect to database. Reason: ${e}")
        }
    }

    /**
     * Tests if the new connection is valid and then updates the Ktorm connection
     */
    fun updateConnection()
    {
        this.jdbc = this.buildJdbcString()

        this.testConnection()
        this.initKtormConnection()
    }

    /**
     * Initiates the Ktorm connection.
     */
    private fun initKtormConnection()
    {
        this.connection = Database.connect(this.jdbc, user=this.user, password=this.password)
    }

    /**
     * Sets the credentials from the yaml config file
     */
    private fun setCredentialsFromYaml()
    {
        val dbCredentialsYaml = this.getConfigurationCredentials()

        //postgres' default port
        this.port = (dbCredentialsYaml.get("port") ?: 5432) as Int

        this.host = dbCredentialsYaml.getString("host")!!
        this.user = dbCredentialsYaml.getString("user")!!
        this.password = dbCredentialsYaml.getString("password")!!
        this.dbName = dbCredentialsYaml.getString("db_name")!!

        this.type = dbCredentialsYaml.getString("type")!!
    }

    /** Builds the jdbc string based on the class' values */
    private fun buildJdbcString(): String = "jdbc:${type}://${host}:${port}/${dbName}"

    /** Checks if the yaml entry is valid or not */
    private fun isYamlDbEntryValid(yaml: ConfigurationSection, entry: String): Boolean = (yaml.get(entry) != null)

    /** Returns the yaml configuration or throw an error */
    private fun getConfigurationCredentials(): ConfigurationSection = this.configuration.getConfigurationSection("connection")
        ?: throw DatabaseException("No credentials available for connecting to the DB.")

}