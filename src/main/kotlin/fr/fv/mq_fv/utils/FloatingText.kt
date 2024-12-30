package fr.fv.mq_fv.utils

import io.papermc.paper.command.brigadier.argument.ArgumentTypes.world
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Display
import org.bukkit.entity.TextDisplay
import kotlin.math.PI
import kotlin.random.Random


/**
 * Creates a floating text at specified location at specified distance
 */
class FloatingText(
    text: String,
    location: Location,
    distance: Float = 1.2f
) {
    /** Coordinates of the text (entity) */
    var location: Location = location

    /** Distance (in blocs) from the location on which the text will be displayed */
    var distance: Float = distance

    /** Text */
    var text: String = text

    /** The armor stand where the text will be displayed */
    private lateinit var display: TextDisplay

    /**
     * Builds the armor stand and displays the text.
     */
    public fun display() {
        val world = this.getWorld()
        val coordinates: Location = this.calculateCoordinates()

        this.display = world.spawn(coordinates, TextDisplay::class.java) { entity ->
            // customize the entity!
            entity.text(Component.text("Some awesome content", NamedTextColor.BLACK))
            entity.billboard = Display.Billboard.VERTICAL // pivot only around the vertical axis
            entity.backgroundColor = Color.RED // make the background red
        }
    }

    /**
     * Calculates the coordinates of the text display
     */
    private fun calculateCoordinates(): Location
    {
        val target = this.location;

        //random increment in the final height
        val randomHeightIncrement: Double = Random.nextDouble(0.0, 0.75)
        val finalHeightIncrement = 1.1 + randomHeightIncrement

        //we decide a random angle
        val randomAngleDegrees: Double = Random.nextDouble(0.0, 360.0);
        val randomAngleRadiant: Double = Math.toRadians(randomAngleDegrees)

        //and apply that angle to the distance set


        //first, we have to level the text display to be a eye-level of the player
        target.add(0.0, finalHeightIncrement, 0.0);

        return target;
    }

    /**
     * Gets the world where the Location object is
     */
    fun getWorld(): World {
        return this.location.world
    }
}