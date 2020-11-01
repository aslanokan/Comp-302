package domain.factories;

import domain.models.alien.*;

public class AlienFactory {
    private static AlienFactory alienFactory;
    private boolean canCollaborate;

    private AlienFactory() {
        canCollaborate = true;
    }

    public static AlienFactory getInstance() {
        if (alienFactory == null) {
            alienFactory = new AlienFactory();
        }
        return alienFactory;
    }

    public Alien createAlien(double x, double y) {
        double rand = Math.random();
        if (canCollaborate) {
            if (rand < 1.0 / 3.0) {
                return new CollaboratingAlien(x, y);
            } else if (rand < 2.0 / 3.0) {
                return new RepairingAlien(x, y);
            } else {
                return new ProtectingAlien(x, y);
            }
        } else {
            if (rand < 1.0 / 2.0) {
                return new RepairingAlien(x, y);
            } else {
                return new ProtectingAlien(x, y);
            }
        }
    }

    public Alien createAlien(String type, double x, double y) {
        if (type.equals("CollaboratingAlien")) {
            return new CollaboratingAlien(x, y);
        } else if (type.equals("RepairingAlien")) {
            return new RepairingAlien(x, y);
        } else if (type.equals("ProtectingAlien")) {
            return new ProtectingAlien(x, y);
        } else if (type.equals("DrunkAlien")) {
            return new DrunkAlien(x, y);
        }
        return null;
    }

    public void setCanCollaborate(boolean bool) {
        canCollaborate = bool;
    }
}
