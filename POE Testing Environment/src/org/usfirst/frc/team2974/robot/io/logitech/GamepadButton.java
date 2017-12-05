package org.usfirst.frc.team2974.robot.io.logitech;

/**
 * This enum is used to map what the button number is mapped to what on the gamepad all the while
 * giving them meaningful names instead of numbers. Note: This is not for a XBox controller but for
 * a Logitech Dual Action Controller
 */
public enum GamepadButton {
    _1(1), _2(2), _3(3), _4(4), _5(5), _6(6), _7(7), _8(8), _9(9), _10(10), LEFT_THUMB_STICK(
        11), RIGHT_THUMB_STICK(12);
    //FIXME Add rest of the buttons and verify that these are correct

    private final int index;

    /**
     * Initializes the variables
     *
     * @param index index of the button as it depicted on the driver station
     */
    GamepadButton(int index) {
        this.index = index;
    }

    /**
     * Returns the index of the button.
     *
     * @return returns the index of the button
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param g the gamepad to check
     * @return <b>true</b> if the button is pressed, <b>false</b> otherwise
     */
    public boolean isPressed(Gamepad g) {
        return g.getRawButton(index);
    }

    @Override
    public String toString() {
        return String.format(
            "GamepadButton{index=%d}"
            , index
        );
    }

}




