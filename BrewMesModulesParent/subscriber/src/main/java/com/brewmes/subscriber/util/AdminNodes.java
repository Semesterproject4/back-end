package com.brewmes.subscriber.util;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public enum AdminNodes {
    PRODUCED_PRODUCTS(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Admin.ProdProcessedCount")),
    DEFECTIVE_PRODUCTS(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Admin.ProdDefectiveCount")),
    STOP_REASON_ID(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Admin.StopReason.ID")),
    STOP_REASON_VALUE(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Admin.StopReason.Value")),
    BATCH_PRODUCT_ID(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Admin.Parameter[0].Value"));

    public final NodeId nodeId;

    AdminNodes(NodeId nodeId) {
        this.nodeId = nodeId;
    }
}
