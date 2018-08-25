package track.container.beans;

import java.util.Objects;

/**
 * Коробка передач, поле - количество скоростей
 */
public class Gear {
    private int count;
    private boolean count1;
    private char count2;

    public Gear() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Gear{" +
                "count=" + count +
                ", count1=" + count1 +
                ", count2=" + count2 +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Gear gear = (Gear) obj;
        return count == gear.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }

    public boolean isCount1() {
        return count1;
    }

    public void setCount1(boolean count1) {
        this.count1 = count1;
    }

    public char getCount2() {
        return count2;
    }

    public void setCount2(char count2) {
        this.count2 = count2;
    }
}
