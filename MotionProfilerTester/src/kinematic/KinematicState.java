package kinematic;

public class KinematicState {

    public final double length;
    public final double velocity;
    public final double acceleration;

    public KinematicState(double length, double velocity, double acceleration) {
        this.length = length;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    /**
     * Interpolates state0 and state1
     *
     * @param state0 intial state
     * @param p how far the robot has gone
     * @param state1 next state
     * @param q how far it has yet to go
     * @return the interpolated state
     */
    public static KinematicState interpolate(KinematicState state0, double p, KinematicState state1,
        double q) {
        return new KinematicState((p * state0.length) + (q * state1.length), state1.velocity,
            state1.acceleration);
    }

    public final String toString() {
        return String
            .format("KinematicState{length=%f, velocity=%f, acceleration=%f}", length, velocity,
                acceleration);
    }
}
