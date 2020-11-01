package domain.managers;

import domain.Constants;
import domain.Constants.PaddleDirection;
import domain.Constants.PaddleRotation;
import domain.Time;
import domain.models.Paddle;

import static domain.Constants.PaddleRotation.*;

public class PaddleManager {
    private PaddleRotation paddleRotationState = PaddleRotation.IDLE_ROTATION;
    private PaddleDirection paddleDirection = PaddleDirection.NONE;

    public PaddleManager() {
    }

    public void setPaddleDirection(PaddleDirection paddleDirection) {
        this.paddleDirection = paddleDirection;
    }

    private void undoPaddleRotationStep(Paddle paddle) {
        if (paddle.getCurrentRotationAngle() != 0) {
            double undoRate = Constants.paddleUndoRotationAngle;
            // thread runs for 30 ms. undoRate is per second
            double undoAnglePerThread = undoRate * Time.getInstance().deltaTime() * 1e-3;
            double newAngle = 0;
            if (paddle.getCurrentRotationAngle() > 0) {
                newAngle = paddle.getCurrentRotationAngle() - undoAnglePerThread;
                if (newAngle < 0) {
                    newAngle = 0;
                    paddleRotationState = IDLE_ROTATION;
                }
            } else {
                newAngle = paddle.getCurrentRotationAngle() + undoAnglePerThread;
                if (newAngle > 0) {
                    newAngle = 0;
                    paddleRotationState = IDLE_ROTATION;
                }
            }
            paddle.changeRotation(newAngle);
        }
    }

    public void updatePaddleRotation(Paddle paddle) {
        if (paddleRotationState.equals(POSITIVE_ROTATION)
                || paddleRotationState.equals(NEGATIVE_ROTATION)) {
            rotatePaddleStep(paddleRotationState, paddle);
        } else if (paddleRotationState.equals(UNDOING_NEGATIVE_ROTATION)
                || paddleRotationState.equals(UNDOING_POSITIVE_ROTATION)) {
            undoPaddleRotationStep(paddle);
        }
    }

    public void rotatePaddle(PaddleRotation rotation) {
        if (paddleRotationState.equals(IDLE_ROTATION)) {
            paddleRotationState = rotation;
        }

        if (paddleRotationState.equals(UNDOING_POSITIVE_ROTATION)) {
            if (rotation.equals(POSITIVE_ROTATION)) {
                paddleRotationState = rotation;
            }
        }

        if (paddleRotationState.equals(UNDOING_NEGATIVE_ROTATION)) {
            if (rotation.equals(NEGATIVE_ROTATION)) {
                paddleRotationState = rotation;
            }
        }
    }

    public void stopRotatePaddle(PaddleRotation rotation) {
        if (paddleRotationState.equals(POSITIVE_ROTATION)) {
            if (rotation.equals(POSITIVE_ROTATION)) {
                paddleRotationState = UNDOING_POSITIVE_ROTATION;
            }
        } else if (paddleRotationState.equals(NEGATIVE_ROTATION)) {
            if (rotation.equals(NEGATIVE_ROTATION)) {
                paddleRotationState = UNDOING_NEGATIVE_ROTATION;
            }
        }
    }

    private void rotatePaddleStep(PaddleRotation rotation, Paddle paddle) {
        double currentAngle = paddle.getCurrentRotationAngle();
        double changeRate = Constants.paddleRotationChangeRate * Time.getInstance().deltaTime() * 1e-3;
        if (rotation.equals(POSITIVE_ROTATION)
                && currentAngle < Constants.paddleMaxPositiveRotationAngle) {

            paddle.changeRotation(paddle.getCurrentRotationAngle() + changeRate);
        } else if (rotation.equals(NEGATIVE_ROTATION)
                && currentAngle > Constants.paddleMaxNegativeRotationAngle) {

            paddle.changeRotation(paddle.getCurrentRotationAngle() - changeRate);
        }
    }

    public void updatePaddleMovement(Paddle paddle) {
        Time time = Time.getInstance();
        double yLocation = paddle.getY() + paddle.getVelocityY() * time.deltaTime() * 1e-3;
        if (paddleDirection.equals(PaddleDirection.EAST)) {
            paddle.setVelocityX(paddle.getSpeed());

            double x = paddle.getX() + paddle.getVelocityX() * time.deltaTime() * 1e-3;
            paddle.changeLocation(x, yLocation);
        } else if (paddleDirection.equals(PaddleDirection.WEST)) {
            paddle.setVelocityX(-paddle.getSpeed());

            double x = paddle.getX() + paddle.getVelocityX() * time.deltaTime() * 1e-3;
            paddle.changeLocation(x, yLocation);
        }
    }

    public void stopMovePaddle(PaddleDirection direction) {
        if (direction.equals(paddleDirection)) {
            paddleDirection = Constants.PaddleDirection.NONE;
        }
    }
}
