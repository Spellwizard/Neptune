/**
 * CoolDownBar was built 4/22/2021
 *
 * The purpose of this function is to simply code, reduce redoing the wheel and
 *      provide a obvious spot to add functions instead of writing them into the player class
 *
 */
public class CoolDownBar {

    /**
     * max_Level describes and is the set number for the maximum value the current_Position number should reaches
     */
    private int max_Level;

    /**
     * current_Position is used as the number for the current value of the bar
     */
    private double current_Position;

    /**
     * increment_HeatUp is used as the increment to use to increase the bar value when in use
     */
    private double increment_HeatUp;

    /**
     * increment_CoolDown is used as the increment to use to decrease or 'cool' the bar value when allowed to cool
     */
    private double increment_CoolDown;

    /**
     * in_Use the value tracking if the bar is currently in use or not.
     */
    private boolean in_Use;


    /**COOLDOWNBAR
     *
     * @param max_Level describes and is the set number for the maximum value the current_Position number should reaches
     * @param current_Position current_Position is used as the number for the current value of the bar
     * @param increment_HeatUp increment_HeatUp is used as the increment to use to increase the bar value when in use
     * @param increment_CoolDown increment_CoolDown is used as the increment to use to decrease or 'cool' the bar value when allowed to cool
     * @param in_Use in_Use the value tracking if the bar is currently in use or not.
     */
    public CoolDownBar(int max_Level, double current_Position,
                       double increment_HeatUp, double increment_CoolDown
            , boolean in_Use) {
        this.max_Level = max_Level;
        this.current_Position = current_Position;
        this.increment_HeatUp = increment_HeatUp;
        this.increment_CoolDown = increment_CoolDown;
        this.in_Use = in_Use;
    }

    public void safetycheckCurrentValue(){
        if(current_Position>max_Level){
            current_Position = max_Level;
        }
        else if(
                current_Position<0
        )
            current_Position =0;
    }

    public void heatByOneIncrement(){
       current_Position+=increment_HeatUp;
       safetycheckCurrentValue();
    }

    public void coolByOneIncrement(){
        current_Position-=increment_CoolDown;
        safetycheckCurrentValue();
    }

    public int getMax_Level() {
        return max_Level;
    }

    public void setMax_Level(int max_Level) {
        this.max_Level = max_Level;
    }

    public double getCurrent_Position() {
        return current_Position;
    }

    public void setCurrent_Position(double current_Position) {
        this.current_Position = current_Position;
    }

    public double getIncrement_HeatUp() {
        return increment_HeatUp;
    }

    public void setIncrement_HeatUp(double increment_HeatUp) {
        this.increment_HeatUp = increment_HeatUp;
    }

    public double getIncrement_CoolDown() {
        return increment_CoolDown;
    }

    public void setIncrement_CoolDown(double increment_CoolDown) {
        this.increment_CoolDown = increment_CoolDown;
    }

    public boolean isIn_Use() {
        return in_Use;
    }

    public void setIn_Use(boolean in_Use) {
        this.in_Use = in_Use;
    }
}
