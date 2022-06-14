package com.example.pong;

public class GameFrameData {
    float computerPlayerBoundsTop;
    float computerPlayerPaddleHeight;
    float  ballCy;
    float computerPlayerOutputPosition;

    public GameFrameData(float computerPlayerBoundsTop, float computerPlayerPaddleHeight, float ballCy, float computerPlayerOutputPosition) {
        this.computerPlayerBoundsTop = computerPlayerBoundsTop;
        this.computerPlayerPaddleHeight = computerPlayerPaddleHeight;
        this.ballCy = ballCy;
        this.computerPlayerOutputPosition = computerPlayerOutputPosition;
    }

    public float getComputerPlayerBoundsTop() {
        return computerPlayerBoundsTop;
    }

    public void setComputerPlayerBoundsTop(float computerPlayerBoundsTop) {
        this.computerPlayerBoundsTop = computerPlayerBoundsTop;
    }

    public float getComputerPlayerPaddleHeight() {
        return computerPlayerPaddleHeight;
    }

    public void setComputerPlayerPaddleHeight(float computerPlayerPaddleHeight) {
        this.computerPlayerPaddleHeight = computerPlayerPaddleHeight;
    }

    public float getBallCy() {
        return ballCy;
    }

    public void setBallCy(float ballCy) {
        this.ballCy = ballCy;
    }

    public float getComputerPlayerOutputPosition() {
        return computerPlayerOutputPosition;
    }

    public void setComputerPlayerOutputPosition(float computerPlayerOutputPosition) {
        this.computerPlayerOutputPosition = computerPlayerOutputPosition;
    }

    @Override
    public String toString() {
        return "GameFrameData{" +
                "computerPlayerBoundsTop=" + computerPlayerBoundsTop +
                ", computerPlayerPaddleHeight=" + computerPlayerPaddleHeight +
                ", ballCy=" + ballCy +
                ", computerPlayerOutputPosition=" + computerPlayerOutputPosition +
                '}';
    }
}
