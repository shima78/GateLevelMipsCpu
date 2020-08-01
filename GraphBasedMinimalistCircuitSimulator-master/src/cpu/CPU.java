package cpu;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class CPU extends Wrapper {
    public CPU(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

        ControlUnit controlUnit = new ControlUnit("cu","6x10");
        ALUControl aluControl = new ALUControl("aluC","6x3");

        ALU alu = new ALU("alu","67x33");

    }
}
