package track.container;

import track.container.config.Bean;

public class BeanVertex {
    public enum State {DEFAULT, VISITED, MARKED}

    private Bean value;
    private State state = State.DEFAULT;

    public BeanVertex(Bean bean) {
        this.value = bean;
    }

    public Bean getValue() {
        return value;
    }

    public void setValue(Bean bean) {
        this.value = bean;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
