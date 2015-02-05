package org.coding.puzzle.common;

import org.junit.Test;

public class AppTest {
    @Test
    public void testOpPrecedence() {
        System.out.println(getOp1() && getOp2() || getOp3() && getOp1());
    }

    boolean getOp1() {
        return false;
    }

    boolean getOp2() {
        return false;
    }

    boolean getOp3() {
        return true;
    }
}
