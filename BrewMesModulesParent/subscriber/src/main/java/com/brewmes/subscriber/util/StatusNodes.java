package com.brewmes.subscriber.util;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public enum StatusNodes {
    MACHINE_STATE(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.StateCurrent")),
    MACHINE_SPEED(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.MachSpeed")),
    NORMALIZED_MACHINE_SPEED(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.CurMachSpeed")),
    CURRENT_BATCH_ID(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.Parameter[0].Value")),
    PRODUCTS_IN_CURRENT_BATCH(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.Parameter[1].Value")),
    HUMIDITY(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.Parameter[2].Value")),
    TEMPERATURE(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.Parameter[3].Value")),
    VIBRATION(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Status.Parameter[4].Value"));

    public final NodeId nodeId;

    StatusNodes(NodeId nodeId) {
        this.nodeId = nodeId;
    }
}
