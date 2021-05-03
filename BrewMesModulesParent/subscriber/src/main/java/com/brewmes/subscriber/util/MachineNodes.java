package com.brewmes.subscriber.util;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public enum MachineNodes {
    BARLEY(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program.Inventory.Barley")),
    MALT(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program.Inventory.Malt")),
    HOPS(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program.Inventory.Hops")),
    WHEAT(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program.Inventory.Wheat")),
    YEAST(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program.Inventory.Yeast")),
    MAINTENANCE(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program.Maintenance"));

    public final NodeId nodeId;

    MachineNodes(NodeId nodeId) {
        this.nodeId = nodeId;
    }
}
