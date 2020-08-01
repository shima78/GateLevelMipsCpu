package components;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

import java.util.Base64;

public class Decoder5x32 extends Wrapper {
    public Decoder5x32(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        Decoder2X4 decoder2X4  = new Decoder2X4("DECODER2TO4" , "2X4");


        Decoder3x8 decoder3x8_1 = new Decoder3x8("Decoder3x8_1" , "3X8");
        Decoder3x8 decoder3x8_2 = new Decoder3x8("Decoder3x8_2" , "3X8");
        Decoder3x8 decoder3x8_3 = new Decoder3x8("Decoder3x8_3" , "3X8");
        Decoder3x8 decoder3x8_4 = new Decoder3x8("Decoder3x8_4" , "3X8");

        decoder2X4.addInput(getInput(0) , getInput(1));

        decoder3x8_1.addInput(decoder2X4.getOutput(0),getInput(2), getInput(3) , getInput(4));
        decoder3x8_1.addInput(decoder2X4.getOutput(1),getInput(2), getInput(3) , getInput(4));
        decoder3x8_1.addInput(decoder2X4.getOutput(2),getInput(2), getInput(3) , getInput(4));
        decoder3x8_1.addInput(decoder2X4.getOutput(3),getInput(2), getInput(3) , getInput(4));

        addOutput(decoder3x8_1.getOutput(0), decoder3x8_1.getOutput(1),decoder3x8_1.getOutput(2),decoder3x8_1.getOutput(3), decoder3x8_1.getOutput(4), decoder3x8_1.getOutput(5),decoder3x8_1.getOutput(6),decoder3x8_1.getOutput(7));
        addOutput(decoder3x8_2.getOutput(0), decoder3x8_2.getOutput(1),decoder3x8_2.getOutput(2),decoder3x8_2.getOutput(3), decoder3x8_2.getOutput(4), decoder3x8_2.getOutput(5),decoder3x8_2.getOutput(6),decoder3x8_2.getOutput(7));
        addOutput(decoder3x8_3.getOutput(0), decoder3x8_3.getOutput(1),decoder3x8_3.getOutput(2),decoder3x8_3.getOutput(3), decoder3x8_3.getOutput(4), decoder3x8_3.getOutput(5),decoder3x8_3.getOutput(6),decoder3x8_3.getOutput(7));
        addOutput(decoder3x8_4.getOutput(0), decoder3x8_4.getOutput(1),decoder3x8_4.getOutput(2),decoder3x8_4.getOutput(3), decoder3x8_4.getOutput(4), decoder3x8_4.getOutput(5),decoder3x8_4.getOutput(6),decoder3x8_4.getOutput(7));

    }
}
