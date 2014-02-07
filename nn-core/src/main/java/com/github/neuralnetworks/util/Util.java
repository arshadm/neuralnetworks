package com.github.neuralnetworks.util;

import java.util.Collection;

import com.github.neuralnetworks.architecture.Connections;
import com.github.neuralnetworks.architecture.Conv2DConnection;
import com.github.neuralnetworks.architecture.ConvGridLayer;
import com.github.neuralnetworks.architecture.GraphConnections;
import com.github.neuralnetworks.architecture.Layer;
import com.github.neuralnetworks.architecture.Subsampling2DConnection;

/**
 * Util class
 */
public class Util {

    public static void fillArray(final float[] array, final float value) {
	int len = array.length;
	if (len > 0) {
	    array[0] = value;
	}

	for (int i = 1; i < len; i += i) {
	    System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
	}
    }
    
    public static void fillArray(final int[] array, final int value) {
	int len = array.length;
	if (len > 0) {
	    array[0] = value;
	}
	
	for (int i = 1; i < len; i += i) {
	    System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
	}
    }

    public static Layer getOppositeLayer(Connections connection, Layer layer) {
	return connection.getInputLayer() != layer ? connection.getInputLayer() : connection.getOutputLayer();
    }

    /**
     * @param layer
     * @return whether layer is in fact bias layer
     */
    public static boolean isBias(Layer layer) {
	boolean isConvBias = false;
	if (layer instanceof ConvGridLayer) {
	    ConvGridLayer cl = (ConvGridLayer) layer;
	    isConvBias = cl.isBias();
	}

	boolean isBias = false;
	if (layer.getConnections().size() == 1) {
	    Connections c = layer.getConnections().get(0);
	    if (c.getInputLayer() == layer && c instanceof GraphConnections) {
		GraphConnections cg = (GraphConnections) c;
		isBias = cg.getConnectionGraph().getColumns() == 1;
	    }
	}

	return isConvBias || isBias;
    }

    /**
     * @param layer
     * @return whether layer is in fact subsampling layer (based on the connections)
     */
    public static boolean isSubsampling(Layer layer) {
	if (layer instanceof ConvGridLayer) {
	    Conv2DConnection conv = null;
	    Subsampling2DConnection ss = null;
	    ConvGridLayer l = (ConvGridLayer) layer;
	    for (Connections c : l.getConnections()) {
		if (c instanceof Conv2DConnection) {
		    conv = (Conv2DConnection) c;
		} else if (c instanceof Subsampling2DConnection) {
		    ss = (Subsampling2DConnection) c;
		}
	    }

	    if (ss != null && (ss.getOutputLayer() == layer || conv == null)) {
		return true;
	    }
	}
	
	return false;
    }
    
    /**
     * @param layer
     * @return whether layer is in fact convolutional layer (based on the connections)
     */
    public static boolean isConvolutional(Layer layer) {
	if (layer instanceof ConvGridLayer) {
	    Conv2DConnection conv = null;
	    Subsampling2DConnection ss = null;
	    ConvGridLayer l = (ConvGridLayer) layer;
	    for (Connections c : l.getConnections()) {
		if (c instanceof Conv2DConnection) {
		    conv = (Conv2DConnection) c;
		} else if (c instanceof Subsampling2DConnection) {
		    ss = (Subsampling2DConnection) c;
		}
	    }

	    if (conv != null && (conv.getOutputLayer() == layer || ss == null)) {
		return true;
	    }
	}
	
	return false;
    }

    /**
     * @param connections
     * @return whether there is a bias connection in the list
     */
    public static boolean hasBias(Collection<Connections> connections) {
	for (Connections c : connections) {
	    if (Util.isBias(c.getInputLayer())) {
		return true;
	    }
	}

	return false;
    }
}
