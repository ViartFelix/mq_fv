package fr.fv.mq_fv.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Display
import org.bukkit.entity.TextDisplay
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
        this.distance = this.getDistance()

    }

    constructor(
        location: Location,
        text: String,
        textColor: NamedTextColor? = null,
        backgroundColor: Color? = null,
        distance: Float? = null,
    ) : this(location, distance, backgroundColor) {

        //black font color by default
        val finalColor = textColor ?: NamedTextColor.BLACK

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
    fun display() {
        val world = this.getWorld()
        val coordinates: Location = this.calculateCoordinates()

        this.display = world.spawn(coordinates, TextDisplay::class.java) { entity ->
            entity.text(this.text)
            entity.billboard = Display.Billboard.VERTICAL
            entity.backgroundColor = this.backgroundColor
            entity.isSeeThrough = true
            entity.alignment = TextDisplay.TextAlignment.CENTER
        }
    }

    /**
     * Calculates the coordinates of the text display
     */
    private fun calculateCoordinates(): Location
    {
        val target = this.location
        val lookAngle = this.location.direction

        //we decide of a random angle (180° angle max to display in front of the player)
        val randomAngleDegrees: Double = Random.nextDouble(0.0, 180.0)
        val randomAngleRadiant: Double = Math.toRadians(randomAngleDegrees)

        //and apply that angle to the distance set
        val adjacentLength = this.calculateXLength(randomAngleRadiant, lookAngle)
        val oppositeLength = this.calculateZLength(randomAngleRadiant, lookAngle)

        //random increment in the final height
        val randomHeightIncrement: Double = Random.nextDouble(0.0, 0.75)
        val finalHeightIncrement = 1.1 + randomHeightIncrement

        target.add(adjacentLength, finalHeightIncrement, oppositeLength)

        return target
    }

    /**
     * Calculates the length on the x-axis from an angle.
     */
    private fun calculateXLength(angle: Double, direction: Vector): Double
    {
        return (direction.x * cos(angle) - direction.z * sin(angle)) * this.distance
    }

    /**
     * Calculates the length on the z-axis from an angle.
     */
    private fun calculateZLength(angle: Double, direction: Vector): Double
    {
        return (direction.x * sin(angle) + direction.z * cos(angle)) * this.distance
    }

    /**
     * Gets the world where the Location object is
     */
    fun getWorld(): World {
        return this.location.world
    }

    /**
     * Destroys the displayed text
     */
    fun destroy()
    {
        this.display.remove()
    }

    /**
     * Returns the set distance
     */
    fun getDistance(): Float
    {
        //if distance has been set in the object
        return if(this.distance !== null) {
            this.distance!!
        } else {
            val configValue = this.configuration.get("measure.distance")

            //if any default value in the .yaml file is present
            if(configValue !== null) {
                configValue.toString().toFloat()
            } else {
                1.2f
            }
        }
    }
}