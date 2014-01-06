package com.github.neuralnetworks.calculation.neuronfunctions;

/**
 * Tanh convolutional calculator
 */
public class AparapiConv2DTanh extends AparapiConv2DFF {

    private static final long serialVersionUID = -7985734201416578973L;

    @Override
    protected float activationFunction(float value) {
	return tan(value);
    }
}
