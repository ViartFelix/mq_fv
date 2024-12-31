package fr.fv.mq_fv.utils

import fr.fv.mq_fv.Mq_fv
import org.bukkit.configuration.file.YamlConfiguration
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Holds all configurations of .yaml and .yml files (Singleton)
 */
class ConfigurationsHolder
private constructor()
{
    /** All configurations */
    private var configurations: HashMap<String, YamlConfiguration> = HashMap<String, YamlConfiguration>()

    companion object {
        val instance: ConfigurationsHolder by lazy {
            ConfigurationsHolder()
        }
    }

    /**
     * Returns a configuration if exists
     */
    fun getConfig(name: String): YamlConfiguration?
    {
        if(! this.configurations.containsKey(name) ) {
            throw RuntimeException("The configuration $name does not exist.")
        }

        return this.configurations[name]
    }

    /**
     * Sets a configuration to a key
     */
    fun setConfig(name: String, config: YamlConfiguration): ConfigurationsHolder
    {
        this.configurations[name] = config

        return this
    }

    /**
     * Deletes a config via it's name and returns it's last assigned value
     */
    fun deleteConfig(name: String): YamlConfiguration?
    {
        return this.configurations.remove(name)
    }

    /**
     * Checks if the config name exists or not
     */
    fun hasConfig(name: String): Boolean
    {
        return this.configurations.containsKey(name)
    }

    fun loadFromMap(files: HashMap<String, String>): ConfigurationsHolder
    {
        files.forEach { (name, path) ->
            this.loadSingleFile(name, path)
        }

        return this
    }

    /**
     * Loads a single ressource file
     */
    fun loadSingleFile(name: String, ressourcePath: String): ConfigurationsHolder
    {
        val fileRessource = Mq_fv.instance.getResource(ressourcePath)

        if(fileRessource === null) {
            throw FileNotFoundException("The file at path $ressourcePath does not exist or is not readable.")
        }

        //load yaml config via an inputStream
        val finalConfiguration = YamlConfiguration.loadConfiguration(
            InputStreamReader(fileRessource, StandardCharsets.UTF_8)
        )

        //set the configuration
        this.configurations[name] = finalConfiguration

        return this
    }
}