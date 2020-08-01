package components;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Mux32To1 extends Wrapper {
    public Mux32To1(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

        /*
        * S0 , S1 , S2 , S3 , S4 are selectors inout indexes from 32 to 36
        * the rest are from 0 to 31
        * */
        Mux4To1 mux4To1_zero = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_one = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_two = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_three = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_four = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_five = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_six = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_seven = new Mux4To1("MUX4TO1" , "6X1");


        Mux4To1 mux4To1_eight = new Mux4To1("MUX4TO1" , "6X1");
        Mux4To1 mux4To1_nine = new Mux4To1("MUX4TO1" , "6X1");


        Mux4To1 mux4To1_ten = new Mux4To1("MUX4TO1" , "6X1");

//        S0 and S1 are selectors for muc4:1 zero to seven
        mux4To1_zero.addInput(getInput(32) ,getInput(33), getInput(0) , getInput(1) , getInput(2) ,getInput(3));
        mux4To1_one.addInput(getInput(32) ,getInput(33), getInput(4) , getInput(5) , getInput(6) ,getInput(7));
        mux4To1_two.addInput(getInput(32) ,getInput(33), getInput(8) , getInput(9) , getInput(10) ,getInput(11));
        mux4To1_three.addInput(getInput(32) ,getInput(33), getInput(12) , getInput(13) , getInput(14) ,getInput(15));
        mux4To1_four.addInput(getInput(32) ,getInput(33), getInput(16) , getInput(17) , getInput(18) ,getInput(19));
        mux4To1_five.addInput(getInput(32) ,getInput(33), getInput(20) , getInput(21) , getInput(22) ,getInput(23));
        mux4To1_six.addInput(getInput(32) ,getInput(33), getInput(24) , getInput(25) , getInput(26) ,getInput(27));
        mux4To1_seven.addInput(getInput(32) ,getInput(33), getInput(28) , getInput(29) , getInput(30) ,getInput(31));


        mux4To1_eight.addInput(getInput(34),getInput(35),mux4To1_zero.getOutput(0),mux4To1_one.getOutput(0),mux4To1_two.getOutput(0),mux4To1_three.getOutput(0));
        mux4To1_nine.addInput(getInput(34),getInput(35),mux4To1_four.getOutput(0),mux4To1_five.getOutput(0),mux4To1_six.getOutput(0),mux4To1_seven.getOutput(0));

        mux4To1_ten.addInput(getInput(36), Simulator.falseLogic, mux4To1_eight.getOutput(0),mux4To1_nine.getOutput(0), Simulator.falseLogic, Simulator.falseLogic);

        addOutput(mux4To1_ten.getOutput(0));
    }
}
