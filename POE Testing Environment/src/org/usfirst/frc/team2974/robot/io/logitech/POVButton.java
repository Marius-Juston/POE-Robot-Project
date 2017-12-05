package org.usfirst.frc.team2974.robot.io.logitech;

public enum POVButton {
    N(0), S(180), E(90), W(270), NE(45), SE(135), NW(315), SW(225), CENTER(0);

    private final int angle;

    POVButton(int angle) {
        this.angle = angle;
    }

    boolean getPressed(Gamepad g) {
        return g.getPOV() == angle;
    }

    @Override
    public String toString() {
        return String.format(
            "POVButton{angle=%d}"
            , angle
        );
    }
}
