package cpu;

import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Mux2To1;

public class Mux8To1 extends Wrapper {
    public Mux8To1(String label, String stream, Link... links) {
        super(label, stream, links);
    }
    // 0/1/2/3/4/5/6/7/8s2/9s1/10s0
    @Override
    public void initialize() {
        Mux2To1 m0 = new Mux2To1("m0","3x1");
        Mux2To1 m1 = new Mux2To1("m1","3x1");
        Mux2To1 m2 = new Mux2To1("m2","3x1");
        Mux2To1 m3 = new Mux2To1("m3","3x1");
        Mux2To1 m4 = new Mux2To1("m4","3x1");
        Mux2To1 m5 = new Mux2To1("m5","3x1");
        Mux2To1 m6 = new Mux2To1("m6","3x1");

        m0.addInput(getInput(0),getInput(1),getInput(10));
        m1.addInput(getInput(2),getInput(3),getInput(10));
        m2.addInput(getInput(4),getInput(5),getInput(10));
        m3.addInput(getInput(6),getInput(7),getInput(10));

        m4.addInput(m0.getOutput(0),m1.getOutput(0),getInput(9));
        m5.addInput(m2.getOutput(0),m3.getOutput(0),getInput(9));

        m6.addInput(m4.getOutput(0),m5.getOutput(0),getInput(8));
        addOutput(m6.getOutput(0));

    }

}
