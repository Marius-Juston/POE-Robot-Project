package org.usfirst.frc.team2974.robot.smartdashboard;

import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;

public final class SmartDashboardManager {

    private static final edu.wpi.first.networktables.NetworkTable TABLE;

    // Properties list where all the SmartDashboard  Properties are stored
    private static final List<SmartDashboardProperty> PROPERTIES;

    static {
        TABLE = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        PROPERTIES = new ArrayList<>(10);

        TABLE.getKeys().forEach(TABLE::delete);
    }

    private SmartDashboardManager() {
    }

    /**
     * Creates a static bind where Supplier is null.
     *
     * @see #addBind(String, Object, Supplier)
     */
    public static <T> SmartDashboardProperty<T> addBind(String key, T value) {
        return addBind(key, value, null);
    }

    /**
     * <p>Creates a SmartDashboard Property that will update automatically when the update method of
     * SmartDashboardManager is called.</p>
     *
     * <p>
     * Example: <pre>{@code
     *   addBind("Left Motor Power", 0, () -> SubsystemManager.getSubsystem(DriveTrain.class).getLeftMotorPower());
     *   }</pre>
     * </p>
     *
     * If the supplier is null the property is effectively static, unless another supplier is added later.
     *
     * @param key SmartDashboard key
     * @param defaultValue Default value that SmartDashboard will returns if it cannot find the value
     * @param valueSupplier Supplier used to get the updating value
     * @param <T> the data type you want SmartDashboard to display (most of the time you don't need to worry about it)
     * @return The SmartDashboard property created
     */
    public static <T> SmartDashboardProperty<T> addBind(String key, T defaultValue,
        Supplier<T> valueSupplier) {
        if (PROPERTIES.stream().anyMatch(p -> p.getKey().equals(key))) {
            throw new RobotRuntimeException(
                "Cannot have duplicate keys for SmartDashboard. Key in question is " + key);
        }

        SmartDashboardProperty<T> prop = new SmartDashboardProperty<>(key, defaultValue,
            valueSupplier);

        PROPERTIES.add(prop);

        return prop;
    }

    /**
     * Creates a static debug bind where Supplier is null.
     *
     * @see #addBind(String, Object, Supplier)
     */
    public static <T> DebugSmartDashboardProperty<T> addDebug(String key, T value) {
        return addDebug(key, value, null);
    }

    /**
     * Creates a debug bind which will update if IS_DEBUG is true.
     *
     * @see #addBind(String, Object, Supplier) its basically the same
     */
    public static <T> DebugSmartDashboardProperty<T> addDebug(String key, T defaultValue,
        Supplier<T> valueSupplier) {
        DebugSmartDashboardProperty<T> prop = new DebugSmartDashboardProperty<>(key, defaultValue,
            valueSupplier);

        PROPERTIES.add(prop);

        return prop;
    }

    /**
     * Removes the bind with key key from SmartDashboard and the smartdashboard.
     *
     * @param key SmartDashboard key
     */
    public static void removeBind(String key) {
        for (int i = 0; i < PROPERTIES.size(); i++) {
            if (PROPERTIES.get(i).getKey().equals(key)) {
                PROPERTIES.remove(i--);
                TABLE.getEntry(key).delete();
            }
        }
    }

    /**
     * @param key the key to check
     * @return <b>true</b> if PROPERTIES contains a property with key key, <b>false</b> otherwise.
     */
    public static boolean containsBind(String key) {
        for (SmartDashboardProperty p : PROPERTIES) {
            if (p.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the list of SmartDashboard Properties
     *
     * @return returns the PROPERTIES list
     */
    public static List<SmartDashboardProperty> getProperties() {
        return Collections.unmodifiableList(SmartDashboardManager.PROPERTIES);
    }

    /**
     * Retrieves the wanted property given it key.
     *
     * @param <T> the data type of value being updated to SmartDashboard
     * @param key the key of the property
     * @return the SmartDashboard property with the specified key if it is found
     */
    @SuppressWarnings("unchecked")
    public static <T> SmartDashboardProperty<T> getProperty(String key) {
        Optional<SmartDashboardProperty> smartDashboardProperty = SmartDashboardManager.PROPERTIES
            .stream()
            .filter(p -> p.getKey()
                .equals(key)) // gts the properties with the same key as the one searching for
            .findFirst();  /// gets the first SmartDashboard property

        if (smartDashboardProperty.isPresent()) { // if there is a SmartDashboard property
            return smartDashboardProperty.get(); // returns the SmartDashboard property
        }

        throw new RobotRuntimeException("Property " + key
            + " does not exist. Did you forget to add it?"); // if it did not find the SmartDashboard property throw error
    }

    /**
     * Iterates through the PROPERTIES list and calls the update method for each individual property.
     * The update method in the property uses its supplier to get the latest value and if the value
     * changes it will update the value by doing whatever the Runnable object onValueChange tells it
     * to do
     */
    public static void update() {
        PROPERTIES.forEach(SmartDashboardProperty::update);
    }
}
