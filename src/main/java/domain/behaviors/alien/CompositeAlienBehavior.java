package domain.behaviors.alien;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeAlienBehavior implements AlienBehavior {

    List<AlienBehavior> behaviorList = new ArrayList<AlienBehavior>();

    public void addBehaviour(AlienBehavior ab) {
        behaviorList.add(ab);
    }

    public List<AlienBehavior> getAlienBehaviorList() {
        return behaviorList;
    }

    public abstract void behave();
}
