package fr.fv.mq_fv.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Display.Billboard
import org.bukkit.entity.TextDisplay
import org.bukkit.entity.TextDisplay.TextAlignment
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Creates a floating text at specified location at specified distance
 * @param location Coordinates of the text (entity)
 * @param distance Distance (in blocs) from the location on which the text will be displayed
 * @param backgroundColor Background color of the displayed text. Default is light grey (Color.fromARGB(50,255,255,255))
 */
class FloatingText(
    private var location: Location,
    private var distance: Float? = null,
    private var backgroundColor: Color? = null
) {
    /** FloatingText's default values from the yaml file */
    private var configuration: YamlConfiguration = ConfigurationsHolder.instance.getConfig("floating-text")!!

    /** Text content */
    private lateinit var text: Component

    /** The TextDisplay */
    private lateinit var display: TextDisplay

    /** Initialises the properties via custom getters */
    init {
        this.distance ?: this.getDefaultDistance()
        this.backgroundColor ?: this.getDefaultBackgroundColor()
    }

    constructor(
        location: Location,
        text: String,
        textColor: NamedTextColor? = null,
        backgroundColor: Color? = null,
        distance: Float? = null,
    ) : this(location, distance, backgroundColor) {

        val finalColor = textColor ?: this.getDefaultFontColor()

        this.text = Component
            .text(text)
            .color(finalColor)
    }

    constructor(
        location: Location,
        text: Component,
        backgroundColor: Color? = null,
        distance: Float? = null,
    ) : this(location, distance, backgroundColor) {
        this.text = text
    }

    /**
     * Builds the armor stand and displays the text.
     */
    fun display(): FloatingText {
        val world = this.getWorld()
        val coordinates: Location = this.calculateCoordinates()

        this.display = world.spawn(coordinates, TextDisplay::class.java) { entity ->
            entity.text(this.text)
            entity.billboard = this.getDefaultBillboardInclinaison()
            entity.backgroundColor = this.backgroundColor
            entity.isSeeThrough = true
            entity.alignment = this.getDefaultTextAlignement()
        }

        return this
    }

    /**
     * Calculates the coordinates of the text display
     */
    private fun calculateCoordinates(): Location
    {
        val target = this.location
        val lookAngle = this.location.direction

        //we decide of a random angle (180° angle max to display in front of the player)
        val randomAngleDegrees: Double = Random.nextDouble(0.0, this.getMaxAngle())
        val randomAngleRadiant: Double = Math.toRadians(randomAngleDegrees)

        //and apply that angle to the distance set
        val adjacentLength = this.calculateXLength(randomAngleRadiant, lookAngle)
        val oppositeLength = this.calculateZLength(randomAngleRadiant, lookAngle)

        //random increment in the final height
        val randomHeightIncrement: Double = Random.nextDouble(0.0, this.getMaxBonusIncrement())
        val finalHeightIncrement = this.getInitialIncrement() + randomHeightIncrement

        target.add(adjacentLength, finalHeightIncrement, oppositeLength)

        return target
    }

    /**
     * Calculates the length on the x-axis from an angle.
     */
    private fun calculateXLength(angle: Double, direction: Vector): Double
    {
        return (direction.x * cos(angle) - direction.z * sin(angle)) * this.distance!!
    }

    /**
     * Calculates the length on the z-axis from an angle.
     */
    private fun calculateZLength(angle: Double, direction: Vector): Double
    {
        return (direction.x * sin(angle) + direction.z * cos(angle)) * this.distance!!
    }

    /**
     * Destroys the displayed text
     */
    fun destroy()
    {
        this.display.remove()
    }

    /**
     * Gets the world where the Location object is
     */
    fun getWorld(): World {
        return this.location.world
    }

    /**
     * Returns the set distance
     */
    fun getDefaultDistance(): Float
    {
        val configValue = this.configuration.get("measure.distance")

        //if distance has been set in the object
        return if(configValue !== null) {
            configValue.toString().toFloat()
        } else {
            1.2f
        }
    }

    /**
     * Returns the initial bloc increment (default is 1.1)
     */
    fun getInitialIncrement(): Float = (this.configuration.get("measure.initial_increment") ?: 1.1f).toString().toFloat()

    /**
     * Returns the max bonus vertical increment of the text (default is 0.75)
     */
    fun getMaxBonusIncrement(): Double = (this.configuration.get("measure.max_bonus_increment") ?: 0.75f).toString().toDouble()

    /**
     * Returns the maximum angle in which the text can appear (default is 180.0)
     */
    fun getMaxAngle(): Double = (this.configuration.get("measure.angle") ?: 180).toString().toDouble()

    /**
     * Calculates the default argb background color of the floating text.
     */
    private fun getDefaultBackgroundColor(): Color
    {
        //fallback BG from the class itself
        val fallbackBG = Color.fromARGB(50,255,255,255)

        //color in the .yaml file)
        val configColor = this.configuration.getConfigurationSection("colors.background")

        return if (configColor != null) {
            if(!configColor.contains("a")) {
                fallbackBG
            } else {
                //return the colors in the yaml config file

                val alpha = configColor.getInt("a")
                val r = (configColor.get("r") ?: 255).toString().toInt()
                val g = (configColor.get("g") ?: 255).toString().toInt()
                val b = (configColor.get("b") ?: 255).toString().toInt()

                Color.fromARGB(alpha, r, g, b)
            }
        } else {
            fallbackBG
        }
    }

    /**
     * Returns the default named text color for the font of the text
     */
    fun getDefaultFontColor(): NamedTextColor
    {
        val fallbackColor = NamedTextColor.BLACK
        val configTextColor = this.configuration.get("colors.text")

        return if(configTextColor != null) {
            //named color or fallback color
            NamedTextColor.NAMES.value(configTextColor.toString().lowercase()) ?: fallbackColor
        } else {
            fallbackColor
        }
    }

    /**
     * Returns the default Display.Billboard inclinaison for the textDisplay entity
     */
    fun getDefaultBillboardInclinaison(): Billboard
    {
        val fallbackBillboard = Billboard.VERTICAL
        val configInclinaison = this.configuration.get("entity.billboard")

        return if (configInclinaison != null) {
            Billboard.entries.firstOrNull { it.name == configInclinaison } ?: fallbackBillboard
        } else {
            fallbackBillboard
        }
    }

    fun getDefaultTextAlignement(): TextAlignment
    {
        val fallbackAlignement = TextAlignment.CENTER
        val configTextAlignment = this.configuration.get("entity.text_alignement")

        return if(configTextAlignment != null) {
            TextAlignment.entries.firstOrNull { it.name == configTextAlignment } ?: fallbackAlignement
        } else {
            fallbackAlignement
        }
    }
}